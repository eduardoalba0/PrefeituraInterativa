package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.util.FirebaseHelper;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UsuarioLoginActivity extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener {

    @NotEmpty(message = "Campo obrigatório", trim = true)
    @Email(message = "Seu e-mail está invalido")
    @BindView(R.id.edt_email)
    TextInputEditText edt_email;

    @NotEmpty(message = "Campo obrigatório", trim = true)
    @Password(min = 6, message = "Sua senha precisa ter no mínimo 6 caracteres.")
    @BindView(R.id.edt_senha)
    TextInputEditText edt_senha;

    @BindView(R.id.edl_email)
    TextInputLayout edl_email;

    @BindView(R.id.edl_senha)
    TextInputLayout edl_senha;

    @BindView(R.id.l_login)
    LinearLayout l_login;

    @BindView(R.id.bt_login)
    Button bt_login;

    @BindView(R.id.bt_login_google)
    MaterialButton bt_login_google;

    @BindView(R.id.bt_recuperar_senha)
    MaterialButton bt_recuperar_senha;

    @BindView(R.id.bt_cadastrar)
    MaterialButton bt_cadastrar;

    Validator validador;
    FirebaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_login);
        ButterKnife.bind(this);
        startAnimation();
        helper = new FirebaseHelper(this);
        validador = new Validator(this);
        validador.setValidationListener(this);
        //Verificar se o e-mail esta verificado antes de passar para a proxima activity
    }

    @OnClick({R.id.bt_login, R.id.bt_login_google, R.id.bt_recuperar_senha, R.id.bt_cadastrar})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                validador.validate();
                break;
            case R.id.bt_login_google:
                break;
            case R.id.bt_recuperar_senha:
                break;
            case R.id.bt_cadastrar:
                chamarActivity(UsuarioCadastroActivity.class, "cadastro_transition", l_login);
                break;
        }
    }

    @Override
    public void onValidationSucceeded() {
        edl_email.setError(null);
        edl_senha.setError(null);
        helper.logar(edt_email.getText().toString(), edt_senha.getText().toString(), bt_login);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String mensagem = error.getCollatedErrorMessage(this);
            switch (view.getId()) {
                case R.id.edt_email:
                    edl_email.setError(mensagem);
                    break;
                case R.id.edt_senha:
                    edl_senha.setError(mensagem);
            }
        }
    }

    public void startAnimation() {
        Fade fade = new Fade();
        fade.setDuration(1500);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);
    }

    public <T> void chamarActivity(Class<T> classe, String transicao, View componente) {
        Intent intent = new Intent(UsuarioLoginActivity.this, classe);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(UsuarioLoginActivity.this, componente, transicao);
        startActivity(intent, options.toBundle());
    }

}
