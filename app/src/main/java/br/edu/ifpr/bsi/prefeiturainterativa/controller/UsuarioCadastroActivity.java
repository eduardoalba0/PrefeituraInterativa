package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Min;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.util.FirebaseHelper;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UsuarioCadastroActivity extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener {

    private Validator validator;
    private FirebaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_cadastro);
        startAnimation();
        ButterKnife.bind(this);
        helper = new FirebaseHelper(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @OnClick(R.id.bt_cadastrar)
    @Override
    public void onClick(View view) {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        edl_nome.setError(null);
        edl_cpf.setError(null);
        edl_email.setError(null);
        edl_senha.setError(null);
        edl_senha_conf.setError(null);
        helper.registrar(edt_email.getText().toString(), edt_senha.getText().toString(), bt_cadastrar);
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
        Fade fade = new Fade();
        fade.setDuration(1500);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);
    }

    @NotEmpty(message = "Campo obrigatório.", trim = true)
    @Min(value = 10, message = "Seu nome está inválido.")
    @BindView(R.id.edt_nome)
    TextInputEditText edt_nome;

    @NotEmpty(message = "Campo obrigatório.", trim = true)
    @Min(value = 11, message = "Seu CPF está inválido.")
    @BindView(R.id.edt_cpf)
    TextInputEditText edt_cpf;

    @NotEmpty(message = "Campo obrigatório.", trim = true)
    @Email(message = "Seu e-mail está inválido.")
    @BindView(R.id.edt_email)
    TextInputEditText edt_email;

    @NotEmpty(message = "Campo obrigatório.", trim = true)
    @Password(min = 6, message = "Sua senha precisa ter no mínimo 6 caracteres.")
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

}
