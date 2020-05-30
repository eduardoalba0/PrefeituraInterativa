package br.edu.ifpr.bsi.prefeiturainterativa.dao;

import android.app.Activity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.UUID;

import br.edu.ifpr.bsi.prefeiturainterativa.helpers.DatabaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Funcionario;

public class FuncionarioDAO {

    private DatabaseHelper helper;
    private CollectionReference reference;

    public FuncionarioDAO(Activity context) {
        helper = new DatabaseHelper(context);
        reference = helper.getDataBase().collection("Funcionarios");
    }

    public Task<Void> inserirAtualizar(Funcionario funcionario) {
        if (funcionario.get_ID() == null || funcionario.get_ID().equals(""))
            funcionario.set_ID(UUID.randomUUID().toString());
        return helper.inserirAtualizar(reference.document(funcionario.get_ID()), funcionario);
    }

    public Task<DocumentSnapshot> get(Funcionario funcionario) {
        if (funcionario.get_ID()  == null || funcionario.get_ID().equals(""))
            return null;
        return helper.get(reference.document(funcionario.get_ID()));
    }

    public Task<DocumentSnapshot> get(String _ID) {
        if (_ID == null || _ID.equals(""))
            return null;
        return helper.get(reference.document(_ID));
    }

    public Task<QuerySnapshot> getAll() {
        return helper.getAll(reference);
    }
}
