package br.edu.ifpr.bsi.prefeiturainterativa.util;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class DatabaseHelper<T> implements EventListener<QuerySnapshot> {

    private FirebaseFirestore dataBase;
    private Context context;

    private RecyclerView view;
    private RecyclerView.Adapter adapter;
    private SweetAlertDialog dialogo;

    private Class<T> classe;
    private List<T> lista;

    public DatabaseHelper(Context context, Class<T> classe) {
        if (FirebaseApp.getApps(context).isEmpty())
            FirebaseApp.initializeApp(context);
        this.context = context;
        this.classe = classe;
        this.lista = new ArrayList<T>();
        dataBase = FirebaseFirestore.getInstance();
        dialogo = new SweetAlertDialog(context);
    }
    //onEvent no DAO
    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
        if (e != null)
            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Erro!").setContentText("Falha ao consultar o banco de dados.")
                    .show();
        else if (view != null && adapter != null) {
            lista.clear();
            for (DocumentSnapshot childSnapshot : queryDocumentSnapshots) {
                T objeto = (T) childSnapshot.toObject(classe);
                lista.add(objeto);
            }
            view.setAdapter(adapter);
        }
    }

    public <T> void inserir(DocumentReference reference, T objeto) {
        dialogo = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        dialogo.setTitleText("Gravando...");
        dialogo.show();
        reference.set(objeto).addOnCompleteListener(task -> {
            dialogo.dismiss();
            if (task.isSuccessful())
                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Sucesso!").setContentText("Dados gravados com sucesso.")
                        .show();
            else
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Erro!").setContentText("Falha na gravação, consulte o suporte do sistema.")
                        .show();
        });
    }

    public <T> void alterar(DocumentReference reference, T objeto) {
        dialogo = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        dialogo.setTitleText("Alterando...");
        dialogo.show();
        reference.set(objeto).addOnCompleteListener(task -> {
            dialogo.dismiss();
            if (task.isSuccessful()) {
                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Sucesso!").setContentText("Informação alterada com sucesso.")
                        .show();
            } else {
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Erro!").setContentText("Falha na alteração, consulte o suporte do sistema.")
                        .show();
            }
        });
    }

    public void remover(DocumentReference reference) {
        dialogo = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        dialogo.setTitleText("Removendo...");
        dialogo.show();
        reference.delete().addOnCompleteListener(task -> {
            dialogo.dismiss();
            if (task.isSuccessful()) {
                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Sucesso!").setContentText("Remoção bem sucedida.")
                        .show();
            } else {
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Erro!").setContentText("Falha na remoção, consulte o suporte do sistema.")
                        .show();
            }
        });
    }

    public <T> T getObjeto(DocumentReference reference, Class<T> classe) {
        dialogo = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        dialogo.setTitleText("Buscando...");
        dialogo.show();
        final List<T> objetos = new ArrayList<>();
        reference.addSnapshotListener((documentSnapshot, e) -> {
            dialogo.dismiss();
            if (e != null)
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Erro!").setContentText("Falha ao consultar o banco de dados.")
                        .show();
            else
                objetos.add(documentSnapshot.toObject(classe));
        });
        return objetos.get(0);
    }

    public FirebaseFirestore getDataBase() {
        if (dataBase == null)
            dataBase = FirebaseFirestore.getInstance();
        return dataBase;
    }
}
