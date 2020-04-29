package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.UsuarioDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.FirebaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Usuario;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class SplashActivity extends AppCompatActivity {

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
        Handler handler = new Handler();
        handler.postDelayed(this::verificarDados, 1000);
    }

    public void verificarDados() {
        Usuario usuario = new Usuario();
        if (helper.getUser() == null) {
            chamarActivity(UsuarioLoginActivity.class);
            return;
        }
        usuario.set_ID(helper.getUser().getUid());
        String cpf = PreferenceManager.getDefaultSharedPreferences(this).getString(usuario.get_ID(), "");
        if (!cpf.trim().equalsIgnoreCase("")) {
            chamarActivity(ActivityTemplate.class);
            return;
        }
        if (!helper.conexaoAtivada()) {
            chamarActivity(UsuarioLoginActivity.class);
            return;
        }

        Task<DocumentSnapshot> task = dao.get(usuario);
        if (task != null)
            task.addOnSuccessListener(this, documentSnapshot -> {
                Usuario aux = documentSnapshot.toObject(Usuario.class);
                if (aux == null || aux.getCpf() == null || aux.getCpf().trim().equalsIgnoreCase(""))
                    chamarActivity(UsuarioCompletarCadastroActivity.class);
                else
                    chamarActivity(ActivityTemplate.class);
            });

    }

    public <T> void chamarActivity(Class<T> activity) {
        Intent intent = new Intent(SplashActivity.this, activity);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(SplashActivity.this, img_app, "splash_transition");
        startActivity(intent, options.toBundle());
    }

    @BindView(R.id.img_app)
    ImageView img_app;

    @BindView(R.id.view_root)
    View view_root;
}
