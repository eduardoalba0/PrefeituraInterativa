package br.edu.ifpr.bsi.prefeiturainterativa.dao;

import android.app.Activity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.UUID;

import br.edu.ifpr.bsi.prefeiturainterativa.helpers.DatabaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Categoria;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Departamento;

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

    public Task<Void> remover(Categoria categoria) {
        if (categoria.get_ID() != null && !categoria.get_ID().equals(""))
            return null;
        return helper.remover(reference.document(categoria.get_ID()));
    }

    public Task<DocumentSnapshot> get(Categoria categoria) {
        if (categoria.get_ID() == null || categoria.get_ID().equals(""))
            return null;
        return helper.get(reference.document(categoria.get_ID()));
    }

    public Task<QuerySnapshot> getAllPorDepartamento(Departamento departamento){
        return helper.getQuery(reference.whereEqualTo("departamento_ID", departamento.get_ID()).orderBy("descricao"));
    }
}
