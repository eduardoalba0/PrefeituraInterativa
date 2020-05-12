package br.edu.ifpr.bsi.prefeiturainterativa.dao;

import android.app.Activity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.UUID;

import br.edu.ifpr.bsi.prefeiturainterativa.helpers.DatabaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.TipoUsuario;

public class TipoUsuarioDAO {


    private DatabaseHelper helper;
    private CollectionReference reference;

    public TipoUsuarioDAO(Activity context) {
        helper = new DatabaseHelper(context);
        reference = helper.getDataBase().collection("TiposUsuario");
    }

    public Task<Void> inserirAtualizar(TipoUsuario tipoUsuario) {
        if (tipoUsuario.get_ID() == null || tipoUsuario.get_ID().equals(""))
            tipoUsuario.set_ID(UUID.randomUUID().toString());
        return helper.inserirAtualizar(reference.document(tipoUsuario.get_ID()), tipoUsuario);
    }

    public Task<Void> remover(TipoUsuario tipoUsuario) {
        if (tipoUsuario.get_ID() != null && !tipoUsuario.get_ID().equals(""))
            return null;
        return helper.remover(reference.document(tipoUsuario.get_ID()));
    }

    public Task<DocumentSnapshot> get(TipoUsuario tipoUsuario) {
        if (tipoUsuario.get_ID()  == null || tipoUsuario.get_ID().equals(""))
            return null;
        return helper.get(reference.document(tipoUsuario.get_ID()));
    }

    public Task<QuerySnapshot> getAll() {
        return helper.getAll(reference);
    }
}
