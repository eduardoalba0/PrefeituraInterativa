package br.edu.ifpr.bsi.prefeiturainterativa.model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;

public class Departamento implements Serializable {
    public static final int STYLE_RED = 0, STYLE_YELLOW = 1, STYLE_GREEN = 2, STYLE_BLUE = 3;

    private String _ID;
    private String descricao;
    private String DepartamentoSuperior_ID;
    private boolean habilitado;
    private boolean staged;
    private List<String> categorias;

    @Exclude
    private Departamento departamentoSuperior;

    @Exclude
    private List<Categoria> localCategorias;

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

    public boolean isStaged() {
        return staged;
    }

    public void setStaged(boolean staged) {
        this.staged = staged;
    }

    public List<String> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<String> categorias) {
        this.categorias = categorias;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    @Exclude
    public Departamento getDepartamentoSuperior() {
        return departamentoSuperior;
    }

    public void setDepartamentoSuperior(Departamento departamentoSuperior) {
        this.departamentoSuperior = departamentoSuperior;
    }

    @Exclude
    public List<Categoria> getLocalCategorias() {
        return localCategorias;
    }

    public void setLocalCategorias(List<Categoria> localCategorias) {
        this.localCategorias = localCategorias;
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

    @NonNull
    @Override
    public String toString() {
        return this.descricao;
    }
}
