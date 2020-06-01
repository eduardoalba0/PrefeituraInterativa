package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Transition;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.SolicitacaoVisualizarTabAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.SolicitacaoDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.FirebaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.SharedPreferencesHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.TransitionHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Aviso;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao.STYLE_NORMAL;
import static br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao.STYLE_PENDENTE;
import static br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao.STYLE_PENDENTE_SEM_AVALIACAO;
import static br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao.STYLE_SEM_AVALIACAO;

public class ActivitySolicitacaoVisualizar extends FragmentActivity implements View.OnClickListener {

    private int estilo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitacao_visualizar);
        ButterKnife.bind(this, this);
        verificarConexao();
        getSolicitacao();
        startAnimation();
    }

    @OnClick(R.id.bt_offline)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_offline:
                verificarConexao();
                Snackbar.make(pager_solicitacao, R.string.str_dica_modo_offline,
                        BaseTransientBottomBar.LENGTH_LONG)
                        .setBackgroundTint(getResources().getColor(R.color.ms_black_87_opacity))
                        .setTextColor(getResources().getColor(R.color.ms_white))
                        .show();
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        new Handler().postDelayed(this::verificarConexao, 2000);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        verificarConexao();
        return super.onTouchEvent(event);
    }

    public void verificarConexao() {
        if (new FirebaseHelper(this).conexaoAtivada())
            bt_offline.setVisibility(View.GONE);
        else bt_offline.setVisibility(View.VISIBLE);
    }

    public void getSolicitacao() {
        Solicitacao solicitacao = (Solicitacao) getIntent().getSerializableExtra("Solicitacao");
        estilo = getIntent().getIntExtra("Estilo", STYLE_NORMAL);
        if (solicitacao == null) {
            Aviso aviso = (Aviso) getIntent().getSerializableExtra("Aviso");
            if (aviso != null)
                if (aviso.getSolicitacao_ID() != null && !aviso.getSolicitacao_ID().isEmpty()) {
                    solicitacao = new Solicitacao();
                    solicitacao.set_ID(aviso.getSolicitacao_ID());
                    Task<DocumentSnapshot> task = new SolicitacaoDAO(this).get(solicitacao);
                    task.addOnFailureListener(this, e -> chamarActivity(ActivityOverview.class));
                    task.addOnSuccessListener(this, documentSnapshot -> initTabLayout(documentSnapshot.toObject(Solicitacao.class)));
                } else {
                    chamarActivity(ActivityOverview.class);
                    finish();
            }
        } else initTabLayout(solicitacao);

    }

    public void initTabLayout(Solicitacao solicitacao) {
        SharedPreferencesHelper sharedPreferences = new SharedPreferencesHelper(this);
        if (!solicitacao.isConcluida())
            if (estilo == STYLE_PENDENTE)
                estilo = STYLE_PENDENTE_SEM_AVALIACAO;
            else estilo = STYLE_SEM_AVALIACAO;

        pager_solicitacao.setAdapter(new SolicitacaoVisualizarTabAdapter(getSupportFragmentManager(), solicitacao, estilo));
        tabs_solicitacao.setupWithViewPager(pager_solicitacao);

        tabs_solicitacao.getTabAt(0).setText(R.string.str_dados_solicitacao);
        if (estilo != STYLE_PENDENTE && estilo != STYLE_PENDENTE_SEM_AVALIACAO)
            tabs_solicitacao.getTabAt(1).setText(R.string.str_tramitacao);

        if (estilo != STYLE_SEM_AVALIACAO && estilo != STYLE_PENDENTE_SEM_AVALIACAO)
            tabs_solicitacao.getTabAt(2).setText(R.string.str_avaliacao);

        if (sharedPreferences.categoriaEquals(solicitacao.get_ID(), Aviso.CATEGORIA_AVALIACAO))
            tabs_solicitacao.getTabAt(2).getOrCreateBadge().setVisible(true);

        if (sharedPreferences.categoriaEquals(solicitacao.get_ID(), Aviso.CATEGORIA_TRAMITACAO))
            tabs_solicitacao.getTabAt(1).getOrCreateBadge().setVisible(true);

        pager_solicitacao.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tabs_solicitacao.getTabAt(position).removeBadge();
                sharedPreferences.removeAvisos(solicitacao);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabs_solicitacao.getTabAt(position).removeBadge();
                sharedPreferences.removeAvisos(solicitacao);
            }
        });

    }


    public void startAnimation() {
        Transition t = TransitionHelper.inflateExplodeTransition(1500);
        getWindow().setEnterTransition(t);
    }

    public <T> void chamarActivity(Class<T> activity) {
        Intent intent = new Intent(ActivitySolicitacaoVisualizar.this, activity);
        intent.putExtra("Tab", 2);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(ActivitySolicitacaoVisualizar.this, tabs_solicitacao, "splash_transition");
        startActivity(intent, options.toBundle());
        finish();
    }

    @Override
    public void onBackPressed() {
        chamarActivity(ActivityOverview.class);
    }

    @BindView(R.id.pager_solicitacao)
    ViewPager pager_solicitacao;

    @BindView(R.id.tab_solicitacao)
    TabLayout tabs_solicitacao;

    @BindView(R.id.bt_offline)
    MaterialButton bt_offline;
}