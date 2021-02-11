package br.edu.ifpr.bsi.prefeiturainterativa.dao;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.UUID;

import br.edu.ifpr.bsi.prefeiturainterativa.helpers.DatabaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Departamento;

public class DepartamentoDAO {


    private DatabaseHelper helper;
    private CollectionReference reference;

    public DepartamentoDAO(Activity context) {
        helper = new DatabaseHelper(context);
        reference = helper.getDataBase().collection("Departamentos");
    }

    public Task<Void> inserirAtualizar(Departamento departamento) {
        if (departamento.get_ID() == null || departamento.get_ID().equals(""))
            departamento.set_ID(UUID.randomUUID().toString());
        return helper.inserirAtualizar(reference.document(departamento.get_ID()), departamento);
    }

    public Task<DocumentSnapshot> get(Departamento departamento) {
        if (departamento.get_ID()  == null || departamento.get_ID().equals(""))
            return null;
        return helper.get(reference.document(departamento.get_ID()));
    }
    public Task<DocumentSnapshot> get(String _ID) {
        if (_ID  == null || _ID.equals(""))
            return null;
        return helper.get(reference.document(_ID));
    }
    public Task<QuerySnapshot> getAll() {
        return helper.getQuery(reference.orderBy("descricao"));
    }
}
