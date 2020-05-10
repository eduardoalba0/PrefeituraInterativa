package br.edu.ifpr.bsi.prefeiturainterativa.helpers.dialogs;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.GaleriaAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class SelectorGaleria extends Fragment {

    public boolean resultadoUnico;

    public SelectorGaleria(boolean resultadoUnico) {
        this.resultadoUnico = resultadoUnico;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_selector_galeria, container, false);
        ButterKnife.bind(this, view);
        SelectorGaleriaPermissionsDispatcher.initGaleriaWithPermissionCheck(this);
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SelectorGaleriaPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void initGaleria() {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getActivity());
        layoutManager.setFlexDirection(FlexDirection.COLUMN_REVERSE);
        layoutManager.setJustifyContent(JustifyContent.FLEX_END);
        rv_galeria.setLayoutManager(layoutManager);
        rv_galeria.setAdapter(new GaleriaAdapter(getActivity(), getImagens(), getChildFragmentManager(), resultadoUnico));
    }

    public List<String> getImagens() {
        Cursor cursor;
        int index_imagem;
        ArrayList<String> imagens = new ArrayList<>();

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        cursor = getActivity().getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Images.Media.DATE_MODIFIED + " DESC");

        index_imagem = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        while (cursor.moveToNext())
            imagens.add(cursor.getString(index_imagem));
        cursor.close();
        return imagens;
    }
    @OnShowRationale({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
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

    @OnPermissionDenied({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void onPermissionDenied() {
        SelectorGaleriaPermissionsDispatcher.initGaleriaWithPermissionCheck(this);
    }

    @OnNeverAskAgain({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void onNeverAskAgain() {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setContentText("Sem as permissões, não será possível anexar imagens, por favor, autorize-as.")
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

    @BindView(R.id.rv_galeria)
    RecyclerView rv_galeria;


}
