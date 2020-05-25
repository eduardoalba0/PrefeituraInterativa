package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.transition.Transition;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.SolicitacaoVisualizarTabAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.SolicitacaoDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.FirebaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.MessagingHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.TransitionHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivitySolicitacaoVisualizar extends FragmentActivity implements View.OnClickListener,
        ViewPager.OnPageChangeListener {

    public static final int STYLE_NORMAL = 0,
            STYLE_PENDENTE = 1,
            STYLE_SEM_AVALIACAO = 2,
            STYLE_PENDENTE_SEM_AVALIACAO = 3;

    private int estilo;
    private Solicitacao solicitacao;

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
        solicitacao = (Solicitacao) getIntent().getSerializableExtra("Solicitacao");
        estilo = getIntent().getIntExtra("Estilo", STYLE_NORMAL);
        if (solicitacao == null) {
            String solicitacaoID = PreferenceManager.getDefaultSharedPreferences(this).getString("Solicitacao", "");
            if (solicitacaoID != null && !solicitacaoID.isEmpty()) {
                solicitacao = new Solicitacao();
                solicitacao.set_ID(solicitacaoID);
                new SolicitacaoDAO(this).get(solicitacao)
                        .addOnFailureListener(this, e -> chamarActivity(ActivityOverview.class))
                        .addOnSuccessListener(this, documentSnapshot -> {
                            solicitacao = documentSnapshot.toObject(Solicitacao.class);
                            initTabLayout(solicitacao);
                        });
            } else {
                onBackPressed();
                finish();
            }
        } else
            initTabLayout(solicitacao);
    }

    public void initTabLayout(Solicitacao solicitacao) {
        String categoria = PreferenceManager.getDefaultSharedPreferences(this).getString("Categoria", MessagingHelper.CATEGORIA_PADRAO);
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


        if (categoria.equals(MessagingHelper.CATEGORIA_AVALIACAO) && !solicitacao.isAvaliada())
            tabs_solicitacao.getTabAt(2).getOrCreateBadge().setVisible(true);

        if (categoria.equals(MessagingHelper.CATEGORIA_TRAMITACAO)) {
            tabs_solicitacao.getTabAt(1).getOrCreateBadge().setVisible(true);
            if (solicitacao.isConcluida())
                tabs_solicitacao.getTabAt(2).getOrCreateBadge().setVisible(true);
        }

        pager_solicitacao.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        tabs_solicitacao.getTabAt(position).removeBadge();
        PreferenceManager.getDefaultSharedPreferences(ActivitySolicitacaoVisualizar.this)
                .edit()
                .remove("Categoria")
                .remove("Solicitacao")
                .apply();
    }

    @Override
    public void onPageSelected(int position) {
        tabs_solicitacao.getTabAt(position).removeBadge();
        PreferenceManager.getDefaultSharedPreferences(ActivitySolicitacaoVisualizar.this)
                .edit()
                .remove("Categoria")
                .remove("Solicitacao")
                .apply();
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

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @BindView(R.id.pager_solicitacao)
    ViewPager pager_solicitacao;

    @BindView(R.id.tab_solicitacao)
    TabLayout tabs_solicitacao;

    @BindView(R.id.bt_offline)
    MaterialButton bt_offline;
}