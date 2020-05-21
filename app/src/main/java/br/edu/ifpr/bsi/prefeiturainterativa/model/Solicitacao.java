package br.edu.ifpr.bsi.prefeiturainterativa.model;

import android.net.Uri;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;

public class Solicitacao implements Serializable {

    private String _ID;
    private String descricao;
    private double latitude;
    private double longitude;
    private String endereco;
    private boolean concluida;
    private boolean anonima;
    private Double avaliacao_nota;
    private String avaliacao_comentario;
    private String usuario_ID;
    private String departamento_ID;
    private List<String> urlImagens;

    @ServerTimestamp
    private Date data;
    @Exclude
    private Usuario usuario;
    @Exclude
    private List<Uri> localUriImagens;
    @Exclude
    private List<Categoria> localCategorias;
    @Exclude
    Departamento departamento;

//---------------------- Encapsulamento ----------------------

    public String get_ID() {
        return _ID;
    }

    public void set_ID(String _ID) {
        this._ID = _ID;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }

    public boolean isAnonima() {
        return anonima;
    }

    public void setAnonima(boolean anonima) {
        this.anonima = anonima;
    }

    public List<String> getUrlImagens() {
        return urlImagens;
    }

    public void setUrlImagens(List<String> urlImagens) {
        this.urlImagens = urlImagens;
    }

    @Exclude
    public List<Uri> getLocalUriImagens() {
        return localUriImagens;
    }

    public void setLocalUriImagens(List<Uri> localUriImagens) {
        this.localUriImagens = localUriImagens;
    }

    public Double getAvaliacao_nota() {
        return avaliacao_nota;
    }

    public void setAvaliacao_nota(Double avaliacao_nota) {
        this.avaliacao_nota = avaliacao_nota;
    }

    public String getAvaliacao_comentario() {
        return avaliacao_comentario;
    }

    public void setAvaliacao_comentario(String avaliacao_comentario) {
        this.avaliacao_comentario = avaliacao_comentario;
    }

    public String getUsuario_ID() {
        return usuario_ID;
    }

    public void setUsuario_ID(String usuario_ID) {
        this.usuario_ID = usuario_ID;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDepartamento_ID() {
        return departamento_ID;
    }

    public void setDepartamento_ID(String departamento_ID) {
        this.departamento_ID = departamento_ID;
    }

    @Exclude
    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    @Exclude
    public List<Categoria> getLocalCategorias() {
        return localCategorias;
    }

    public void setLocalCategorias(List<Categoria> localCategorias) {
        this.localCategorias = localCategorias;
    }

    @Exclude
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Solicitacao solicitacao = (Solicitacao) o;
        return _ID.equals(solicitacao._ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_ID);
    }

    @NonNull
    @Override
    public String toString() {
        return this.descricao;
    }
}
