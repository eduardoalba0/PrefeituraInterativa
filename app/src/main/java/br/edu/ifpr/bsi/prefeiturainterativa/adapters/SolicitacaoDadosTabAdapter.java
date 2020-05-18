package br.edu.ifpr.bsi.prefeiturainterativa.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.dialogs.SolicitacaoDados;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.dialogs.SolicitacaoTramitacao;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;

public class SolicitacaoDadosTabAdapter extends FragmentPagerAdapter {

    private Solicitacao solicitacao;

    public SolicitacaoDadosTabAdapter(FragmentManager fm, Solicitacao solicitacao) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.solicitacao = solicitacao;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SolicitacaoDados(solicitacao);
            case 1:
                return new SolicitacaoTramitacao(solicitacao);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

}