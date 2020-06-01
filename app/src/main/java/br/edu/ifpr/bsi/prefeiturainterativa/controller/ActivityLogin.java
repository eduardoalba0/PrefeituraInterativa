package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Transition;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.UsuarioDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.FirebaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.TransitionHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Usuario;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener,
        Validator.ValidationListener, OnSuccessListener<AuthResult> {

    private Validator validador;
    private FirebaseHelper helper;
    private Usuario usuario;
    private SweetAlertDialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this, this);
        startAnimation();
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText(R.string.str_autenticando);
        helper = new FirebaseHelper(this);
        validador = new Validator(this);
        validador.setValidationListener(this);
    }

    @OnClick({R.id.bt_login, R.id.bt_loginGoogle, R.id.bt_recuperarSenha, R.id.bt_cadastrar})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                validador.validate();
                break;
            case R.id.bt_loginGoogle:
                logarGoogle();
                break;
            case R.id.bt_recuperarSenha:
                chamarActivity(ActivityRedefinirSenha.class);
                break;
            case R.id.bt_cadastrar:
                chamarActivity(ActivityCadastro.class);
                break;
        }
    }

    @Override
    public void onValidationSucceeded() {
        dialog.show();
        edl_email.setError(null);
        edl_senha.setError(null);
        usuario = new Usuario();
        usuario.setEmail(edt_email.getText().toString());
        usuario.setSenha(edt_senha.getText().toString());
        Task<AuthResult> task = helper.logar(usuario);
        if (task != null) {
            task.addOnSuccessListener(this, this);
            task.addOnFailureListener(this, e -> dialog.dismiss());
        } else
            dialog.dismiss();
    }

    @Override
    public void onSuccess(AuthResult authResult) {
        UsuarioDAO dao = new UsuarioDAO(this);
        usuario = new Usuario();
        usuario.set_ID(authResult.getUser().getUid());
        if (!authResult.getUser().isEmailVerified()) {
            Task<Void> task = helper.verificarEmail();
            if (task != null) {
                task.addOnCompleteListener(this, e -> dialog.dismiss());
                task.addOnSuccessListener(this, aVoid ->
                        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                                .setContentText(getResources().getString(R.string.str_clique_link_email))
                                .show());
            } else dialog.dismiss();
            return;
        }
        Task<DocumentSnapshot> task = dao.get(usuario);
        task.addOnFailureListener(this, e -> dialog.dismiss());
        task.addOnSuccessListener(this, documentSnapshot -> {
            usuario = documentSnapshot.toObject(Usuario.class);
            if (usuario == null || usuario.getCpf() == null || usuario.getCpf().trim().isEmpty()) {
                dialog.dismiss();
                chamarActivity(ActivityCompletarCadastro.class);
            } else {
                helper.getToken().addOnSuccessListener(this, instanceIdResult -> {
                    if (usuario.getToken() == null || !usuario.getToken().equals(instanceIdResult.getToken())) {
                        usuario.setToken(instanceIdResult.getToken());
                        Task taskInsert = dao.inserirAtualizar(usuario);
                        taskInsert.addOnCompleteListener(this, e -> dialog.dismiss());
                        taskInsert.addOnSuccessListener(this, aVoid -> chamarActivity(ActivityOverview.class));
                    } else {
                        dialog.dismiss();
                        chamarActivity(ActivityOverview.class);
                    }
                });
            }
        });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            String mensagem = error.getCollatedErrorMessage(this);
            switch (error.getView().getId()) {
                case R.id.edt_email:
                    edl_email.setError(mensagem);
                    break;
                case R.id.edt_senha:
                    edl_senha.setError(mensagem);
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == helper.RC_GOOGLE_LOGIN) {
            try {
                GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data)
                        .getResult(ApiException.class);
                Task<AuthResult> auth = helper.logarGoogle(account);
                if (auth != null)
                    auth.addOnSuccessListener(this, this);
            } catch (ApiException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void startAnimation() {
        Transition t = TransitionHelper.inflateChangeBoundsTransition(this);
        t.addListener(TransitionHelper.getCircularEnterTransitionListener(img_app, view_root, l_login, l_footer));
        getWindow().setSharedElementEnterTransition(t);
    }

    public void logarGoogle() {
        if (helper.conexaoAtivada()) {
            Intent intent = helper.getSignInClient().getSignInIntent();
            startActivityForResult(intent, helper.RC_GOOGLE_LOGIN);
        } else new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(R.string.str_erro)
                .setContentText(getResources().getString(R.string.str_erro_internet_login))
                .show();
    }

    public <T> void chamarActivity(Class<T> activity) {
        Intent intent = new Intent(ActivityLogin.this, activity);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(ActivityLogin.this, img_app, "splash_transition");
        startActivity(intent, options.toBundle());
        finish();
    }

    @Email(message = "Seu e-mail está inválido.")
    @BindView(R.id.edt_email)
    TextInputEditText edt_email;

    @Password(min = 6, message = "Sua senha precisa ter no mínimo 6 caracteres.")
    @BindView(R.id.edt_senha)
    TextInputEditText edt_senha;

    @BindView(R.id.edl_email)
    TextInputLayout edl_email;

    @BindView(R.id.edl_senha)
    TextInputLayout edl_senha;

    @BindView(R.id.bt_login)
    Button bt_login;

    @BindView(R.id.bt_loginGoogle)
    MaterialButton bt_loginGoogle;

    @BindView(R.id.bt_recuperarSenha)
    MaterialButton bt_recuperarSenha;

    @BindView(R.id.bt_cadastrar)
    MaterialButton bt_cadastrar;

    @BindView(R.id.view_root)
    View view_root;

    @BindView(R.id.img_app)
    View img_app;

    @BindView(R.id.l_login)
    View l_login;

    @BindView(R.id.l_footer)
    View l_footer;
}
