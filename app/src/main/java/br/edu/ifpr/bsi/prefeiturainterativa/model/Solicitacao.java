package br.edu.ifpr.bsi.prefeiturainterativa.model;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Solicitacao implements Serializable {

    private String _ID;
    private String descricao;
    private double latitude;
    private double longitude;
    private boolean concluida;
    private boolean anonima;
    private String imagem_url;
    private String anexos_url;
    private Double avaliacao_nota;
    private String avaliacao_comentario;
    private String tipoSolicitacao_ID;
    private String usuario_ID;

    @ServerTimestamp
    private Date data;
    @Exclude
    private TipoSolicitacao tipoSolicitacao;
    @Exclude
    private Usuario usuario;

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

    public String getImagem_url() {
        return imagem_url;
    }

    public void setImagem_url(String imagem_url) {
        this.imagem_url = imagem_url;
    }

    public String getAnexos_url() {
        return anexos_url;
    }

    public void setAnexos_url(String anexos_url) {
        this.anexos_url = anexos_url;
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

    public String getTipoSolicitacao_ID() {
        return tipoSolicitacao_ID;
    }

    public void setTipoSolicitacao_ID(String tipoSolicitacao_ID) {
        this.tipoSolicitacao_ID = tipoSolicitacao_ID;
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

    public TipoSolicitacao getTipoSolicitacao() {
        return tipoSolicitacao;
    }

    public void setTipoSolicitacao(TipoSolicitacao tipoSolicitacao) {
        this.tipoSolicitacao = tipoSolicitacao;
    }

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

    @Override
    public String toString() {
        return this.descricao;
    }
}
