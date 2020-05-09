package br.edu.ifpr.bsi.prefeiturainterativa.model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.Objects;

public class Categorias_Solicitacao implements Serializable {

    private String _ID;
    private String categoria_ID;
    private String solicitacao_ID;

    @Exclude
    private Categoria categoria;
    @Exclude
    private Solicitacao solicitacao;

//---------------------- Encapsulamento ----------------------


    public String get_ID() {
        return _ID;
    }

    public void set_ID(String _ID) {
        this._ID = _ID;
    }

    public String getCategoria_ID() {
        return categoria_ID;
    }

    public void setCategoria_ID(String categoria_ID) {
        this.categoria_ID = categoria_ID;
    }

    public String getSolicitacao_ID() {
        return solicitacao_ID;
    }

    public void setSolicitacao_ID(String solicitacao_ID) {
        this.solicitacao_ID = solicitacao_ID;
    }

    @Exclude
    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Exclude
    public Solicitacao getSolicitacao() {
        return solicitacao;
    }

    public void setSolicitacao(Solicitacao solicitacao) {
        this.solicitacao = solicitacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categorias_Solicitacao categoria = (Categorias_Solicitacao) o;
        return _ID.equals(categoria._ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_ID);
    }

    @Override
    public String toString() {
        return this._ID;
    }
}
