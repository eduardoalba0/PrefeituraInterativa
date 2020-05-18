package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.io.IOException;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.FirebaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.ViewModelsHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;
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
public class FragmentSolicitacaoLocalizacao extends Fragment implements Step,
        OnMapReadyCallback, View.OnClickListener {

    private static final float MAP_ZOOM = 17.0f;
    private static final int RC_LOCAL = 111;

    private FirebaseHelper helper;
    private GoogleMap map_view;
    private Marker map_marker;
    private Geocoder geocoder;

    private ViewModelsHelper viewModel;
    private Solicitacao solicitacao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solicitacao_localizacao, container, false);
        ButterKnife.bind(this, view);
        solicitacao = new Solicitacao();
        initMap();
        return view;
    }

    @OnClick(R.id.bt_remover)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_remover:
                map_view.clear();
                tv_marcadorSelecionado.setText(R.string.str_marcadorNaoSelecionado);
                bt_remover.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map_view = googleMap;
        map_view.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map_view.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-26.484335, -51.991754), MAP_ZOOM));
        map_view.setOnMapClickListener(latLng -> {
            map_view.stopAnimation();
            map_view.clear();
            map_marker = map_view.addMarker(new MarkerOptions()
                    .position(latLng));
            map_marker.setTitle((getEndereco(latLng.latitude, latLng.longitude)));
        });
        map_view.setOnMyLocationClickListener(location -> {
            map_view.stopAnimation();
            map_view.clear();
            map_marker = map_view.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude())));
            map_marker.setTitle((getEndereco(location.getLatitude(), location.getLongitude())));
        });
        FragmentSolicitacaoLocalizacaoPermissionsDispatcher.updateLocationWithPermissionCheck(this);
    }

    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void updateLocation() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(1000)
                        .setFastestInterval(1000));

        Task<LocationSettingsResponse> result = LocationServices
                .getSettingsClient(getActivity())
                .checkLocationSettings(builder.build());

        result.addOnSuccessListener(getActivity(), locationSettingsResponse -> {
            LocationSettingsStates status = locationSettingsResponse.getLocationSettingsStates();
            if (status.isLocationPresent()) {
                map_view.setMyLocationEnabled(true);
                FusedLocationProviderClient locationProvider = LocationServices.getFusedLocationProviderClient(getActivity());
                locationProvider.getLastLocation().addOnSuccessListener(getActivity(), location -> {
                    if (location != null) {
                        map_view.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), MAP_ZOOM));
                    }
                });
            }
        }).addOnFailureListener(getActivity(), e -> {
            Log.e("Localização", "Failed Listener");
            int statusCode = ((ApiException) e).getStatusCode();
            if (statusCode == CommonStatusCodes.RESOLUTION_REQUIRED)
                try {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(getActivity(), RC_LOCAL);
                } catch (IntentSender.SendIntentException sendEx) {
                }
        });
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        if (map_marker == null || map_marker.getPosition() == null)
            return new VerificationError(getString(R.string.str_local_nao_informado));
        solicitacao.setLatitude(map_marker.getPosition().latitude);
        solicitacao.setLongitude(map_marker.getPosition().longitude);
        solicitacao.setEndereco(map_marker.getTitle());
        viewModel.postSolicitacao(solicitacao);
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {
        Snackbar.make(getView(), error.getErrorMessage(), BaseTransientBottomBar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.ms_errorColor))
                .setTextColor(getResources().getColor(R.color.ms_white))
                .show();
    }

    public String getEndereco(double latitude, double longitude) {
        String endereco = "";
        tv_marcadorSelecionado.setText(R.string.str_marcadorSelecionado);
        bt_remover.setVisibility(View.VISIBLE);
        if (helper.conexaoAtivada())
            try {
                geocoder = new Geocoder(getActivity(), Locale.getDefault());
                Address address = geocoder.getFromLocation(latitude, longitude, 1)
                        .get(0);
                if (address != null) {
                    String rua = address.getThoroughfare();
                    String numero = address.getSubThoroughfare();
                    String bairro = address.getAdminArea();

                    endereco = (rua == null ? "" : rua) +
                            (numero == null ? "" : ", " + numero) +
                            (bairro == null ? "" : ", " + bairro) + ".";
                }
            } catch (IOException ex) {

            }
        return endereco;
    }

    public void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        viewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(ViewModelsHelper.class);
        solicitacao = viewModel.getObjetoSolicitacao();
        helper = new FirebaseHelper(getActivity());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case RC_LOCAL:
                    FragmentSolicitacaoLocalizacaoPermissionsDispatcher.updateLocationWithPermissionCheck(this);
                    break;
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        FragmentSolicitacaoLocalizacaoPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
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

    @OnPermissionDenied({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void onPermissionDenied() {
        FragmentSolicitacaoLocalizacaoPermissionsDispatcher.updateLocationWithPermissionCheck(this);
    }

    @OnNeverAskAgain({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void onNeverAskAgain() {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setContentText(getString(R.string.str_never_ask_gps))
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
    @BindView(R.id.tv_marcadorSelecionado)
    TextView tv_marcadorSelecionado;

    @BindView(R.id.bt_remover)
    MaterialButton bt_remover;
}
