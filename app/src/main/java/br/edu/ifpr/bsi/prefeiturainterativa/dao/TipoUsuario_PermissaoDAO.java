package br.edu.ifpr.bsi.prefeiturainterativa.dao;

import android.app.Activity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.UUID;

import br.edu.ifpr.bsi.prefeiturainterativa.helpers.DatabaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.TipoUsuario_Permissao;

public class TipoUsuario_PermissaoDAO {


    private DatabaseHelper helper;
    private CollectionReference reference;

    public TipoUsuario_PermissaoDAO(Activity context) {
        helper = new DatabaseHelper(context);
        reference = helper.getDataBase().collection("TipoUsuarios_Permissoes");
    }

    public Task<Void> inserirAtualizar(TipoUsuario_Permissao tipoUsuario_Permissao) {
        if (tipoUsuario_Permissao.get_ID() == null || tipoUsuario_Permissao.get_ID().equals(""))
            tipoUsuario_Permissao.set_ID(UUID.randomUUID().toString());
        return helper.inserirAtualizar(reference.document(tipoUsuario_Permissao.get_ID()), tipoUsuario_Permissao);
    }

    public Task<Void> remover(TipoUsuario_Permissao tipoUsuario_Permissao) {
        if (tipoUsuario_Permissao.get_ID() != null && !tipoUsuario_Permissao.get_ID().equals(""))
            return null;
        return helper.remover(reference.document(tipoUsuario_Permissao.get_ID()));
    }

    public Task<DocumentSnapshot> get(TipoUsuario_Permissao tipoUsuario_Permissao) {
        if (tipoUsuario_Permissao.get_ID() != null && !tipoUsuario_Permissao.get_ID().equals(""))
            return null;
        return helper.get(reference.document(tipoUsuario_Permissao.get_ID()));
    }

    public Task<QuerySnapshot> getAll() {
        return helper.getAll(reference);
    }
}
