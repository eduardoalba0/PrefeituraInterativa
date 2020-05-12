package br.edu.ifpr.bsi.prefeiturainterativa.dao;

import android.app.Activity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.UUID;

import br.edu.ifpr.bsi.prefeiturainterativa.helpers.DatabaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Atendimento;

public class AtendimentoDAO {


    private DatabaseHelper helper;
    private CollectionReference reference;

    public AtendimentoDAO(Activity context) {
        helper = new DatabaseHelper(context);
        reference = helper.getDataBase().collection("Atendimentos");
    }

    public Task<Void> inserirAtualizar(Atendimento atendimento) {
        if (atendimento.get_ID() == null || atendimento.get_ID().equals(""))
            atendimento.set_ID(UUID.randomUUID().toString());
        return helper.inserirAtualizar(reference.document(atendimento.get_ID()), atendimento);
    }

    public Task<Void> remover(Atendimento atendimento) {
        if (atendimento.get_ID() != null && !atendimento.get_ID().equals(""))
            return null;
        return helper.remover(reference.document(atendimento.get_ID()));
    }

    public Task<DocumentSnapshot> get(Atendimento atendimento) {
        if (atendimento.get_ID() == null || atendimento.get_ID().equals(""))
            return null;
        return helper.get(reference.document(atendimento.get_ID()));
    }

    public Task<QuerySnapshot> getAll() {
        return helper.getAll(reference);
    }
}
