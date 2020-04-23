package br.edu.ifpr.bsi.prefeiturainterativa.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.controller.BaseActivity;
import br.edu.ifpr.bsi.prefeiturainterativa.controller.UsuarioLoginActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class FirebaseHelper {

    public static final int RC_GOOGLE_LOGIN = 10;

    private Context context;
    private SweetAlertDialog dialogo;

    private FirebaseAuth auth;
    private GoogleSignInClient signInClient;
    private FirebaseMessaging messaging;
    private StorageReference storage;

    public FirebaseHelper(Context context){
        if (FirebaseApp.getApps(context).isEmpty())
            FirebaseApp.initializeApp(context);
        this.context = context;
        auth = FirebaseAuth.getInstance();
        signInClient = GoogleSignIn.getClient(context, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.google_auth_id))
                .requestEmail()
                .build());
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

    public void logarGoogle(GoogleSignInAccount account, View view) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(context, BaseActivity.class);
                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation((Activity)context, view, "base_transition");
                        context.startActivity(intent, options.toBundle());
                    } else
                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Erro!").setContentText("Falha ao associar conta Google. Consulte o suporte do sistema.")
                                .show();
                });
    }

    public void registrar(String email, String senha, View view) {
        if (!conexaoAtivada()) {
            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Erro!").setContentText("Você não está conectado à Internet.")
                    .show();
            return;
        }
        auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
            if (task.isSuccessful())
                if (verificarEmail()) {
                    Intent intent = new Intent(context, BaseActivity.class);
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity) context, view, "base_transition");
                    context.startActivity(intent, options.toBundle());
                }
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
                        .setContentText("Clique no link enviado para seu e-mail para ativar a conta.")
                        .setConfirmClickListener(sweetAlertDialog -> {
                            Intent intent = new Intent(context, UsuarioLoginActivity.class);
                            context.startActivity(intent);
                        })
                        .show();
            else
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Erro!").setContentText("Falha ao validar e-mail. Consulte o suporte do sistema.")
                        .show();
        });
        return false;
    }

    public boolean conexaoAtivada() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
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

    public GoogleSignInClient getSignInClient() {
        return this.signInClient;
    }

}
