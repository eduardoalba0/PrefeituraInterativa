package br.edu.ifpr.bsi.prefeiturainterativa.dao;

import android.app.Activity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.UUID;

import br.edu.ifpr.bsi.prefeiturainterativa.helpers.DatabaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.FirebaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;

public class SolicitacaoDAO {


    private DatabaseHelper helper;
    private CollectionReference reference;
    private Activity context;

    public SolicitacaoDAO(Activity context) {
        this.helper = new DatabaseHelper(context);
        this.context = context;
        this.reference = helper.getDataBase().collection("Solicitacoes");
    }

    public Task<Void> inserirAtualizar(Solicitacao solicitacao) {
        if (solicitacao.get_ID() == null || solicitacao.get_ID().equals(""))
            solicitacao.set_ID(UUID.randomUUID().toString());
        return helper.inserirAtualizar(reference.document(solicitacao.get_ID()), solicitacao);
    }

    public Task<DocumentSnapshot> get(Solicitacao solicitacao) {
        if (solicitacao.get_ID()  == null || solicitacao.get_ID().equals(""))
            return null;
        return helper.get(reference.document(solicitacao.get_ID()));
    }

    public Task<QuerySnapshot> getAll() {
        return helper.getQuery(reference
                .whereEqualTo("usuario_ID", new FirebaseHelper(context).getUser().getUid()));
    }

    public Task<QuerySnapshot> getAllporStatus(boolean concluida) {
        return helper.getQuery(reference
                .whereEqualTo("concluida", concluida)
                .whereEqualTo("usuario_ID", new FirebaseHelper(context).getUser().getUid())
                .orderBy("data", Query.Direction.DESCENDING));
    }

}
