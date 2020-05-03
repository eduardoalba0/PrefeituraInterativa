package br.edu.ifpr.bsi.prefeiturainterativa.dao;

import android.app.Activity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.UUID;

import br.edu.ifpr.bsi.prefeiturainterativa.helpers.DatabaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Departamento;
import br.edu.ifpr.bsi.prefeiturainterativa.model.TipoSolicitacao;

public class TipoSolicitacaoDAO {


    private DatabaseHelper helper;
    private CollectionReference reference;

    public TipoSolicitacaoDAO(Activity context) {
        helper = new DatabaseHelper(context);
        reference = helper.getDataBase().collection("TiposSolicitacao");
    }

    public Task<Void> inserirAtualizar(TipoSolicitacao tipoSolicitacao) {
        if (tipoSolicitacao.get_ID() == null || tipoSolicitacao.get_ID().equals(""))
            tipoSolicitacao.set_ID(UUID.randomUUID().toString());
        return helper.inserirAtualizar(reference.document(tipoSolicitacao.get_ID()), tipoSolicitacao);
    }

    public Task<Void> remover(TipoSolicitacao tipoSolicitacao) {
        if (tipoSolicitacao.get_ID() != null && !tipoSolicitacao.get_ID().equals(""))
            return null;
        return helper.remover(reference.document(tipoSolicitacao.get_ID()));
    }

    public Task<DocumentSnapshot> get(TipoSolicitacao tipoSolicitacao) {
        if (tipoSolicitacao.get_ID() != null && !tipoSolicitacao.get_ID().equals(""))
            return null;
        return helper.get(reference.document(tipoSolicitacao.get_ID()));
    }

    public Task<QuerySnapshot> getAll() {
        return helper.getAll(reference);
    }

    public Task<QuerySnapshot> getAllPorDepartamento(Departamento departamento){
        return helper.getQuery(reference.whereEqualTo("departamento_ID", departamento.get_ID()));
    }
}
