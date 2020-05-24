package br.edu.ifpr.bsi.prefeiturainterativa.model;

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
    private boolean concluida;
    private boolean avaliada;
    private boolean anonima;
    private Avaliacao avaliacao;
    private String usuario_ID;
    private String departamento_ID;
    private List<String> urlImagens;
    private Localizacao localizacao;

    @ServerTimestamp
    private Date data;
    @Exclude
    private Usuario usuario;
    @Exclude
    private List<Categoria> localCategorias;
    @Exclude
    private List<String> localCaminhoImagens;
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

    public Localizacao getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
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

    public Avaliacao getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Avaliacao avaliacao) {
        this.avaliacao = avaliacao;
    }

    public boolean isAvaliada() {
        return avaliada;
    }

    public void setAvaliada(boolean avaliada) {
        this.avaliada = avaliada;
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
    public List<String> getLocalCaminhoImagens() {
        return localCaminhoImagens;
    }

    public void setLocalCaminhoImagens(List<String> localCaminhoImagens) {
        this.localCaminhoImagens = localCaminhoImagens;
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
