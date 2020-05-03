package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.lifecycle.ViewModelProvider;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.ViewModelsHelper;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class FragmentAnexosCamera extends Fragment implements Executor,
        View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Preview preview;
    private ImageCapture capture;
    private CameraX.LensFacing lensFacing;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anexos_camera, container, false);
        ButterKnife.bind(this, view);
        initCamera();
        return view;
    }

    @SuppressLint("RestrictedApi")
    @OnClick({R.id.bt_tirarFoto, R.id.bt_trocarCamera})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_tirarFoto:
                FragmentAnexosCameraPermissionsDispatcher.capturarImagemWithPermissionCheck(this);
                break;
            case R.id.bt_trocarCamera:
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
    public void execute(Runnable runnable) {
        runnable.run();
    }

    public void initCamera() {
        CameraX.unbindAll();
        if (lensFacing == null)
            lensFacing = CameraX.LensFacing.BACK;
        Size size = new Size(640, 480);
        PreviewConfig config = new PreviewConfig.Builder()
                .setLensFacing(lensFacing)
                .setTargetRotation(getActivity().getWindowManager().getDefaultDisplay().getRotation())
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
        capture.takePicture(file,
                this,
                new ImageCapture.OnImageSavedListener() {
                    @Override
                    public void onImageSaved(@NonNull File file) {
                        ViewModelsHelper viewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(ViewModelsHelper.class);
                        viewModel.getImagemCamera().postValue(file);
                    }

                    @Override
                    public void onError(@NonNull ImageCapture.ImageCaptureError imageCaptureError, @NonNull String message, @Nullable Throwable cause) {
                        Snackbar.make(getView(), "Falha ao capturar imagem. Se o erro persistir consulte o suporte do sistema",
                                BaseTransientBottomBar.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        FragmentAnexosCameraPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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
