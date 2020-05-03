package br.edu.ifpr.bsi.prefeiturainterativa.model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Departamento implements Serializable {

    private String _ID;
    private String descricao;
    private String DepartamentoSuperior_ID;

    @Exclude
    private Departamento departamentoSuperior;

    @Exclude
    private List<TipoSolicitacao> tiposSolicitacao;

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

    public String getDepartamentoSuperior_ID() {
        return DepartamentoSuperior_ID;
    }

    public void setDepartamentoSuperior_ID(String departamentoSuperior_ID) {
        DepartamentoSuperior_ID = departamentoSuperior_ID;
    }

    @Exclude
    public Departamento getDepartamentoSuperior() {
        return departamentoSuperior;
    }

    public void setDepartamentoSuperior(Departamento departamentoSuperior) {
        this.departamentoSuperior = departamentoSuperior;
    }

    @Exclude
    public List<TipoSolicitacao> getTiposSolicitacao() {
        return tiposSolicitacao;
    }

    public void setTiposSolicitacao(List<TipoSolicitacao> tiposSolicitacao) {
        this.tiposSolicitacao = tiposSolicitacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Departamento departamento = (Departamento) o;
        return _ID.equals(departamento._ID);
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
