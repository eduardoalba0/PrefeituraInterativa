package br.edu.ifpr.bsi.prefeiturainterativa.model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Funcionario implements Serializable {
    private String _ID;
    private String matricula;
    private String cargo;
    private String departamento_ID;
    private String usuario_ID;

    @Exclude
    private Departamento departamento;

    @Exclude
    private Usuario usuario;

//---------------------- Encapsulamento ----------------------


    public String get_ID() {
        return _ID;
    }

    public void set_ID(String _ID) {
        this._ID = _ID;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
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

    public String getUsuario_ID() {
        return usuario_ID;
    }

    public void setUsuario_ID(String usuario_ID) {
        this.usuario_ID = usuario_ID;
    }

    @Exclude
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
