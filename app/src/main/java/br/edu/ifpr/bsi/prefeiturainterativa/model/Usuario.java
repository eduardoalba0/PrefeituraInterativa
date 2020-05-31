package br.edu.ifpr.bsi.prefeiturainterativa.model;

import android.net.Uri;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.Objects;

import androidx.annotation.NonNull;

public class Usuario implements Serializable {

    private String _ID;
    private String nome;
    private String cpf;
    private String email;
    private String tipoUsuario_ID;
    private String token;
    private String uriFoto;
	private String motivoDesabilitacao;
	private boolean habilitado;

    @Exclude
    private Uri localUriFoto;
    @Exclude
    private String senha;
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

    @Exclude
    public Uri getLocalUriFoto() {
        return localUriFoto;
    }

    public void setLocalUriFoto(Uri urlFoto) {
        this.localUriFoto = urlFoto;
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

    @Exclude
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

    public String getUriFoto() {
        return uriFoto;
    }

    public void setUriFoto(String uriFoto) {
        this.uriFoto = uriFoto;
    }

    @Exclude
    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
    
	public String getMotivoDesabilitacao() {
		return motivoDesabilitacao;
	}

	public void setMotivoDesabilitacao(String motivoDesabilitacao) {
		this.motivoDesabilitacao = motivoDesabilitacao;
	}

	public boolean isHabilitado() {
		return habilitado;
	}

	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    @NonNull
    @Override
    public String toString() {
        return this._ID;
    }
}
