package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.UsuarioDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.FirebaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Aviso;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Usuario;
import butterknife.BindView;
import butterknife.ButterKnife;
import fcm.androidtoandroid.FirebasePush;
import fcm.androidtoandroid.model.Notification;

public class ActivitySplash extends AppCompatActivity {

    private FirebaseHelper helper;
    private UsuarioDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this, this);
        helper = new FirebaseHelper(this);
        dao = new UsuarioDAO(this);
        startAnimation();
    }

    public void startAnimation() {
        FirebasePush firebasePush = new FirebasePush("AAAA463WeYk:APA91bEG9L93sqV3JAP6p8Y0sGvX-NwcCUTKZghnjM7-CsCg_FzS0ueB8vUzqrhomTXEReWqTwr9ALNVKm7Si_DDyyuhjt_1ifpYczWqa76dLbiZNiZcf2xD9Mhop1dmIrA5LM4OLIBz");
        try {
            JSONObject json = new JSONObject();
            json.put("Categoria", Aviso.CATEGORIA_TRAMITACAO);
            json.put("Solicitacao", "6edd4d61-2cd7-40bd-b784-5d828a6fb9fb");
            firebasePush.setNotification(new Notification("Teste Titulo", "Teste Corpo"))
                    .setData(json)
                    .sendToToken("eFs1yk1CTXungCOTP0oGYf:APA91bFdwcmxNlpXlicj_6sIvq2jDBlsADFeI9yRY4I9CzNq2SMa--atM3xftIHGgg9eL7-IDNDHqfqoUNqNDrszsRymPDqZSEFdcKeY49Mz3rq5OD1mnmrPQmI5TdmiRg6t-_hg1_O5");
        } catch (JSONException ex) {

        }
        Handler handler = new Handler();
        handler.postDelayed(this::verificarDados, 700);
    }

    public void verificarDados() {
        Usuario usuario = new Usuario();
        if (helper.getUser() == null) {
            chamarActivity(ActivityLogin.class);
            return;
        }
        usuario.set_ID(helper.getUser().getUid());
        String cpf = PreferenceManager.getDefaultSharedPreferences(this).getString(usuario.get_ID(), "");
        if (!cpf.trim().equalsIgnoreCase("")) {
            chamarActivity(ActivityOverview.class);
            return;
        }
        if (!helper.conexaoAtivada()) {
            chamarActivity(ActivityLogin.class);
            return;
        }

        Task<DocumentSnapshot> task = dao.get(usuario);
        if (task != null)
            task.addOnSuccessListener(this, documentSnapshot -> {
                Usuario aux = documentSnapshot.toObject(Usuario.class);
                if (aux == null || aux.getCpf() == null || aux.getCpf().trim().equalsIgnoreCase(""))
                    chamarActivity(ActivityCompletarCadastro.class);
                else
                    chamarActivity(ActivityOverview.class);
            });

    }

    public <T> void chamarActivity(Class<T> activity) {
        Intent intent = new Intent(ActivitySplash.this, activity);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(ActivitySplash.this, img_app, "splash_transition");
        startActivity(intent, options.toBundle());
    }

    @BindView(R.id.img_app)
    ImageView img_app;

    @BindView(R.id.view_root)
    View view_root;
}
