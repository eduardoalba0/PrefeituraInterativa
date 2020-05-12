package br.edu.ifpr.bsi.prefeiturainterativa.dao;

import android.app.Activity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.UUID;

import br.edu.ifpr.bsi.prefeiturainterativa.helpers.DatabaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Categorias_Solicitacao;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;

public class Categorias_SolicitacaoDAO {


    private DatabaseHelper helper;
    private CollectionReference reference;

    public Categorias_SolicitacaoDAO(Activity context) {
        helper = new DatabaseHelper(context);
        reference = helper.getDataBase().collection("Categorias_Solicitacao");
    }

    public Task<Void> inserirAtualizar(Categorias_Solicitacao categorias_Solicitacao) {
        if (categorias_Solicitacao.get_ID() == null || categorias_Solicitacao.get_ID().equals(""))
            categorias_Solicitacao.set_ID(UUID.randomUUID().toString());
        return helper.inserirAtualizar(reference.document(categorias_Solicitacao.get_ID()), categorias_Solicitacao);
    }

    public Task<Void> remover(Categorias_Solicitacao categorias_Solicitacao) {
        if (categorias_Solicitacao.get_ID() != null && !categorias_Solicitacao.get_ID().equals(""))
            return null;
        return helper.remover(reference.document(categorias_Solicitacao.get_ID()));
    }

    public Task<DocumentSnapshot> get(Categorias_Solicitacao categorias_Solicitacao) {
        if (categorias_Solicitacao.get_ID()  == null || categorias_Solicitacao.get_ID().equals(""))
            return null;
        return helper.get(reference.document(categorias_Solicitacao.get_ID()));
    }

    public Task<QuerySnapshot> getAll() {
        return helper.getAll(reference);
    }

    public Task<QuerySnapshot> getAllporSolicitacao(Solicitacao solicitacao) {
        return helper.getQuery(reference.whereEqualTo("solicitacao_ID", solicitacao.get_ID()));
    }
}
