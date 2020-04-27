package br.edu.ifpr.bsi.prefeiturainterativa.dao;

import android.app.Activity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.UUID;

import br.edu.ifpr.bsi.prefeiturainterativa.helpers.DatabaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Permissao;

public class PermissaoDAO {


    private DatabaseHelper helper;
    private CollectionReference reference;

    public PermissaoDAO(Activity context) {
        helper = new DatabaseHelper(context);
        reference = helper.getDataBase().collection("Permissaos");
    }

    public Task<Void> inserirAtualizar(Permissao permissao) {
        if (permissao.get_ID() == null || permissao.get_ID().equals(""))
            permissao.set_ID(UUID.randomUUID().toString());
        return helper.inserirAtualizar(reference.document(permissao.get_ID()), permissao);
    }

    public Task<Void> remover(Permissao permissao) {
        if (permissao.get_ID() != null && !permissao.get_ID().equals(""))
            return null;
        return helper.remover(reference.document(permissao.get_ID()));
    }

    public Task<DocumentSnapshot> get(Permissao permissao) {
        if (permissao.get_ID() != null && !permissao.get_ID().equals(""))
            return null;
        return helper.get(reference.document(permissao.get_ID()));
    }

    public Task<QuerySnapshot> getAll() {
        return helper.getAll(reference);
    }
}
