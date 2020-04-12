package br.edu.ifpr.bsi.prefeiturainterativa.model;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.Objects;

public class Usuario implements Serializable {

    @DocumentId
    private String _ID;
    private String nome;
    private String cpf;
    private String email;
    private String senha;
    private String tipoUsuario_ID;

    @Exclude
    private TipoUsuario tipoUsuario;

//---------------------- Encapsulamento ----------------------

    public String get_ID() {
        return _ID;
    }

    public void set_ID(String _ID) {
        this._ID = _ID;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTipoUsuario_ID() {
        return tipoUsuario_ID;
    }

    public void setTipoUsuario_ID(String tipoUsuario_ID) {
        this.tipoUsuario_ID = tipoUsuario_ID;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return _ID.equals(usuario._ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_ID);
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
