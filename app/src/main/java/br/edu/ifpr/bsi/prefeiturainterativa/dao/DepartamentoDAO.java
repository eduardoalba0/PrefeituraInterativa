package br.edu.ifpr.bsi.prefeiturainterativa.dao;

import android.content.Context;

import com.google.firebase.firestore.CollectionReference;

import java.util.UUID;

import br.edu.ifpr.bsi.prefeiturainterativa.model.Departamento;
import br.edu.ifpr.bsi.prefeiturainterativa.util.DatabaseHelper;

public class DepartamentoDAO {
    private DatabaseHelper helper;
    private CollectionReference reference;

    public DepartamentoDAO(Context context) {
        helper = new DatabaseHelper<Departamento>(context, Departamento.class);
        reference = helper.getDataBase().collection("Departamentos");
    }

    public Departamento inserirAtualizar(Departamento departamento) {
        if (departamento.get_ID() == null || departamento.get_ID().equals("")) {
            departamento.set_ID(UUID.randomUUID().toString());
            helper.inserir(reference.document(departamento.get_ID()), departamento);
        } else
            helper.alterar(reference.document(departamento.get_ID()), departamento);
        return departamento;
    }

    public void remover(Departamento departamento) {
        if (departamento.get_ID() != null && !departamento.get_ID().equals(""))
            helper.remover(reference.document(departamento.get_ID()));
    }

}
