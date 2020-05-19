package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.os.Bundle;
import android.transition.Transition;
import android.view.WindowManager;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.SolicitacaoVisualizarTabAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.TransitionHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivitySolicitacaoVisualizar extends FragmentActivity {

    private Solicitacao solicitacao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitacao_visualizar);
        ButterKnife.bind(this, this);
        initTabLayout();
        startAnimation();
    }

    public void initTabLayout() {
        solicitacao = (Solicitacao) getIntent().getSerializableExtra("Solicitacao");
        if (solicitacao == null) {
            onBackPressed();
            finish();
            return;
        }
        pager_solicitacao.setAdapter(new SolicitacaoVisualizarTabAdapter(getSupportFragmentManager(), solicitacao));
        tabs_solicitacao.setupWithViewPager(pager_solicitacao);
        tabs_solicitacao.getTabAt(0).setText(R.string.str_dados_solicitacao);
        tabs_solicitacao.getTabAt(1).setText(R.string.str_tramitacao);
    }

    public void startAnimation() {
        Transition t = TransitionHelper.inflateExplodeTransition(1500);
        getWindow().setEnterTransition(t);
    }

    @BindView(R.id.pager_solicitacao)
    ViewPager pager_solicitacao;

    @BindView(R.id.tab_solicitacao)
    TabLayout tabs_solicitacao;
}