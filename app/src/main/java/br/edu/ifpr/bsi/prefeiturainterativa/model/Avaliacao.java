package br.edu.ifpr.bsi.prefeiturainterativa.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;

public class Avaliacao implements Serializable {
    private float nota;
    private String comentario;
    private boolean solucionada;

    @ServerTimestamp
    private Date data;

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public boolean isSolucionada() {
        return solucionada;
    }

    public void setSolucionada(boolean solucionada) {
        this.solucionada = solucionada;
    }

    @ServerTimestamp
    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
