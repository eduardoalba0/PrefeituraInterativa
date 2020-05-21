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
import android.os.Handler;
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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
public class FragmentSolicitacaoLocalizacao extends Fragment implements Step, View.OnClickListener,
        OnFailureListener, OnSuccessListener<LocationSettingsResponse> {

    public static final int RC_LOCAL = 111;

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
        viewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(ViewModelsHelper.class);
        solicitacao = viewModel.getObjetoSolicitacao();
        helper = new FirebaseHelper(getActivity());
        initMap();
        return view;
    }

    @OnClick(R.id.bt_remover)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_remover:
                map_view.clear();
                map_marker = null;
                tv_marcadorSelecionado.setText(R.string.str_marcadorNaoSelecionado);
                bt_remover.setVisibility(View.GONE);
                break;
        }
    }

    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void updateLocation() {
        map_view.setMyLocationEnabled(true);
        map_view.getUiSettings().setMyLocationButtonEnabled(true);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(2000)
                        .setFastestInterval(1000));
        LocationServices
                .getSettingsClient(getActivity())
                .checkLocationSettings(builder.build())
                .addOnSuccessListener(getActivity(), this)
                .addOnFailureListener(getActivity(), this);
    }

    @Override
    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
        LocationSettingsStates status = locationSettingsResponse.getLocationSettingsStates();
        if (status.isLocationPresent()) {
            FusedLocationProviderClient locationProvider = LocationServices.getFusedLocationProviderClient(getActivity());
                locationProvider.getLastLocation().addOnSuccessListener(getActivity(), location -> {
                    if (location != null) {
                        map_view.moveCamera(CameraUpdateFactory
                                .newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17f));
                    }
                });
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        int statusCode = ((ApiException) e).getStatusCode();
        if (statusCode == CommonStatusCodes.RESOLUTION_REQUIRED)
            try {
                ResolvableApiException resolvable = (ResolvableApiException) e;
                startIntentSenderForResult(resolvable.getResolution().getIntentSender(),
                        RC_LOCAL,
                        null,
                        0,
                        0,
                        0,
                        null);
            } catch (IntentSender.SendIntentException sendEx) {
            }
    }

    public void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(googleMap -> {
            map_view = googleMap;
            map_view.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            map_view.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(new LatLng(-26.484335, -51.991754), 17f));

            map_view.setOnMapClickListener(latLng -> {
                map_view.clear();
                map_marker = map_view.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title((getEndereco(latLng.latitude, latLng.longitude)))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            });

            map_view.setOnMyLocationClickListener(location -> {
                map_view.clear();
                map_marker = map_view.addMarker(new MarkerOptions()
                        .position(new LatLng(location.getLatitude(), location.getLongitude()))
                        .title((getEndereco(location.getLatitude(), location.getLongitude())))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            });

            map_view.setOnMyLocationButtonClickListener(() -> {
                FragmentSolicitacaoLocalizacaoPermissionsDispatcher.updateLocationWithPermissionCheck(this);
                return true;
            });
            FragmentSolicitacaoLocalizacaoPermissionsDispatcher.updateLocationWithPermissionCheck(this);
        });
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case RC_LOCAL:
                    FragmentSolicitacaoLocalizacaoPermissionsDispatcher.updateLocationWithPermissionCheck(this);
                    break;
            }
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
    public void onError(@NonNull VerificationError error) {
        Snackbar.make(getView(), error.getErrorMessage(), BaseTransientBottomBar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.ms_errorColor))
                .setTextColor(getResources().getColor(R.color.ms_white))
                .show();
    }

    @Override
    public void onSelected() {

    }

    @BindView(R.id.tv_marcadorSelecionado)
    TextView tv_marcadorSelecionado;

    @BindView(R.id.bt_remover)
    MaterialButton bt_remover;
}
