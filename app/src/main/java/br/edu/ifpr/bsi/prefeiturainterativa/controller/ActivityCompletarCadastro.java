package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.transition.Transition;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
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

public class ActivityCompletarCadastro extends AppCompatActivity implements View.OnClickListener,
        Validator.ValidationListener, OnSuccessListener<Void> {

    private Validator validador;
    private FirebaseHelper helper;
    private UsuarioDAO dao;
    private Usuario usuario;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completar_cadastro);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        ButterKnife.bind(this, this);
        startAnimation();
        helper = new FirebaseHelper(this);
        validador = new Validator(this);
        validador.setValidationListener(this);
        dao = new UsuarioDAO(this);
        usuario = new Usuario();
        preencherCampos();
    }

    @OnClick({R.id.bt_cadastrar, R.id.bt_sair})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_cadastrar:
                validador.validate();
                break;
            case R.id.bt_sair:
                helper.deslogar();
                Intent intent = new Intent(ActivityCompletarCadastro.this, ActivityLogin.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(ActivityCompletarCadastro.this, img_app, "splash_transition");
                startActivity(intent, options.toBundle());
                break;
        }
    }

    @Override
    public void onValidationSucceeded() {
        edl_cpf.setError(null);
        usuario.setCpf(edt_cpf.getText().toString());
        dao.inserirAtualizar(usuario).addOnSuccessListener(this, this);
    }

    @Override
    public void onSuccess(Void tVoid) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
        edit.putString(usuario.get_ID(), usuario.getCpf());
        edit.apply();
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Sucesso! ")
                .setContentText("Cadastro completo.")
                .setConfirmClickListener(sweetAlertDialog -> {
                    Intent intent = new Intent(ActivityCompletarCadastro.this, ActivityOverview.class);
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

    @Override
    public void onBackPressed() {
        helper.deslogar();
        Intent intent = new Intent(ActivityCompletarCadastro.this, ActivityLogin.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, img_app, "splash_transition");
        startActivity(intent, options.toBundle());
    }

    public void preencherCampos() {
        usuario.set_ID(helper.getUser().getUid());
        usuario.setNome(helper.getUser().getDisplayName());
        usuario.setEmail(helper.getUser().getEmail());
        usuario.setTipoUsuario_ID(getResources().getString(R.string.tipo_usuario_cidadao_id));
        tv_usuario.setText(usuario.getNome());
        if (helper.getUser().getPhotoUrl() != null)
            Glide.with(this)
                    .load(helper.getUser().getPhotoUrl())
                    .placeholder(R.mipmap.ic_maca)
                    .circleCrop()
                    .into(img_app);
    }
    public void startAnimation() {
        Transition t = TransitionHelper.inflateChangeBoundsTransition(this);
        t.addListener(TransitionHelper.getCircularEnterTransitionListener(img_app, view_root, tv_usuario, bt_sair, l_completar, shape_irregular));
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

    @BindView(R.id.bt_sair)
    Button bt_sair;

    @BindView(R.id.tv_usuario)
    TextView tv_usuario;

    @BindView(R.id.view_root)
    View view_root;

    @BindView(R.id.img_app)
    ImageView img_app;

    @BindView(R.id.l_completar)
    View l_completar;

    @BindView(R.id.shape_irregular)
    View shape_irregular;

}
