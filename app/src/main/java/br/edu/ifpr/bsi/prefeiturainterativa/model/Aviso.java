package br.edu.ifpr.bsi.prefeiturainterativa.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Aviso implements Serializable {

    public static final String CATEGORIA_PADRAO = "Avisos Gerais",
            CATEGORIA_AVALIACAO = "Avaliação de Solicitações",
            CATEGORIA_TRAMITACAO = "Tramitação de Solicitações";

    private String titulo;
    private String corpo;
    private String solicitacao_ID;
    private String categoria;
    private Date data;

//---------------------- Encapsulamento ----------------------


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCorpo() {
        return corpo;
    }

    public void setCorpo(String corpo) {
        this.corpo = corpo;
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aviso aviso = (Aviso) o;
        return data.equals(aviso.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }
}
