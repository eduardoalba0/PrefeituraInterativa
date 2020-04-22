package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Fade;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivitySplash extends AppCompatActivity {

    @BindView(R.id.img_app)
    ImageView img_app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        startAnimation();
    }

    public void startAnimation() {
        Fade fade = new Fade();
        fade.setDuration(1500);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(ActivitySplash.this, UsuarioLoginActivity.class);
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(ActivitySplash.this, img_app, "splash_transition");
            startActivity(intent, options.toBundle());
        }, 500);

    }

}
