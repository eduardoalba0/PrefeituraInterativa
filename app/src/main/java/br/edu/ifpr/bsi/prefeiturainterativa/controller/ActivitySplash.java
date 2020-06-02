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

public class ActivitySplash extends AppCompatActivity {
    // todo arrumar todas as animações
    // todo criar construtor padrao para fragments, sem parametros que retornar para a atividade-mae
    // todo turorial
    // todo verificar libs inutilizadas
    // todo testar tramitação
    // todo testar avaliação
    // todo testar validador CPF
    // todo configurar dimens.xml para multiplas dimensões
    // todo implementar método "Add", para gerar Ids automaticamente
    // todo botão de avaliar quando a solicitação está concluida
    // todo implementar animação de carregamento nos recyclerviews
    // todo arrumar espaço excessivo no Adapter Tramitação


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this, this);
        startAnimation();
    }

    public void startAnimation() {
        Handler handler = new Handler();
        handler.postDelayed(this::verificarLogin, 700);
    }

    public void verificarLogin() {
        if (new FirebaseHelper(this).getUser() == null) {
            chamarActivity(ActivityLogin.class);
        } else
            chamarActivity(ActivityOverview.class);
    }

    public <T> void chamarActivity(Class<T> activity) {
        Intent intent = new Intent(ActivitySplash.this, activity);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(ActivitySplash.this, img_app, "splash_transition");
        startActivity(intent, options.toBundle());
        finish();
    }

    @BindView(R.id.img_app)
    ImageView img_app;

    @BindView(R.id.view_root)
    View view_root;
}
