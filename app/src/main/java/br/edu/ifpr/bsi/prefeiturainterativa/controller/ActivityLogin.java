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

import java.util.ArrayList;
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
    private UsuarioDAO dao;
    private Usuario usuario;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this, this);
        startAnimation();
        helper = new FirebaseHelper(this);
        validador = new Validator(this);
        validador.setValidationListener(this);
        usuario = new Usuario();
        dao = new UsuarioDAO(this);
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
        edl_email.setError(null);
        edl_senha.setError(null);
        usuario = new Usuario();
        usuario.setEmail(edt_email.getText().toString());
        usuario.setSenha(edt_senha.getText().toString());
        Task<AuthResult> task = helper.logar(usuario);
        if (task != null)
            task.addOnSuccessListener(this, this);
    }

    @Override
    public void onSuccess(AuthResult authResult) {
        usuario.set_ID(authResult.getUser().getUid());
        if (!authResult.getUser().isEmailVerified()) {
            Task<Void> task = helper.verificarEmail();
            if (task != null)
                task.addOnSuccessListener(this, aVoid ->
                        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                                .setContentText(getResources().getString(R.string.str_clique_link_email))
                                .show());
            return;
        }

        Task<DocumentSnapshot> task = dao.get(usuario);
        if (task != null)
            task.addOnSuccessListener(this, documentSnapshot -> {
                Usuario aux = documentSnapshot.toObject(Usuario.class);
                if (aux == null || aux.getCpf() == null || aux.getCpf().trim().isEmpty())
                    if (task.getResult().getMetadata().isFromCache()) {
                        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(R.string.str_erro)
                                .setTitleText(getResources().getString(R.string.str_erro_internet_login))
                                .show();
                    } else
                        chamarActivity(ActivityCompletarCadastro.class);
                else {
                    List<String> tokens;
                    if (aux.getTokens() == null) {
                        tokens = new ArrayList<>();
                    } else
                        tokens = aux.getTokens();
                    helper.getToken().addOnSuccessListener(this, instanceIdResult -> {
                        tokens.add(instanceIdResult.getToken());
                        usuario.setTokens(tokens);
                        dao.inserirAtualizar(usuario)
                                .addOnSuccessListener(this, aVoid -> chamarActivity(ActivityOverview.class));
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
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                GoogleSignInAccount account = task.getResult(ApiException.class);
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
        Intent intent = helper.getSignInClient().getSignInIntent();
        startActivityForResult(intent, helper.RC_GOOGLE_LOGIN);
    }

    public <T> void chamarActivity(Class<T> activity) {
        Intent intent = new Intent(ActivityLogin.this, activity);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(ActivityLogin.this, img_app, "splash_transition");
        startActivity(intent, options.toBundle());
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
