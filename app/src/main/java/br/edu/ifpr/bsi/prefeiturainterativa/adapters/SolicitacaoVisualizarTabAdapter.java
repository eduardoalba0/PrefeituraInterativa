package br.edu.ifpr.bsi.prefeiturainterativa.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.controller.FragmentSolicitacaoDados;
import br.edu.ifpr.bsi.prefeiturainterativa.controller.FragmentSolicitacaoTramitacao;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;

public class SolicitacaoVisualizarTabAdapter extends FragmentPagerAdapter {

    private Solicitacao solicitacao;

    public SolicitacaoVisualizarTabAdapter(FragmentManager fm, Solicitacao solicitacao) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.solicitacao = solicitacao;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentSolicitacaoDados(solicitacao);
            case 1:
                return new FragmentSolicitacaoTramitacao(solicitacao);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

}