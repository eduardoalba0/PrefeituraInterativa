package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.concurrent.Executor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraInfoUnavailableException;
import androidx.camera.core.CameraX;
import androidx.camera.core.FlashMode;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.fragment.app.Fragment;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.ViewerBottomSheetDialog;
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
public class FragmentAnexosCamera extends Fragment implements Executor,
        View.OnClickListener, CompoundButton.OnCheckedChangeListener, ImageCapture.OnImageSavedListener {

    private Preview preview;
    private ImageCapture capture;
    private CameraX.LensFacing lensFacing;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anexos_camera, container, false);
        ButterKnife.bind(this, view);
        FragmentAnexosCameraPermissionsDispatcher.initCameraWithPermissionCheck(this);
        return view;
    }

    @OnClick({R.id.bt_tirarFoto, R.id.bt_trocarCamera})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_tirarFoto:
                FragmentAnexosCameraPermissionsDispatcher.capturarImagemWithPermissionCheck(this);
                break;
            case R.id.bt_trocarCamera:
                FragmentAnexosCameraPermissionsDispatcher.trocarCameraWithPermissionCheck(this);
                break;
        }
    }

    @OnCheckedChanged(R.id.bt_flash)
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.bt_flash:
                if (b)
                    bt_flash.setButtonDrawable(getResources().getDrawable(R.drawable.ic_flash));
                else
                    bt_flash.setButtonDrawable(getResources().getDrawable(R.drawable.ic_flash_off));
                break;
        }
    }

    @Override
    public void onImageSaved(@NonNull File file) {
        ViewerBottomSheetDialog viewer = new ViewerBottomSheetDialog();
        Bundle bundle = new Bundle();
        bundle.putString("Imagem", file.getAbsolutePath());
        viewer.setArguments(bundle);
        viewer.show(getChildFragmentManager(), "Viewer");
    }

    @Override
    public void onError(@NonNull ImageCapture.ImageCaptureError imageCaptureError, @NonNull String message, @Nullable Throwable cause) {
        Snackbar.make(getView(), "Falha ao capturar imagem. Se o erro persistir consulte o suporte do sistema",
                BaseTransientBottomBar.LENGTH_LONG).show();
    }

    @Override
    public void execute(Runnable runnable) {
        runnable.run();
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void initCamera() {
        CameraX.unbindAll();
        if (lensFacing == null)
            lensFacing = CameraX.LensFacing.BACK;
        Size size = new Size(480, 720);
        PreviewConfig config = new PreviewConfig.Builder()
                .setLensFacing(lensFacing)
                .setTargetResolution(size).build();
        preview = new Preview(config);

        ImageCaptureConfig captureConfig = new ImageCaptureConfig.Builder()
                .setLensFacing(lensFacing)
                .setTargetResolution(size).build();
        capture = new ImageCapture(captureConfig);
        CameraX.bindToLifecycle(this, preview, capture);
        view_camera.post(() -> {
            FragmentAnexosCameraPermissionsDispatcher.abrirCameraWithPermissionCheck(this);
        });
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void abrirCamera() {
        preview.setOnPreviewOutputUpdateListener(previewOutput -> {
            ViewGroup parent = (ViewGroup) view_camera.getParent();
            parent.removeView(view_camera);
            parent.addView(view_camera);
            view_camera.setSurfaceTexture(previewOutput.getSurfaceTexture());
        });

    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void capturarImagem() {
        File file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + ".jpg");
        capture.setFlashMode(bt_flash.isChecked() ? FlashMode.ON : FlashMode.OFF);
        capture.takePicture(file, this, this);
    }

    @SuppressLint("RestrictedApi")
    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void trocarCamera() {
        CameraX.LensFacing backup = lensFacing;
        try {
            if (lensFacing == CameraX.LensFacing.FRONT)
                lensFacing = CameraX.LensFacing.BACK;
            else
                lensFacing = CameraX.LensFacing.FRONT;
            CameraX.getCameraWithLensFacing(lensFacing);
            initCamera();
        } catch (CameraInfoUnavailableException e) {
            lensFacing = backup;
            Snackbar.make(getView(), "Falha ao trocar a câmera. Se o erro persistir consulte o suporte do sistema",
                    BaseTransientBottomBar.LENGTH_LONG).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        FragmentAnexosCameraPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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
        FragmentAnexosCameraPermissionsDispatcher.initCameraWithPermissionCheck(this);
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
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

    @BindView(R.id.view_camera)
    TextureView view_camera;

    @BindView(R.id.bt_flash)
    ToggleButton bt_flash;

    @BindView(R.id.bt_tirarFoto)
    FloatingActionButton bt_tirar_foto;

    @BindView(R.id.bt_trocarCamera)
    FloatingActionButton bt_trocarCamera;

}
