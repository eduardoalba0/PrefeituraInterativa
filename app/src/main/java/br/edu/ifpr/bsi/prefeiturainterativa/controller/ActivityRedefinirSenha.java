package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Transition;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.FirebaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.TransitionHelper;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class ActivityRedefinirSenha extends AppCompatActivity implements View.OnClickListener,
        Validator.ValidationListener {

    private Validator validador;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_redefinir_senha);
        ButterKnife.bind(this,this);
        startAnimation();
        validador = new Validator(this);
        validador.setValidationListener(this);
    }

    @OnClick({R.id.bt_redefinir})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_redefinir:
                validador.validate();
                break;
        }
    }

    @Override
    public void onValidationSucceeded() {
        edl_email.setError(null);
        Task task = new FirebaseHelper(this).redefinirSenha(edt_email.getText().toString());
        task.addOnSuccessListener(this, aVoid -> new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setContentText(getResources().getString(R.string.str_clique_link_redefinir_senha))
                .setConfirmClickListener(sweetAlertDialog -> chamarActivity(ActivityLogin.class)).show());
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            String mensagem = error.getCollatedErrorMessage(this);
            switch (error.getView().getId()) {
                case R.id.edt_email:
                    edl_email.setError(mensagem);
                    break;
            }
        }
    }

    public void startAnimation() {
        Transition t = TransitionHelper.inflateChangeBoundsTransition(this);
        t.addListener(TransitionHelper.getCircularEnterTransitionListener(img_app, view_root, l_redefinicao));
        getWindow().setSharedElementEnterTransition(t);
    }

    private <T> void chamarActivity(Class<T> activity) {
        Intent intent = new Intent(ActivityRedefinirSenha.this, activity);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(ActivityRedefinirSenha.this, img_app, "splash_transition");
        startActivity(intent, options.toBundle());
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActivityRedefinirSenha.this, ActivityLogin.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(ActivityRedefinirSenha.this, img_app, "splash_transition");
        startActivity(intent, options.toBundle());
    }

    @Email(message = "Seu e-mail está inválido.")
    @BindView(R.id.edt_email)
    TextInputEditText edt_email;

    @BindView(R.id.edl_email)
    TextInputLayout edl_email;

    @BindView(R.id.bt_redefinir)
    Button bt_redefinir;

    @BindView(R.id.view_root)
    View view_root;

    @BindView(R.id.img_app)
    View img_app;

    @BindView(R.id.l_redefinicao)
    View l_redefinicao;

}
