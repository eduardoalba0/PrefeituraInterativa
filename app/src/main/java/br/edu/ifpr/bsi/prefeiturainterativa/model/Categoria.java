package br.edu.ifpr.bsi.prefeiturainterativa.model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.Objects;

public class Categoria implements Serializable {

    private String _ID;
    private String descricao;
    private String Departamento_ID;
    private int prazoResposta;
    private boolean fotoObrigatoria;
    private boolean localObrigatorio;
    private boolean descricaoObrigatoria;

    @Exclude
    private Departamento departamento;
    @Exclude
    private boolean selecionada;

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

    public int getPrazoResposta() {
        return prazoResposta;
    }

    public void setPrazoResposta(int prazoResposta) {
        this.prazoResposta = prazoResposta;
    }

    public String getDepartamento_ID() {
        return Departamento_ID;
    }

    public void setDepartamento_ID(String departamento_ID) {
        Departamento_ID = departamento_ID;
    }

    @Exclude
    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    @Exclude
    public boolean isSelecionada() {
        return selecionada;
    }

    public void setSelecionada(boolean selecionada) {
        this.selecionada = selecionada;
    }

    public boolean isFotoObrigatoria() {
        return fotoObrigatoria;
    }

    public void setFotoObrigatoria(boolean fotoObrigatoria) {
        this.fotoObrigatoria = fotoObrigatoria;
    }

    public boolean isLocalObrigatorio() {
        return localObrigatorio;
    }

    public void setLocalObrigatorio(boolean localObrigatorio) {
        this.localObrigatorio = localObrigatorio;
    }

    public boolean isDescricaoObrigatoria() {
        return descricaoObrigatoria;
    }

    public void setDescricaoObrigatoria(boolean descricaoObrigatoria) {
        this.descricaoObrigatoria = descricaoObrigatoria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return _ID.equals(categoria._ID);
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