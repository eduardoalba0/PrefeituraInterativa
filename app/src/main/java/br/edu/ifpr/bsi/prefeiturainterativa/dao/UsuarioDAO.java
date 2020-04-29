package br.edu.ifpr.bsi.prefeiturainterativa.dao;

import android.app.Activity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.UUID;

import br.edu.ifpr.bsi.prefeiturainterativa.helpers.DatabaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Usuario;

public class UsuarioDAO {


    private DatabaseHelper helper;
    private CollectionReference reference;

    public UsuarioDAO(Activity context) {
        helper = new DatabaseHelper(context);
        reference = helper.getDataBase().collection("Usuarios");
    }

    public Task<Void> inserirAtualizar(Usuario usuario) {
        if (usuario.get_ID() == null || usuario.get_ID().equals(""))
            usuario.set_ID(UUID.randomUUID().toString());
        return helper.inserirAtualizar(reference.document(usuario.get_ID()), usuario);
    }

    public Task<Void> remover(Usuario usuario) {
        if (usuario.get_ID() == null && usuario.get_ID().equals(""))
            return null;
        return helper.remover(reference.document(usuario.get_ID()));
    }

    public Task<DocumentSnapshot> get(Usuario usuario) {
        if (usuario.get_ID() == null && usuario.get_ID().equals(""))
            return null;
        return helper.get(reference.document(usuario.get_ID()));
    }

    public Task<QuerySnapshot> getAll() {
        return helper.getAll(reference);
    }

    public Task<QuerySnapshot> buscaCpf(Usuario usuario) {
        return helper.getQuery(reference.whereEqualTo("cpf", usuario.getCpf()));
    }
}
