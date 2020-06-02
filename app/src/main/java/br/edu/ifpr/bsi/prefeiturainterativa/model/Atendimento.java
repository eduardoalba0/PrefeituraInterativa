package br.edu.ifpr.bsi.prefeiturainterativa.model;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Atendimento implements Serializable {

    private String _ID;
    private String resposta;
    private String acao;
    private String funcionario_ID;
    private String solicitacao_ID;

    @ServerTimestamp
    private Date data;
    @Exclude
    private Funcionario funcionario;

//---------------------- Encapsulamento ----------------------

    public String get_ID() {
        return _ID;
    }

    public void set_ID(String _ID) {
        this._ID = _ID;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public String getFuncionario_ID() {
        return funcionario_ID;
    }

    public void setFuncionario_ID(String funcionario_ID) {
        this.funcionario_ID = funcionario_ID;
    }

    public String getSolicitacao_ID() {
        return solicitacao_ID;
    }

    public void setSolicitacao_ID(String solicitacao_ID) {
        this.solicitacao_ID = solicitacao_ID;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
    @Exclude
    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Atendimento atendimento = (Atendimento) o;
        return _ID.equals(atendimento._ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_ID);
    }

    @Override
    public String toString() {
        return this.resposta;
    }
}
