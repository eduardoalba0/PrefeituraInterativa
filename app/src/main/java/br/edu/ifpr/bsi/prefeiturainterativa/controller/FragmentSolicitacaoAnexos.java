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
import android.widget.CompoundButton;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.AnexosAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.ViewModelsHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.dialogs.DialogSelector;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class FragmentSolicitacaoAnexos extends Fragment implements Step, View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    private DialogSelector dialogo;
    private Solicitacao solicitacao;
    private ViewModelsHelper viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solicitacao_anexos, container, false);
        ButterKnife.bind(this, view);
        solicitacao = new Solicitacao();
        initRecyclerView();
        return view;
    }

    @OnClick(R.id.bt_adicionar)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_adicionar:
                FragmentSolicitacaoAnexosPermissionsDispatcher.inserirImagensWithPermissionCheck(this);
                break;
        }
    }

    @OnCheckedChanged(R.id.sw_anonimo)
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.sw_anonimo:
                solicitacao.setAnonima(b);
                break;
        }
    }

    public void initRecyclerView() {
        viewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(ViewModelsHelper.class);
        this.solicitacao = viewModel.getObjetoSolicitacao();
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getActivity());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.CENTER);
        rv_imagens.setLayoutManager(layoutManager);

        viewModel.getImagens().observe(getViewLifecycleOwner(),
                strings -> {
                    if (strings.isEmpty())
                        rv_imagens.setAdapter(new AnexosAdapter(getActivity(), new ArrayList<>(), getChildFragmentManager()));
                    else {
                        rv_imagens.setAdapter(new AnexosAdapter(getActivity(), strings, getChildFragmentManager()));
                        if (dialogo != null)
                            dialogo.dismiss();
                    }
                });
    }


    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void inserirImagens() {
        if (viewModel.getListImagens().size() >= 3) {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(R.string.str_erro)
                    .setContentText(getString(R.string.str_erro_limite_imagens))
                    .show();
            return;
        }
        if (dialogo == null)
            dialogo = new DialogSelector(false);
        dialogo.show(getChildFragmentManager(), "Selector");
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        if (viewModel.getListImagens().isEmpty() && edt_descricao.getText().toString().trim().equals(""))
            return new VerificationError(getString(R.string.str_sem_anexos_descricao));
        edl_descricao.setError(null);
        solicitacao.setDescricao(edt_descricao.getText().toString());
        solicitacao.setAnonima(sw_anonimo.isChecked());
        viewModel.postSolicitacao(solicitacao);
        return null;
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        edl_descricao.setError(error.getErrorMessage());
        Snackbar.make(getView(), error.getErrorMessage(), BaseTransientBottomBar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.ms_errorColor))
                .setTextColor(getResources().getColor(R.color.ms_white))
                .show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        FragmentSolicitacaoAnexosPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void showRationale(PermissionRequest request) {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setContentText(getString(R.string.str_rationale))
                .setCancelButton(R.string.str_cancelar, Dialog::dismiss)
                .setConfirmButton(R.string.str_confirmar, sweetAlertDialog -> {
                    request.proceed();
                    sweetAlertDialog.dismiss();
                })
                .show();
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void onPermissionDenied() {
        FragmentSolicitacaoAnexosPermissionsDispatcher.inserirImagensWithPermissionCheck(this);
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void onNeverAskAgain() {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setContentText(getString(R.string.str_never_ask_imagens))
                .setCancelButton(R.string.str_cancelar, Dialog::dismiss)
                .setConfirmButton(R.string.str_confirmar, sweetAlertDialog -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                    intent.setData(uri);
                    sweetAlertDialog.dismiss();
                    startActivity(intent);
                })
                .show();
    }

    @Override
    public void onSelected() {
    }

    @BindView(R.id.rv_imagens)
    RecyclerView rv_imagens;

    @BindView(R.id.bt_adicionar)
    MaterialButton bt_adicionar;

    @BindView(R.id.sw_anonimo)
    SwitchMaterial sw_anonimo;


    @BindView(R.id.edt_descricao)
    TextInputEditText edt_descricao;

    @BindView(R.id.edl_descricao)
    TextInputLayout edl_descricao;

}
