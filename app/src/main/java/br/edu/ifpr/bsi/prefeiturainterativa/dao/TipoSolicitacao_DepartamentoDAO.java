package br.edu.ifpr.bsi.prefeiturainterativa.dao;

import android.app.Activity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.UUID;

import br.edu.ifpr.bsi.prefeiturainterativa.helpers.DatabaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.TipoSolicitacao_Departamento;

public class TipoSolicitacao_DepartamentoDAO {


    private DatabaseHelper helper;
    private CollectionReference reference;

    public TipoSolicitacao_DepartamentoDAO(Activity context) {
        helper = new DatabaseHelper(context);
        reference = helper.getDataBase().collection("TiposSolicitacao_Departamentos");
    }

    public Task<Void> inserirAtualizar(TipoSolicitacao_Departamento tipoSolicitacao_Departamento) {
        if (tipoSolicitacao_Departamento.get_ID() == null || tipoSolicitacao_Departamento.get_ID().equals(""))
            tipoSolicitacao_Departamento.set_ID(UUID.randomUUID().toString());
        return helper.inserirAtualizar(reference.document(tipoSolicitacao_Departamento.get_ID()), tipoSolicitacao_Departamento);
    }

    public Task<Void> remover(TipoSolicitacao_Departamento tipoSolicitacao_Departamento) {
        if (tipoSolicitacao_Departamento.get_ID() != null && !tipoSolicitacao_Departamento.get_ID().equals(""))
            return null;
        return helper.remover(reference.document(tipoSolicitacao_Departamento.get_ID()));
    }

    public Task<DocumentSnapshot> get(TipoSolicitacao_Departamento tipoSolicitacao_Departamento) {
        if (tipoSolicitacao_Departamento.get_ID() != null && !tipoSolicitacao_Departamento.get_ID().equals(""))
            return null;
        return helper.get(reference.document(tipoSolicitacao_Departamento.get_ID()));
    }

    public Task<QuerySnapshot> getAll() {
        return helper.getAll(reference);
    }
}
