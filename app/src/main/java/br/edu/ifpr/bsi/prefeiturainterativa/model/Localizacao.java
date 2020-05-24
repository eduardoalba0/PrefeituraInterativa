package br.edu.ifpr.bsi.prefeiturainterativa.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Localizacao implements Serializable {

    private double latitude;
    private double longitude;
    private String endereco;

    public Localizacao() {
    }

    public Localizacao(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Localizacao(LatLng latLng) {
        this.latitude = latLng.latitude;
        this.longitude = latLng.longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
