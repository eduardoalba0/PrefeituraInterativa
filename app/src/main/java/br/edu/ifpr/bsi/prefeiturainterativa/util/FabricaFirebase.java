package br.edu.ifpr.bsi.prefeiturainterativa.util;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class FabricaFirebase {

    private Context context;

    private FirebaseAuth auth;
    private FirebaseFirestore dataBase;
    private FirebaseMessaging messaging;
    private StorageReference storage;
    private FirebaseUser user;

    public FabricaFirebase(Context context) {
        this.context = context;
        inicializar();
    }

    public void inicializar() {
        FirebaseApp.initializeApp(context);
        auth = FirebaseAuth.getInstance();
        dataBase = FirebaseFirestore.getInstance();
        messaging = FirebaseMessaging.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void logar(String email, String senha) {
        if (user != null && !user.isAnonymous())
            auth.signOut();
        else {
            final SweetAlertDialog loading = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            loading.setTitleText("Autenticando...");
            loading.show();
            auth.signInWithEmailAndPassword(email, senha).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loading.dismiss();
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Falha no login!")
                            .setContentText("Por favor, verifique seus dados.")
                            .show();
                    Log.e("ERRO", e.toString());
                }
            }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    user = auth.getCurrentUser();
                    loading.dismiss();
                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Sucesso!")
                            .setContentText("Login efetuado com sucesso.")
                            .show();
                }
            });
        }
    }

    public FirebaseFirestore getDataBase() {
        return dataBase;
    }

    public FirebaseMessaging getMessaging() {
        return messaging;
    }

    public StorageReference getStorage() {
        return storage;
    }

    public FirebaseUser getUser() {
        return user;
    }
}
