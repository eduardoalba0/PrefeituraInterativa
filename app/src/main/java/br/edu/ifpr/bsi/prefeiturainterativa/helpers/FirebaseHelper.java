package br.edu.ifpr.bsi.prefeiturainterativa.helpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;

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
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Usuario;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class FirebaseHelper {

    public static final int RC_GOOGLE_LOGIN = 10;

    private Activity context;
    private SweetAlertDialog dialogo;

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
        dialogo = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);

        signInClient = GoogleSignIn.getClient(context, new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.google_auth_id))
                .requestEmail()
                .build());
    }

    public Task<AuthResult> logar(Usuario usuario) {
        dialogo.setContentText("Autenticando...").show();
        if (!conexaoAtivada()) {
            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Erro!").setContentText("Você não está conectado à Internet.")
                    .show();
            return null;
        }

        if (getUser() != null && !getUser().isAnonymous()) {
            auth.signOut();
        }

        return auth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(context, authResultTask -> {
            dialogo.dismiss();
            if (!authResultTask.isSuccessful()) {
                if (authResultTask.getException().toString().contains("FirebaseAuthInvalidCredentialsException"))
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Erro!").setContentText("E-mail e/ou senha incorretos.")
                            .show();
                else
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Erro!").setContentText("Falha no login. Consulte o suporte do sistema.")
                            .show();
            }
        });

    }

    public Task<AuthResult> logarGoogle(GoogleSignInAccount account) {
        dialogo.setContentText("Autenticando...").show();
        if (!conexaoAtivada()) {
            dialogo.dismiss();
            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Erro!").setContentText("Você não está conectado à Internet.")
                    .show();
            return null;
        }

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        return auth.signInWithCredential(credential).addOnCompleteListener(context, task -> {
            dialogo.dismiss();
            if (!task.isSuccessful())
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Erro!").setContentText("Falha ao associar conta Google. Consulte o suporte do sistema.")
                        .show();
        });
    }

    public Task<AuthResult> registrar(Usuario usuario) {
        dialogo.setContentText("Validando dados...").show();
        if (!conexaoAtivada()) {
            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Erro!").setContentText("Você precisa de uma conexão com a internet para se registrar.")
                    .show();
            return null;
        } else
            return auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(context, task -> {
                dialogo.dismiss();
                if (!task.isSuccessful()) {
                    if (task.getException().toString().contains("FirebaseAuthUserCollisionException"))
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

    public Task<Void> verificarEmail() {
        if (getUser().isEmailVerified())
            return null;
        return getUser().sendEmailVerification().addOnCompleteListener(context, task -> {
            dialogo.dismiss();
            if (!task.isSuccessful()) {
                if (task.getException().toString().contains("FirebaseTooManyRequestsException"))
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Erro!").setContentText("Clique no link enviado para seu e-mail para ativar a conta.")
                            .show();
                else
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Erro!").setContentText("Falha ao validar e-mail. Consulte o suporte do sistema.")
                            .show();
            }
        });
    }


    public Task<Void> redefinirSenha(String email) {
        if (!conexaoAtivada()) {
            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Erro!").setContentText("Você precisa de uma conexão com a internet para redefinir a senha.")
                    .show();
            return null;
        }
        return auth.sendPasswordResetEmail(email).addOnCompleteListener(context, task -> {
            dialogo.dismiss();
            if (!task.isSuccessful()) {
                if (task.getException().toString().contains("FirebaseAuthEmailException"))
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Erro!").setContentText("O e-mail informado não pertence a nenhuma conta.")
                            .show();
                else
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Erro!").setContentText("Falha ao redefinir senha. Consulte o suporte do sistema.")
                            .show();
            }
        });
    }

    public Task<Void> atualizarPerfil(Usuario usuario) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(usuario.getNome())
                .build();
        return auth.getCurrentUser().updateProfile(profileUpdates).addOnCompleteListener(context, task -> {
            dialogo.dismiss();
            if (!task.isSuccessful()) {
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Erro!").setContentText("Falha ao atualizar perfil. Consulte o suporte do sistema.")
                        .show();
            }
        });
    }

    public StorageTask<UploadTask.TaskSnapshot> carregarImagemUsuario(Bitmap bitmap) {
        dialogo.setContentText("Carregando imagem...").show();
        if (!conexaoAtivada()) {
            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Erro!").setContentText("Você precisa de uma conexão com a internet para inserir uma imagem.")
                    .show();
            return null;
        }
        byte[] data = comprimir(bitmap);
        return storage.child("Usuarios").child(getUser().getUid()).child(getUser().getDisplayName() + "_.jpg").putBytes(data)
                .addOnCompleteListener(context, task -> {
                    dialogo.dismiss();
                    if (!task.isSuccessful())
                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Erro!").setContentText("Falha ao carregar imagem. Consulte o suporte do sistema.")
                                .show();

                });
    }

    public Bitmap decodificarImagem(String caminho) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        return BitmapFactory.decodeFile(caminho, options);
    }

    public static byte[] comprimir(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }


    public boolean conexaoAtivada() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    public FirebaseMessaging getMessaging() {
        return messaging;
    }

    public StorageReference getStorage() {
        return storage;
    }

    public FirebaseUser getUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public GoogleSignInClient getSignInClient() {
        return this.signInClient;
    }

}
