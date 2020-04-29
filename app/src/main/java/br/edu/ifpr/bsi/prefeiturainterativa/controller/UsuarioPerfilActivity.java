package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.commit451.modalbottomsheetdialogfragment.ModalBottomSheetDialogFragment;
import com.commit451.modalbottomsheetdialogfragment.Option;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import br.edu.ifpr.bsi.prefeiturainterativa.BuildConfig;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.UsuarioDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.FirebaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Usuario;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class UsuarioPerfilActivity extends Fragment implements View.OnClickListener,
        Validator.ValidationListener, OnSuccessListener<Void>, ModalBottomSheetDialogFragment.Listener {

    private static final int REQ_GALERIA = 11, REQ_CAMERA = 12;

    private Validator validador;
    private FirebaseHelper helper;
    private UsuarioDAO dao;

    private Usuario usuario;
    private SweetAlertDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_usuario_perfil, container, false);
        ButterKnife.bind(this, view);
        helper = new FirebaseHelper(getActivity());
        validador = new Validator(this);
        validador.setValidationListener(this);
        dao = new UsuarioDAO(getActivity());
        dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        preencherCampos();
        return view;
    }

    @OnClick({R.id.bt_atualizar, R.id.bt_redefinir, R.id.img_usuario, R.id.bt_sair})
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
                UsuarioPerfilActivityPermissionsDispatcher.abrirAppSelectorWithPermissionCheck(this);
                break;
            case R.id.bt_sair:
                helper.deslogar();
                Intent intent = new Intent(getActivity(), UsuarioLoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onModalOptionSelected(@org.jetbrains.annotations.Nullable String s, @NotNull Option option) {
        switch (option.getId()) {
            case R.id.action_camera:
                usarCamera();
                break;
            case R.id.action_galeria:
                usarGaleria();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ActivityTemplate.RESULT_OK) {
            switch (requestCode) {
                case REQ_GALERIA:
                    Uri selectedImage = data.getData();
                    usuario.setUriFoto(selectedImage);
                    Glide.with(this).load(selectedImage).into(img_usuario);
                    break;

                case REQ_CAMERA:
                    Glide.with(this).load(usuario.getUriFoto()).into(img_usuario);
                    break;

            }
        }
    }

    @Override
    public void onValidationSucceeded() {
        edl_nome.setError(null);
        edl_cpf.setError(null);
        edl_email.setError(null);

        usuario.setEmail(edt_email.getText().toString());
        usuario.setNome(edt_nome.getText().toString());
        usuario.setCpf(edt_cpf.getText().toString());
        if (usuario.getUriFoto() != null) {
            Task<Uri> upload = helper.carregarImagemUsuario(usuario.getUriFoto());
            if (upload != null)
                upload.addOnSuccessListener(getActivity(), (Uri task) -> {
                    usuario.setUriFoto(task);
                    Task<Void> update = helper.atualizarPerfil(usuario);
                    if (update != null)
                        update.addOnSuccessListener(getActivity(), this);
                });
        } else {
            Task<Void> task = helper.atualizarPerfil(usuario);
            if (task != null)
                task.addOnSuccessListener(getActivity(), this);
        }
    }

    @Override
    public void onSuccess(Void avoid) {
        dialog.setTitleText("Gravando alterações...").show();
        helper.atualizarPerfil(usuario).addOnSuccessListener(getActivity(), aVoid ->
                dao.inserirAtualizar(usuario).addOnSuccessListener(getActivity(), task -> {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Sucesso!")
                            .setContentText("Perfil atualizado com êxito!")
                            .show();
                    preencherCampos();
                }));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        UsuarioPerfilActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void abrirAppSelector() {
        new ModalBottomSheetDialogFragment.Builder()
                .add(R.menu.menu_imagens)
                .columns(2)
                .layout(R.layout.layout_item_modalbottomsheet)
                .show(getChildFragmentManager(), "AppSelector");
    }

    public void usarGaleria() {
        Intent pegarFoto = new Intent(Intent.ACTION_PICK);
        pegarFoto.setType("image/*");
        startActivityForResult(Intent.createChooser(pegarFoto, "Continue com:"), REQ_GALERIA);
    }

    public void usarCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            try {
                File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File photoFile = File.createTempFile(helper.getUser().getUid(), ".jpg", storageDir);
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".fileprovider", photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    usuario.setUriFoto(photoURI);
                    startActivityForResult(takePictureIntent, REQ_CAMERA);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void showRationale(PermissionRequest request) {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setContentText("Para prosseguir, autorize as seguintes permissões:")
                .setCancelButton("Cancelar", Dialog::dismiss)
                .setConfirmButton("OK", sweetAlertDialog -> {
                    request.proceed();
                    sweetAlertDialog.dismiss();
                })
                .show();
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void onPermissionDenied() {
        img_usuario.callOnClick();
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void onNeverAskAgain() {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setContentText("Sem as permissões, não será possível trocar sua foto, por favor, autorize-as.")
                .setCancelButton("Cancelar", Dialog::dismiss)
                .setConfirmButton("OK", sweetAlertDialog -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                    intent.setData(uri);
                    sweetAlertDialog.dismiss();
                    startActivity(intent);
                })
                .show();
    }

    private void preencherCampos() {
        usuario = new Usuario();
        usuario.set_ID(helper.getUser().getUid());
        Task<DocumentSnapshot> task = dao.get(usuario);

        if (task != null) {
            task.addOnSuccessListener(getActivity(), documentSnapshot -> {
                usuario = documentSnapshot.toObject(Usuario.class);
                if (usuario != null) {
                    tv_usuario.setText(usuario.getNome());
                    edt_nome.setText(usuario.getNome());
                    edt_email.setText(usuario.getEmail());
                    edt_cpf.setText(usuario.getCpf());
                    if (helper.getUser().getPhotoUrl() != null)
                        Glide.with(this)
                                .load(helper.getUser().getPhotoUrl())
                                .into(img_usuario);
                    dialog.dismiss();
                }
            });
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

    @BindView(R.id.img_usuario)
    CircularImageView img_usuario;

    @BindView(R.id.tv_usuario)
    TextView tv_usuario;

    @BindView(R.id.bt_sair)
    Button bt_sair;

    @BindView(R.id.bt_redefinir)
    Button bt_redefinir;

    @BindView(R.id.bt_atualizar)
    Button bt_atualizar;

    @BindView(R.id.view_root)
    View view_root;

    @BindView(R.id.l_perfil)
    View l_perfil;

    @BindView(R.id.edl_nome)
    TextInputLayout edl_nome;

    @BindView(R.id.edl_cpf)
    TextInputLayout edl_cpf;

    @BindView(R.id.edl_email)
    TextInputLayout edl_email;

}
