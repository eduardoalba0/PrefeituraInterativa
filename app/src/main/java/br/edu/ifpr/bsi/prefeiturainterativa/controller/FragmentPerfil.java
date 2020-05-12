package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.UsuarioDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.FirebaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.ViewModelsHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.dialogs.DialogSelector;
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
public class FragmentPerfil extends Fragment implements View.OnClickListener,
        Validator.ValidationListener, OnSuccessListener<Void>, Observer<Uri> {

    private Validator validador;
    private FirebaseHelper helper;
    private UsuarioDAO dao;

    private Usuario usuario;
    private SweetAlertDialog dialog;
    private DialogSelector selector;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        ButterKnife.bind(this, view);
        helper = new FirebaseHelper(getActivity());
        validador = new Validator(this);
        validador.setValidationListener(this);
        dao = new UsuarioDAO(getActivity());
        dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        selector = new DialogSelector(true);
        ViewModelsHelper viewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(ViewModelsHelper.class);
        viewModel.getImagem().observe(getViewLifecycleOwner(), this);
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
                FragmentPerfilPermissionsDispatcher.pegarFotoWithPermissionCheck(this);
                break;
            case R.id.bt_sair:
                helper.deslogar();
                Intent intent = new Intent(getActivity(), ActivityLogin.class);
                startActivity(intent);
                break;
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
        dialog.setTitleText("Gravando alterações...").show();
        if (usuario.getLocalUriFoto() != null) {
            Task<Uri> upload = helper.carregarImagemUsuario(usuario.getLocalUriFoto());
            if (upload != null)
                upload.addOnSuccessListener(getActivity(), (Uri task) -> {
                    usuario.setLocalUriFoto(task);
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
        dao.inserirAtualizar(usuario).addOnSuccessListener(getActivity(), task -> {
            dialog.dismiss();
            new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Sucesso!")
                    .setContentText("Perfil atualizado com êxito!")
                    .show();
            preencherCampos();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        FragmentPerfilPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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
    public void onChanged(Uri imagem) {
        selector.dismiss();
        usuario.setLocalUriFoto(imagem);
        Glide.with(this)
                .load(imagem)
                .placeholder(R.drawable.ic_adicionar_foto)
                .circleCrop()
                .into(img_usuario);
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void pegarFoto() {
        selector.show(getChildFragmentManager(), "Selector");
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
        FragmentPerfilPermissionsDispatcher.pegarFotoWithPermissionCheck(this);
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
                                .placeholder(R.drawable.ic_adicionar_foto)
                                .circleCrop()
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
    ImageView img_usuario;

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
