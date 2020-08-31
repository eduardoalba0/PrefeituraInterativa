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
    // TODO melhorar o design do modalDialog conforme o app meditopia
    // TODO atualizar o URI da foto do usuário no DB quando cadastra / atualiza cadastro
    // TODO obrigar o usuário a inserir a descrição.
    // TODO se solicitação foi avaliada como "não solucionada" ela será encaminhada pro departamento superior.
    // TODO verificar se a senha aceita entre 6 e 11 caracteres, e consertar o validador
    // TODO atualizar Diagrama de Classes
    // TODO atualziar o model conforme o projeto Web
    // TODO consertar reautenticação (quando usuário é desabilitado no webapp não desloga no app)
    // TODO consertar atualização de departamentos (quando é alterado no webapp não aplica no app na mesma hora)
    // TODO adicionar atributo 'habilitado' no Departamento
    // TODO arrumar erro no login
    // TODO arrumar erro da foto do perfil que não carrega
    // TODO arrumar erro dos avisos não aparecerem
    // TODO arrumar erro da localização não marcada
    // TODO arrumar todas as animações
    // TODO criar construtor padrao para fragments, sem parametros que retornar para a atividade-mae
    // TODO turorial
    // TODO verificar libs inutilizadas
    // TODO testar tramitação
    // TODO testar avaliação
    // TODO testar validador CPF
    // TODO configurar dimens.xml para multiplas dimensões
    // TODO botão de avaliar quando a solicitação está concluida
    // TODO implementar animação de carregamento nos recyclerviews
    // TODO arrumar espaço excessivo no Adapter Tramitação
    // TODO implementar modalBottomSheetDialog para mostrar a resposta completa, data, autor e detalhes da tramitação (atendimento)
    // TODO melhorar regras do Cloud firestore
    

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
