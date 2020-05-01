package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Transition;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class ActivityCadastro extends AppCompatActivity implements View.OnClickListener,
        Validator.ValidationListener, OnSuccessListener<AuthResult> {

    private Validator validador;
    private FirebaseHelper helper;
    private UsuarioDAO dao;

    private Usuario usuario;
    private SweetAlertDialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        ButterKnife.bind(this, this);
        startAnimation();
        helper = new FirebaseHelper(this);
        validador = new Validator(this);
        validador.setValidationListener(this);
        dao = new UsuarioDAO(this);
        usuario = new Usuario();
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
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
        edl_nome.setError(null);
        edl_cpf.setError(null);
        edl_email.setError(null);
        edl_senha.setError(null);
        edl_senha_conf.setError(null);

        usuario = new Usuario();
        usuario.setEmail(edt_email.getText().toString());
        usuario.setSenha(edt_senha.getText().toString());
        usuario.setNome(edt_nome.getText().toString());
        usuario.setCpf(edt_cpf.getText().toString());

        Task<AuthResult> task = helper.registrar(usuario);
        dialog.setTitleText("Cadastrando...").show();
        if (task != null)
            task.addOnSuccessListener(this, this)
                    .addOnFailureListener(this, e -> {
                        if (task.getException().toString().contains("FirebaseAuthUserCollisionException"))
                            edl_email.setError("Seu e-mail já está cadastrado.");
                    });
    }

    @Override
    public void onSuccess(AuthResult authResult) {
        usuario.set_ID(authResult.getUser().getUid());

        helper.atualizarPerfil(usuario).addOnSuccessListener(this, aVoid ->

                dao.inserirAtualizar(usuario).addOnSuccessListener(this, task ->

                        helper.verificarEmail().addOnSuccessListener(this, bVoid -> {
                            dialog.dismiss();
                            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                                        .setContentText("Clique no link enviado para seu e-mail para ativar a conta.")
                                        .setConfirmClickListener(sweetAlertDialog -> {
                                            Intent intent = new Intent(ActivityCadastro.this, ActivityLogin.class);
                                            ActivityOptionsCompat options = ActivityOptionsCompat.
                                                    makeSceneTransitionAnimation(ActivityCadastro.this, img_app, "splash_transition");
                                            startActivity(intent, options.toBundle());
                                        }).show();
                        })));
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActivityCadastro.this, ActivityLogin.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(ActivityCadastro.this, img_app, "splash_transition");
        startActivity(intent, options.toBundle());
    }

    public void startAnimation() {
        Transition t = TransitionHelper.inflateChangeBoundsTransition(this, 0);
        t.addListener(TransitionHelper.getCircularEnterTransitionListener(img_app, view_root, l_cadastro));
        getWindow().setSharedElementEnterTransition(t);
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
