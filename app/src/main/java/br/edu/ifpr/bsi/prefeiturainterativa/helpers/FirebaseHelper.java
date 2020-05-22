package br.edu.ifpr.bsi.prefeiturainterativa.helpers;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Usuario;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class FirebaseHelper {

    public static final int RC_GOOGLE_LOGIN = 10;

    private Activity context;

    private FirebaseAuth auth;
    private GoogleSignInClient signInClient;
    private FirebaseMessaging messaging;
    private StorageReference storage;

    public FirebaseHelper(Activity context) {
        if (FirebaseApp.getApps(context).isEmpty())
            FirebaseApp.initializeApp(context);
        this.context = context;
        auth = FirebaseAuth.getInstance();
        messaging = FirebaseMessaging.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
        signInClient = GoogleSignIn.getClient(context, new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.google_auth_id))
                .requestEmail()
                .build());
    }

    public Task<AuthResult> logar(Usuario usuario) {
        if (!conexaoAtivada()) {
            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(R.string.str_erro).setContentText(context.getString(R.string.str_erro_internet_login))
                    .show();
            return null;
        }

        if (getUser() != null && !getUser().isAnonymous()) {
            auth.signOut();
        }

        return auth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(context, authResultTask -> {
            if (!authResultTask.isSuccessful()) {
                if (authResultTask.getException().toString().contains("FirebaseAuthInvalidCredentialsException"))
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(R.string.str_erro).setContentText(context.getString(R.string.str_erro_dados_invalidos))
                            .show();
                else
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(R.string.str_erro).setContentText(context.getString(R.string.str_erro_login))
                            .show();
            }
        });

    }

    public Task<AuthResult> logarGoogle(GoogleSignInAccount account) {
        if (!conexaoAtivada()) {
            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(R.string.str_erro).setContentText("Você não está conectado à Internet.")
                    .show();
            return null;
        }

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        return auth.signInWithCredential(credential).addOnCompleteListener(context, task -> {
            if (!task.isSuccessful())
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(R.string.str_erro).setContentText("Falha ao associar conta Google. Consulte o suporte do sistema.")
                        .show();
        });
    }

    public Task<AuthResult> registrar(Usuario usuario) {
        if (!conexaoAtivada()) {
            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(R.string.str_erro).setContentText(context.getString(R.string.str_erro_internet_cadastro))
                    .show();
            return null;
        } else
            return auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(context, task -> {
                if (!task.isSuccessful()) {
                    if (task.getException().toString().contains("FirebaseAuthUserCollisionException"))
                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(R.string.str_erro).setContentText(context.getString(R.string.str_erro_email_cadastrado))
                                .show();
                    else
                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(R.string.str_erro).setContentText(context.getString(R.string.str_erro_cadastro))
                                .show();
                }
            });
    }
    public void deslogar(){
        auth.signOut();
    }
    public Task<Void> verificarEmail() {
        if (getUser().isEmailVerified())
            return null;
        return getUser().sendEmailVerification().addOnCompleteListener(context, task -> {
            if (!task.isSuccessful()) {
                if (task.getException().toString().contains("FirebaseTooManyRequestsException"))
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(R.string.str_erro).setContentText(context.getString(R.string.str_clique_link_email))
                            .show();
                else
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(R.string.str_erro).setContentText(context.getString(R.string.str_erro_validar_email))
                            .show();
            }
        });
    }


    public Task<Void> redefinirSenha(String email) {
        if (!conexaoAtivada()) {
            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(R.string.str_erro).setContentText(context.getString(R.string.str_erro_internet_redefinir_senha))
                    .show();
            return null;
        }
        return auth.sendPasswordResetEmail(email).addOnCompleteListener(context, task -> {
            if (!task.isSuccessful()) {
                if (task.getException().toString().contains("FirebaseAuthEmailException"))
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(R.string.str_erro).setContentText(context.getString(R.string.str_erro_email_nao_cadastrado))
                            .show();
                else
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(R.string.str_erro).setContentText(context.getString(R.string.str_erro_redefinir_Senha))
                            .show();
            }
        });
    }

    public Task<Void> atualizarPerfil(Usuario usuario) {
        if (!conexaoAtivada()) {
            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(R.string.str_erro).setContentText(context.getString(R.string.str_erro_internet_atualizar_perfil))
                    .show();
            return null;
        }

        UserProfileChangeRequest profileUpdates;
        if (usuario.getLocalUriFoto() == null)
            profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(usuario.getNome()).build();
        else profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(usuario.getNome()).setPhotoUri(usuario.getLocalUriFoto())
                .build();

        return auth.getCurrentUser().updateProfile(profileUpdates).addOnCompleteListener(context, task -> {
            if (!task.isSuccessful()) {
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(R.string.str_erro).setContentText(context.getString(R.string.str_erro_carregar_imagem))
                        .show();
            }
        });
    }

    public Task<Uri> carregarImagemUsuario(Uri file) {
        if (!conexaoAtivada()) {
            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(R.string.str_erro).setContentText(context.getString(R.string.str_erro_internet_carregar_imagem))
                    .show();
            return null;
        }

        StorageReference upload = storage.child("Usuarios").child(getUser().getUid()).child(System.currentTimeMillis() + "_.jpg");

        return upload.putFile(file).continueWithTask(task -> {
            if (!task.isSuccessful()) throw task.getException();
            return upload.getDownloadUrl();
        }).addOnCompleteListener(context, task -> {
            if (!task.isSuccessful())
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(R.string.str_erro).setContentText(context.getString(R.string.str_erro_carregar_imagem))
                        .show();
        });
    }

    public Task<Uri> carregarAnexos(Uri uri) {
        StorageReference upload = storage.child("Solicitacoes").child(getUser().getUid()).child(System.currentTimeMillis() + ".jpg");
        return upload.putFile(uri).continueWithTask(task -> {
            if (!task.isSuccessful()) throw task.getException();
            return upload.getDownloadUrl();
        }).addOnCompleteListener(context, task -> {
            if (!task.isSuccessful())
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(R.string.str_erro).setContentText(context.getString(R.string.str_erro_carregar_imagem))
                        .show();
        });
    }

    public boolean conexaoAtivada() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public FirebaseUser getUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public GoogleSignInClient getSignInClient() {
        return this.signInClient;
    }

}
