package br.edu.ifpr.bsi.prefeiturainterativa.dao;

import android.app.Activity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.UUID;

import br.edu.ifpr.bsi.prefeiturainterativa.helpers.DatabaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Categoria;

public class CategoriaDAO {

    private DatabaseHelper helper;
    private CollectionReference reference;

    public CategoriaDAO(Activity context) {
        helper = new DatabaseHelper(context);
        reference = helper.getDataBase().collection("Categorias");
    }

    public Task<Void> inserirAtualizar(Categoria categoria) {
        if (categoria.get_ID() == null || categoria.get_ID().equals(""))
            categoria.set_ID(UUID.randomUUID().toString());
        return helper.inserirAtualizar(reference.document(categoria.get_ID()), categoria);
    }

    public Task<DocumentSnapshot> get(Categoria categoria) {
        if (categoria.get_ID() == null || categoria.get_ID().equals(""))
            return null;
        return helper.get(reference.document(categoria.get_ID()));
    }

    public Task<QuerySnapshot> getAll(List<String> _IDs) {
        return helper.getQuery(reference
                .orderBy("descricao")
                .whereIn("_ID", _IDs));
    }

    public Task<QuerySnapshot> getAllHabilitadas(List<String> _IDs) {
        return helper.getQuery(reference
                .orderBy("descricao")
                .whereIn("_ID", _IDs)
                .whereEqualTo("habilitada", true));
    }

}
