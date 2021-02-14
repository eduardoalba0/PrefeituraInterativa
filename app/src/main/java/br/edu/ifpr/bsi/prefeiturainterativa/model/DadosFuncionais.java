package br.edu.ifpr.bsi.prefeiturainterativa.model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DadosFuncionais implements Serializable {

    private String matricula;
    private String cargo;
    private String departamento_ID;

    @Exclude
    private Departamento departamento;

//---------------------- Encapsulamento ----------------------

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

    public void setDepartamento(Departamento localDepartamento) {
        if (localDepartamento != null && localDepartamento.get_ID() != null)
            this.setDepartamento_ID(localDepartamento.get_ID());
        this.departamento = localDepartamento;
    }

}
