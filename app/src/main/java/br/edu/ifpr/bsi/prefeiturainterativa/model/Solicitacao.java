package br.edu.ifpr.bsi.prefeiturainterativa.model;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;

public class Solicitacao implements Serializable {

    public static final int STYLE_NORMAL = 0,
            STYLE_PENDENTE = 1,
            STYLE_SEM_AVALIACAO = 2,
            STYLE_PENDENTE_SEM_AVALIACAO = 3;

    private String _ID;
    private String descricao;
    private String usuario_ID;
    private String departamento_ID;
    private String funcionarioConclusao_ID;
    private boolean concluida;
    private boolean anonima;
    private Localizacao localizacao;
    private Avaliacao avaliacao;
    private List<String> urlImagens;
    private List<String> categorias;
    private List<String> atendimentos;
    private Date dataConclusao;

    @ServerTimestamp
    private Date dataAbertura;
    @Exclude
    private Departamento departamento;
    @Exclude
    private Usuario usuario;
    @Exclude
	private Usuario localFuncionarioConclusao;
    @Exclude
    private List<Categoria> localCategorias;
    @Exclude
    private List<String> localCaminhoImagens;
    @Exclude
    private List<Atendimento> localAtendimentos;

//---------------------- Encapsulamento ----------------------


    public Solicitacao() {
        urlImagens = new ArrayList<>();
        categorias = new ArrayList<>();
        atendimentos = new ArrayList<>();
    }

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

    public Localizacao getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }

    public boolean isAnonima() {
        return anonima;
    }

    public void setAnonima(boolean anonima) {
        this.anonima = anonima;
    }

    public List<String> getUrlImagens() {
        return urlImagens;
    }

    public void setUrlImagens(List<String> urlImagens) {
        this.urlImagens = urlImagens;
    }

    public Avaliacao getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Avaliacao avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getUsuario_ID() {
        return usuario_ID;
    }

    public void setUsuario_ID(String usuario_ID) {
        this.usuario_ID = usuario_ID;
    }

	public String getFuncionarioConclusao_ID() {
		return funcionarioConclusao_ID;
	}

	public void setFuncionarioConclusao_ID(String funcionarioConclusao_ID) {
		this.funcionarioConclusao_ID = funcionarioConclusao_ID;
	}

    public Date getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(Date dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public Date getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(Date dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public String getDepartamento_ID() {
        return departamento_ID;
    }

    public void setDepartamento_ID(String departamento_ID) {
        this.departamento_ID = departamento_ID;
    }

    public List<String> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<String> categorias) {
        this.categorias = categorias;
    }

    public List<String> getAtendimentos() {
        return atendimentos;
    }

    public void setAtendimentos(List<String> atendimentos) {
        this.atendimentos = atendimentos;
    }

    @Exclude
    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    @Exclude
    public List<Categoria> getLocalCategorias() {
        return localCategorias;
    }

    public void setLocalCategorias(List<Categoria> localCategorias) {
        this.localCategorias = localCategorias;
    }

    @Exclude
    public List<String> getLocalCaminhoImagens() {
        return localCaminhoImagens;
    }

    public void setLocalCaminhoImagens(List<String> localCaminhoImagens) {
        this.localCaminhoImagens = localCaminhoImagens;
    }

    @Exclude
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Exclude
	public Usuario getLocalFuncionarioConclusao() {
		return localFuncionarioConclusao;
	}

	public void setLocalFuncionarioConclusao(Usuario funcionarioConclusao) {
		this.localFuncionarioConclusao = funcionarioConclusao;
	}
    
    @Exclude
    public List<Atendimento> getLocalAtendimentos() {
        return localAtendimentos;
    }

    public void setLocalAtendimentos(List<Atendimento> localAtendimentos) {
        this.localAtendimentos = localAtendimentos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Solicitacao solicitacao = (Solicitacao) o;
        return _ID.equals(solicitacao._ID);
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
