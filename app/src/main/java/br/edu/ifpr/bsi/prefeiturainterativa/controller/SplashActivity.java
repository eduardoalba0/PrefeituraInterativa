package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.FirebaseHelper;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    private FirebaseHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this, this);
        helper = new FirebaseHelper(this);
        startAnimation();
    }

    public void startAnimation() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent;
            if (helper.getUser() != null && helper.getUser().isEmailVerified())
                intent = new Intent(SplashActivity.this, ActivityTemplate.class);
            else
                intent = new Intent(SplashActivity.this, UsuarioLoginActivity.class);
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(SplashActivity.this, img_app, "splash_transition");
            startActivity(intent, options.toBundle());
        }, 1000);
    }

    @BindView(R.id.img_app)
    ImageView img_app;

    @BindView(R.id.view_root)
    View view_root;
}
