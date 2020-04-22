package br.edu.ifpr.bsi.prefeiturainterativa.dao;

import android.content.Context;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.edu.ifpr.bsi.prefeiturainterativa.model.Atendimento;
import br.edu.ifpr.bsi.prefeiturainterativa.util.FirebaseHelper;

public class AtendimentoDAO {

    private FirebaseHelper helper;
    private CollectionReference reference;

    public AtendimentoDAO(Context context) {
        helper = new FirebaseHelper(context);
        reference = helper.getDataBase().collection("Atendimentos");
    }

    public Atendimento inserirAtualizar(Atendimento atendimento) {
        if (atendimento.get_ID() == null || atendimento.get_ID().equals("")) {
            atendimento.set_ID(UUID.randomUUID().toString());
            helper.inserir(reference.document(atendimento.get_ID()), atendimento);
        } else
            helper.alterar(reference.document(atendimento.get_ID()), atendimento);
        return atendimento;
    }

    public void remover(Atendimento atendimento) {
        if (atendimento.get_ID() != null && !atendimento.get_ID().equals(""))
            helper.remover(reference.document(atendimento.get_ID()));
    }

    public Atendimento getAtendimento(Atendimento atendimento) {
        return helper.getObjeto(reference.document(atendimento.get_ID()), Atendimento.class);
    }

    public List<Atendimento> listar() {
        return helper.listar(reference, Atendimento.class);
    }
}
