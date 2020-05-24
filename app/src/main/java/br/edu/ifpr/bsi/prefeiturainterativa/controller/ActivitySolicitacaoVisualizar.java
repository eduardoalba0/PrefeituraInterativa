package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.os.Bundle;
import android.os.Handler;
import android.transition.Transition;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.SolicitacaoVisualizarTabAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.FirebaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.TransitionHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivitySolicitacaoVisualizar extends FragmentActivity implements View.OnClickListener {

    public static final int STYLE_NORMAL = 0, STYLE_PENDENTE = 1, STYLE_NAO_AVALIADA = 2, STYLE_PENDENTE_NAO_AVALIADA = 3;

    private int estilo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitacao_visualizar);
        ButterKnife.bind(this, this);
        verificarConexao();
        initTabLayout();
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

    public void initTabLayout() {
        Solicitacao solicitacao = (Solicitacao) getIntent().getSerializableExtra("Solicitacao");
        estilo = getIntent().getIntExtra("Estilo", STYLE_NORMAL);
        if (solicitacao == null) {
            onBackPressed();
            finish();
            return;
        }
        if (!solicitacao.isAvaliada())
            if (estilo == STYLE_PENDENTE)
                estilo = STYLE_PENDENTE_NAO_AVALIADA;
            else estilo = STYLE_NAO_AVALIADA;

        pager_solicitacao.setAdapter(new SolicitacaoVisualizarTabAdapter(getSupportFragmentManager(), solicitacao, estilo));
        tabs_solicitacao.setupWithViewPager(pager_solicitacao);

        tabs_solicitacao.getTabAt(0).setText(R.string.str_dados_solicitacao);
        if (estilo != STYLE_PENDENTE && estilo != STYLE_PENDENTE_NAO_AVALIADA)
            tabs_solicitacao.getTabAt(1).setText(R.string.str_tramitacao);
        if (estilo != STYLE_NAO_AVALIADA && estilo != STYLE_PENDENTE_NAO_AVALIADA)
            tabs_solicitacao.getTabAt(2).setText(R.string.str_avaliacao);
    }

    public void startAnimation() {
        Transition t = TransitionHelper.inflateExplodeTransition(1500);
        getWindow().setEnterTransition(t);
    }

    @BindView(R.id.pager_solicitacao)
    ViewPager pager_solicitacao;

    @BindView(R.id.tab_solicitacao)
    TabLayout tabs_solicitacao;

    @BindView(R.id.bt_offline)
    MaterialButton bt_offline;
}