package br.edu.ifpr.bsi.prefeiturainterativa.helpers;

import android.app.Activity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DatabaseHelper {

    private FirebaseFirestore dataBase;
    private Activity context;

    public DatabaseHelper(Activity context) {
        if (FirebaseApp.getApps(context).isEmpty())
            FirebaseApp.initializeApp(context);
        this.context = context;
        dataBase = FirebaseFirestore.getInstance();
    }

    public <T> Task<Void> inserirAtualizar(DocumentReference reference, T objeto) {
        return reference.set(objeto).addOnCompleteListener(context, task -> {
            if (!task.isSuccessful()) {
                if (task.getException().toString().contains("FirebaseFirestoreException: PERMISSION_DENIED"))
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Erro!").setContentText("Você não possui permissão para gravar nesta coleção.")
                            .show();
                else new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Erro!").setContentText("Falha na gravação, consulte o suporte do sistema.")
                        .show();
            }
        });
    }

    public Task<Void> remover(DocumentReference reference) {
        return reference.delete().addOnCompleteListener(context, task -> {
            if (!task.isSuccessful()) {
                if (task.getException().toString().contains("FirebaseFirestoreException: PERMISSION_DENIED"))
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Erro!").setContentText("Você não possui permissão para remover dados nesta coleção.")
                            .show();
                else new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Erro!").setContentText("Falha na gravação, consulte o suporte do sistema.")
                        .show();

            }
        });
    }

    public Task<DocumentSnapshot> get(DocumentReference reference) {
        return reference.get().addOnCompleteListener(context, task -> {
            if (!task.isSuccessful())
                if (task.getException().toString().contains("FirebaseFirestoreException: PERMISSION_DENIED"))
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Erro!").setContentText("Você não possui permissão para acessar esta coleção.")
                            .show();
                else new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Erro!").setContentText("Falha na consulta ao banco de dados, consulte o suporte do sistema.")
                        .show();
        });
    }

    public Task<QuerySnapshot> getAll(CollectionReference reference) {
        return reference.get().addOnCompleteListener(context, task -> {
            if (!task.isSuccessful())
                if (task.getException().toString().contains("FirebaseFirestoreException: PERMISSION_DENIED"))
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Erro!").setContentText("Você não possui permissão para acessar esta coleção.")
                            .show();
                else new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Erro!").setContentText("Falha na consulta ao banco de dados, consulte o suporte do sistema.")
                        .show();
        });
    }

    public Task<QuerySnapshot> getQuery(Query query) {
        return query.get().addOnCompleteListener(context, task -> {
            if (!task.isSuccessful())
                if (task.getException().toString().contains("FirebaseFirestoreException: PERMISSION_DENIED"))
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Erro!").setContentText("Você não possui permissão para acessar esta coleção.")
                            .show();
                else
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Erro!").setContentText("Falha na consulta ao banco de dados, consulte o suporte do sistema.")
                            .show();

        });
    }

    public FirebaseFirestore getDataBase() {
        if (dataBase == null)
            dataBase = FirebaseFirestore.getInstance();
        return dataBase;
    }
}
