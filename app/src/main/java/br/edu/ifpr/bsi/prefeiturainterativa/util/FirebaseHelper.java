package br.edu.ifpr.bsi.prefeiturainterativa.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.view.View;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import androidx.core.app.ActivityOptionsCompat;
import br.edu.ifpr.bsi.prefeiturainterativa.controller.ActivitySplash;
import br.edu.ifpr.bsi.prefeiturainterativa.controller.BaseActivity;
import br.edu.ifpr.bsi.prefeiturainterativa.controller.UsuarioLoginActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class FirebaseHelper {

    private Context context;
    private SweetAlertDialog dialogo;

    private FirebaseAuth auth;
    private FirebaseFirestore dataBase;
    private FirebaseMessaging messaging;
    private StorageReference storage;

    public FirebaseHelper(Context context){
        this.context = context;
        FirebaseApp.initializeApp(context);
        auth = FirebaseAuth.getInstance();
        dataBase = FirebaseFirestore.getInstance();
        messaging = FirebaseMessaging.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
        dialogo = new SweetAlertDialog(context);
    }

    public void logar(String email, String senha, View view) {
        if (!conexaoAtivada()) {
            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Erro!").setContentText("Você não está conectado à Internet.")
                    .show();
            return;
        }

        if (getUser() != null && !getUser().isAnonymous()) {
            auth.signOut();
        }

        dialogo = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        dialogo.setTitleText("Autenticando...");
        dialogo.show();

        auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
            dialogo.dismiss();
            if (task.isSuccessful()) {
                if (verificarEmail()) {
                    Intent intent = new Intent(context, BaseActivity.class);
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity)context, view, "base_transition");
                    context.startActivity(intent, options.toBundle());
                }
            } else
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Erro!").setContentText("E-mail e/ou senha incorretos.")
                        .show();
        });

    }

    public void registrar(String email, String senha) {
        if (!conexaoAtivada()) {
            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Erro!").setContentText("Você não está conectado à Internet.")
                    .show();
            return;
        }
        auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
            if (task.isSuccessful())
                verificarEmail();
            else {
                if (task.getException() instanceof FirebaseAuthUserCollisionException)
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Erro!").setContentText("Seu e-mail já está cadastrado.")
                            .show();
                else
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Erro!").setContentText("Falha no registro. Consulte o suporte do sistema.")
                            .show();
            }
        });
    }

    public boolean verificarEmail() {
        if (getUser() == null)
            return false;
        if (getUser().isEmailVerified())
            return true;

        getUser().sendEmailVerification().addOnCompleteListener(task -> {
            dialogo.dismiss();
            if (task.isSuccessful())
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setContentText("Ative sua conta com o link enviado para seu e-mail.")
                        .show();
            else
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Erro!").setContentText("Falha ao validar e-mail. Consulte o suporte do sistema.")
                        .show();
        });
        return false;
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

    public <T> List<T> listar(CollectionReference reference, Class<T> classe) {
        dialogo = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        dialogo.setTitleText("Buscando...");
        dialogo.show();
        List<T> lista = new ArrayList<T>();
        reference.addSnapshotListener((queryDocumentSnapshots, e) -> {
            dialogo.dismiss();
            if (e != null)
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Erro!").setContentText("Falha ao consultar o banco de dados.")
                        .show();
            else
                for (DocumentSnapshot childSnapshot : queryDocumentSnapshots) {
                    T objeto = (T) childSnapshot.toObject(classe);
                    lista.add(objeto);
                }
        });
        return lista;
    }

    public boolean conexaoAtivada() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    public FirebaseFirestore getDataBase() {
        if (dataBase == null)
            dataBase = FirebaseFirestore.getInstance();
        return dataBase;
    }

    public FirebaseMessaging getMessaging() {
        if (messaging == null)
            messaging = FirebaseMessaging.getInstance();
        return messaging;
    }

    public StorageReference getStorage() {
        if (storage == null)
            storage = FirebaseStorage.getInstance().getReference();
        return storage;
    }

    public FirebaseUser getUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
}
