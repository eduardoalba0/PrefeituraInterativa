package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.UsuarioDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.FirebaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Usuario;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class UsuarioPerfilActivity extends Fragment implements View.OnClickListener,
        Validator.ValidationListener, OnSuccessListener<Void> {
    private static final int REQ_GALERIA = 11;
    private Validator validador;
    private FirebaseHelper helper;
    private UsuarioDAO dao;
    private Usuario usuario;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_usuario_perfil, container, false);
        ButterKnife.bind(this, view);
        //animacao
        helper = new FirebaseHelper(getActivity());
        validador = new Validator(getActivity());
        validador.setValidationListener(this);
        dao = new UsuarioDAO(getActivity());
        usuario = new Usuario();
        return view;
    }

    @OnClick({R.id.bt_atualizar, R.id.bt_redefinir, R.id.img_usuario})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_atualizar:
                validador.validate();
                break;
            case R.id.bt_redefinir:
                helper.redefinirSenha(helper.getUser().getEmail()).addOnSuccessListener(getActivity(), aVoid -> new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setContentText("Clique no link enviado para seu e-mail para redefinir sua senha.")
                        .show());
                break;
            case R.id.img_usuario:
                Intent pegarFoto = new Intent(Intent.ACTION_PICK);
                pegarFoto.setType("image/*");
                startActivityForResult(Intent.createChooser(pegarFoto, "Continue com:"), REQ_GALERIA);
                break;
        }
    }

    @Override
    public void onValidationSucceeded() {
        edl_nome.setError(null);
        edl_cpf.setError(null);
        edl_email.setError(null);

        usuario = new Usuario();
        usuario.set_ID(helper.getUser().getUid());
        usuario.setEmail(edt_email.getText().toString());
        usuario.setNome(edt_nome.getText().toString());
        usuario.setCpf(edt_cpf.getText().toString());

        Bitmap imagem = ((BitmapDrawable) img_usuario.getDrawable()).getBitmap();
        if (imagem != null) {
            StorageTask<UploadTask.TaskSnapshot> upload = helper.carregarImagemUsuario(imagem);
            if (upload != null)
                upload.addOnSuccessListener(getActivity(), taskSnapshot -> {
                    usuario.setUrlFoto(taskSnapshot.getMetadata().getPath());
                    Task<Void> task = helper.atualizarPerfil(usuario);
                    if (task != null)
                        task.addOnSuccessListener(getActivity(), this);
                });
        } else {
            Task<Void> task = helper.atualizarPerfil(usuario);
            if (task != null)
                task.addOnSuccessListener(getActivity(), this);
        }
    }

    @Override
    public void onSuccess(Void avoid) {
        helper.atualizarPerfil(usuario).addOnSuccessListener(getActivity(), aVoid ->
                dao.inserirAtualizar(usuario).addOnSuccessListener(getActivity(), task ->
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Sucesso!")
                                .setContentText("Perfil atualizado com êxito!")
                                .show()));
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            String mensagem = error.getCollatedErrorMessage(getActivity());
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
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ActivityTemplate.RESULT_OK) {
            if (requestCode == REQ_GALERIA) {
                String[] dados = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(data.getData(), dados, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    img_usuario.setImageBitmap(helper.decodificarImagem(cursor.getString(cursor.getColumnIndex(dados[0]))));
                    cursor.close();
                } else
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Erro!")
                            .setContentText("Falha ao carregar imagem. Consulte o suporte do sistema.")
                            .show();
            }
        }

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

    @BindView(R.id.edl_nome)
    TextInputLayout edl_nome;

    @BindView(R.id.edl_cpf)
    TextInputLayout edl_cpf;

    @BindView(R.id.edl_email)
    TextInputLayout edl_email;

    @BindView(R.id.img_usuario)
    CircularImageView img_usuario;

    @BindView(R.id.bt_redefinir)
    Button bt_redefinir;

    @BindView(R.id.bt_atualizar)
    Button bt_atualizar;

    @BindView(R.id.view_root)
    View view_root;

    @BindView(R.id.l_perfil)
    View l_perfil;


}
