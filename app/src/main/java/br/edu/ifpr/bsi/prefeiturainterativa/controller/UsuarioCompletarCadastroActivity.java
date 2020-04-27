package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Transition;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

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

public class UsuarioCompletarCadastroActivity extends AppCompatActivity implements View.OnClickListener,
        Validator.ValidationListener, OnSuccessListener<Void> {

    private Validator validador;
    private FirebaseHelper helper;
    private UsuarioDAO dao;
    private Usuario usuario;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_completar_dados);
        ButterKnife.bind(this, this);
        startAnimation();
        helper = new FirebaseHelper(this);
        validador = new Validator(this);
        validador.setValidationListener(this);
        dao = new UsuarioDAO(this);
        usuario = new Usuario();
    }

    @OnClick({R.id.bt_cadastrar})
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
        edl_cpf.setError(null);
        usuario = new Usuario();
        usuario.set_ID(helper.getUser().getUid());
        usuario.setNome(helper.getUser().getDisplayName());
        usuario.setCpf(edt_cpf.getText().toString());
        usuario.setEmail(helper.getUser().getEmail());
        dao.inserirAtualizar(usuario).addOnSuccessListener(this, this);
    }

    @Override
    public void onSuccess(Void tVoid) {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Sucesso! ")
                .setContentText("Cadastro completo.")
                .setConfirmClickListener(sweetAlertDialog -> {
                    Intent intent = new Intent(UsuarioCompletarCadastroActivity.this, ActivityTemplate.class);
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(this, img_app, "splash_transition");
                    startActivity(intent, options.toBundle());
                }).show();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            String mensagem = error.getCollatedErrorMessage(this);
            switch (error.getView().getId()) {
                case R.id.edt_cpf:
                    edl_cpf.setError(mensagem);
                    break;
            }
        }
    }

    public void startAnimation() {
        Transition t = TransitionHelper.inflateChangeBoundsTransition(this);
        t.addListener(TransitionHelper.getCircularEnterTransitionListener(img_app, view_root, l_completar));
        getWindow().setSharedElementEnterTransition(t);
    }

    @NotEmpty(message = "Campo obrigatório.", trim = true)
    @Length(max = 11, min = 11, message = "Seu CPF está inválido.")
    @BindView(R.id.edt_cpf)
    TextInputEditText edt_cpf;

    @BindView(R.id.edl_cpf)
    TextInputLayout edl_cpf;

    @BindView(R.id.bt_cadastrar)
    Button bt_cadastrar;

    @BindView(R.id.view_root)
    View view_root;

    @BindView(R.id.img_app)
    View img_app;

    @BindView(R.id.l_completar)
    View l_completar;

}
