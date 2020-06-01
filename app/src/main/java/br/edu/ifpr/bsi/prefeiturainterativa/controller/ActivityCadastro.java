package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Transition;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.UsuarioDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.CpfValidatorHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.FirebaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.TransitionHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Usuario;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class ActivityCadastro extends AppCompatActivity implements View.OnClickListener,
        Validator.ValidationListener, OnSuccessListener<AuthResult> {

    private FirebaseHelper helper;
    private SweetAlertDialog dialog;
    private Validator validador;

    private Usuario usuario;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        ButterKnife.bind(this, this);
        startAnimation();
        validador = new Validator(this);
        validador.setValidationListener(this);
    }


    @OnClick(R.id.bt_cadastrar)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_cadastrar:
                validador.validate();
                break;
        }
    }

    @Override
    public void onValidationSucceeded() {
        if (!CpfValidatorHelper.validar(edt_cpf.getText().toString())) {
            edl_cpf.setError("Seu CPF está inválido.");
            return;
        }
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        helper = new FirebaseHelper(this);
        usuario = new Usuario();

        edl_nome.setError(null);
        edl_cpf.setError(null);
        edl_email.setError(null);
        edl_senha.setError(null);
        edl_senha_conf.setError(null);

        usuario.setEmail(edt_email.getText().toString());
        usuario.setSenha(edt_senha.getText().toString());
        usuario.setNome(edt_nome.getText().toString());
        usuario.setCpf(edt_cpf.getText().toString());
        usuario.setTipoUsuario_ID(getResources().getString(R.string.tipo_usuario_cidadao_id));

        Task<AuthResult> task = helper.registrar(usuario);
        if (task != null) {
            dialog.setTitleText(R.string.str_carregando_cadastro).show();
            task.addOnSuccessListener(this, this);
            task.addOnFailureListener(this, e -> {
                dialog.dismiss();
                if (task.getException().toString().contains("FirebaseAuthUserCollisionException"))
                    edl_email.setError(this.getResources().getString(R.string.str_erro_email_cadastrado));
            });
        }
    }

    @Override
    public void onSuccess(AuthResult authResult) {
        usuario.set_ID(authResult.getUser().getUid());
        List<Task<?>> tasks = new ArrayList<>();
        tasks.add(helper.atualizarPerfil(usuario));
        tasks.add(new UsuarioDAO(this).inserirAtualizar(usuario));
        tasks.add(helper.verificarEmail());
        Tasks.whenAllComplete(tasks).addOnSuccessListener(this, task -> {
            dialog.dismiss();
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setContentText(getResources().getString(R.string.str_clique_link_email))
                    .setConfirmClickListener(sweetAlertDialog -> chamarActivity(ActivityLogin.class))
                    .show();
        });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            String mensagem = error.getCollatedErrorMessage(this);
            switch (error.getView().getId()) {
                case R.id.edt_nome:
                    edl_nome.setError(mensagem);
                    break;
                case R.id.edt_cpf:
                    edl_cpf.setError(mensagem);
                    break;
                case R.id.edt_email:
                    edl_email.setError(mensagem);
                    break;
                case R.id.edt_senha:
                    edl_senha.setError(mensagem);
                    break;
                case R.id.edt_senha_conf:
                    edl_senha_conf.setError(mensagem);
                    break;
            }
        }
    }

    public void startAnimation() {
        Transition t = TransitionHelper.inflateChangeBoundsTransition(this, 0);
        t.addListener(TransitionHelper.getCircularEnterTransitionListener(img_app, view_root, l_cadastro));
        getWindow().setSharedElementEnterTransition(t);
    }

    public <T> void chamarActivity(Class<T> activity) {
        Intent intent = new Intent(ActivityCadastro.this, activity);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(ActivityCadastro.this, img_app, "splash_transition");
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onBackPressed() {
        chamarActivity(ActivityLogin.class);
    }

    @NotEmpty(message = "Campo obrigatório.", trim = true)
    @Length(min = 8, message = "Seu nome está inválido")
    @BindView(R.id.edt_nome)
    TextInputEditText edt_nome;

    @NotEmpty(message = "Campo obrigatório.", trim = true)
    @Length(max = 11, min = 11, message = "Seu CPF está inválido.")
    @BindView(R.id.edt_cpf)
    TextInputEditText edt_cpf;

    @NotEmpty(message = "Campo obrigatório.", trim = true)
    @Email(message = "Seu e-mail está inválido.")
    @BindView(R.id.edt_email)
    TextInputEditText edt_email;

    @NotEmpty(message = "Campo obrigatório.", trim = true)
    @Password(message = "Sua senha precisa ter no mínimo 6 caracteres.")
    @BindView(R.id.edt_senha)
    TextInputEditText edt_senha;

    @NotEmpty(message = "Campo obrigatório.", trim = true)
    @ConfirmPassword(message = "Suas senhas não conferem.")
    @BindView(R.id.edt_senha_conf)
    TextInputEditText edt_senha_conf;

    @BindView(R.id.edl_nome)
    TextInputLayout edl_nome;

    @BindView(R.id.edl_cpf)
    TextInputLayout edl_cpf;

    @BindView(R.id.edl_email)
    TextInputLayout edl_email;

    @BindView(R.id.edl_senha)
    TextInputLayout edl_senha;

    @BindView(R.id.edl_senha_conf)
    TextInputLayout edl_senha_conf;

    @BindView(R.id.bt_cadastrar)
    Button bt_cadastrar;

    @BindView(R.id.view_root)
    View view_root;

    @BindView(R.id.img_app)
    View img_app;

    @BindView(R.id.l_cadastro)
    View l_cadastro;

}
