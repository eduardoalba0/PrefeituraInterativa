package br.edu.ifpr.bsi.prefeiturainterativa.model;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.Objects;

public class TipoSolicitacao_Departamento implements Serializable {

    private String _ID;
    private String tipoSolicitacao_ID;
    private String departamento_ID;

    @Exclude
    private TipoSolicitacao tipoSolicitacao;
    @Exclude
    private Departamento departamento;

//---------------------- Encapsulamento ----------------------

    public String get_ID() {
        return _ID;
    }

    public void set_ID(String _ID) {
        this._ID = _ID;
    }

    public String getTipoSolicitacao_ID() {
        return tipoSolicitacao_ID;
    }

    public void setTipoSolicitacao_ID(String tipoSolicitacao_ID) {
        this.tipoSolicitacao_ID = tipoSolicitacao_ID;
    }

    public String getDepartamento_ID() {
        return departamento_ID;
    }

    public void setDepartamento_ID(String departamento_ID) {
        this.departamento_ID = departamento_ID;
    }

    public TipoSolicitacao getTipoSolicitacao() {
        return tipoSolicitacao;
    }

    public void setTipoSolicitacao(TipoSolicitacao tipoSolicitacao) {
        this.tipoSolicitacao = tipoSolicitacao;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipoSolicitacao_Departamento tipoSolicitacao_departamento = (TipoSolicitacao_Departamento) o;
        return _ID.equals(tipoSolicitacao_departamento._ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_ID);
    }

}
