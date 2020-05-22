package br.edu.ifpr.bsi.prefeiturainterativa.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.controller.FragmentSolicitacaoDados;
import br.edu.ifpr.bsi.prefeiturainterativa.controller.FragmentSolicitacaoTramitacao;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;

public class SolicitacaoVisualizarTabAdapter extends FragmentPagerAdapter {
    public static final int STYLE_NORMAL = 0, STYLE_PENDENTE = 1;

    private Solicitacao solicitacao;
    private int estilo;

    public SolicitacaoVisualizarTabAdapter(FragmentManager fm, Solicitacao solicitacao) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.solicitacao = solicitacao;
        this.estilo = STYLE_NORMAL;
    }

    public SolicitacaoVisualizarTabAdapter(FragmentManager fm, Solicitacao solicitacao, int estilo) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.solicitacao = solicitacao;
        this.estilo = estilo;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentSolicitacaoDados(solicitacao, estilo);
            case 1:
                return new FragmentSolicitacaoTramitacao(solicitacao, estilo);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

}