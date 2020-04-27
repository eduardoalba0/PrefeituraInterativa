package br.edu.ifpr.bsi.prefeiturainterativa.dao;

import android.app.Activity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.UUID;

import br.edu.ifpr.bsi.prefeiturainterativa.helpers.DatabaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;

public class SolicitacaoDAO {


    private DatabaseHelper helper;
    private CollectionReference reference;

    public SolicitacaoDAO(Activity context) {
        helper = new DatabaseHelper(context);
        reference = helper.getDataBase().collection("Solicitacoes");
    }

    public Task<Void> inserirAtualizar(Solicitacao solicitacao) {
        if (solicitacao.get_ID() == null || solicitacao.get_ID().equals(""))
            solicitacao.set_ID(UUID.randomUUID().toString());
        return helper.inserirAtualizar(reference.document(solicitacao.get_ID()), solicitacao);
    }

    public Task<Void> remover(Solicitacao solicitacao) {
        if (solicitacao.get_ID() != null && !solicitacao.get_ID().equals(""))
            return null;
        return helper.remover(reference.document(solicitacao.get_ID()));
    }

    public Task<DocumentSnapshot> get(Solicitacao solicitacao) {
        if (solicitacao.get_ID() != null && !solicitacao.get_ID().equals(""))
            return null;
        return helper.get(reference.document(solicitacao.get_ID()));
    }

    public Task<QuerySnapshot> getAll() {
        return helper.getAll(reference);
    }
}
