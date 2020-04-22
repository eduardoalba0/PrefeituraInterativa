package br.edu.ifpr.bsi.prefeiturainterativa.model;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.Objects;

public class TipoUsuario_Permissao implements Serializable {

    private String _ID;
    private boolean concedida;
    private String tipoUsuario_ID;
    private String permissao_ID;

    @Exclude
    private TipoUsuario tipoUsuario;
    @Exclude
    private Permissao permissao;

//---------------------- Encapsulamento ----------------------

    public String get_ID() {
        return _ID;
    }

    public void set_ID(String _ID) {
        this._ID = _ID;
    }

    public boolean isConcedida() {
        return concedida;
    }

    public void setConcedida(boolean concedida) {
        this.concedida = concedida;
    }

    public String getTipoUsuario_ID() {
        return tipoUsuario_ID;
    }

    public void setTipoUsuario_ID(String tipoUsuario_ID) {
        this.tipoUsuario_ID = tipoUsuario_ID;
    }

    public String getPermissao_ID() {
        return permissao_ID;
    }

    public void setPermissao_ID(String permissao_ID) {
        this.permissao_ID = permissao_ID;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public Permissao getPermissao() {
        return permissao;
    }

    public void setPermissao(Permissao permissao) {
        this.permissao = permissao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipoUsuario_Permissao tipoUsuario_permissao = (TipoUsuario_Permissao) o;
        return _ID.equals(tipoUsuario_permissao._ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_ID);
    }

}
