package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.SolicitacoesTabAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.SharedPreferencesHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Aviso;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentSolicitacoes extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solicitacoes, container,false);
        ButterKnife.bind(this, view);
        initTabLayout();
        return view;
    }

    public void initTabLayout() {
        SharedPreferencesHelper sharedPreferences = new SharedPreferencesHelper(getActivity());
        SolicitacoesTabAdapter adapter = new SolicitacoesTabAdapter(getChildFragmentManager());
        pager_solicitacoes.setAdapter(adapter);
        tab_solicitacoes.setupWithViewPager(pager_solicitacoes);
        tab_solicitacoes.getTabAt(0).setText(R.string.str_solicitacao_em_andamento);
        tab_solicitacoes.getTabAt(1).setText(R.string.str_solicitacao_encerrada);

        if (sharedPreferences.containsCategoria(Aviso.CATEGORIA_AVALIACAO)) {
            tab_solicitacoes.getTabAt(1).getOrCreateBadge().setVisible(true);
            pager_solicitacoes.setCurrentItem(1);
        } else if (sharedPreferences.containsCategoria(Aviso.CATEGORIA_TRAMITACAO)) {
            tab_solicitacoes.getTabAt(0).getOrCreateBadge().setVisible(true);
            pager_solicitacoes.setCurrentItem(0);
        }
    }

    @BindView(R.id.pager_solicitacoes)
    ViewPager pager_solicitacoes;

    @BindView(R.id.tab_solicitacoes)
    TabLayout tab_solicitacoes;
}
