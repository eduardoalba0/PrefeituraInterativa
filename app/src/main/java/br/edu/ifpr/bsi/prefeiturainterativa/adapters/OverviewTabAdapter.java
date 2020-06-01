package br.edu.ifpr.bsi.prefeiturainterativa.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.controller.FragmentDepartamentos;
import br.edu.ifpr.bsi.prefeiturainterativa.controller.FragmentInicio;
import br.edu.ifpr.bsi.prefeiturainterativa.controller.FragmentPerfil;
import br.edu.ifpr.bsi.prefeiturainterativa.controller.FragmentSolicitacoes;

public class OverviewTabAdapter extends FragmentPagerAdapter {

    public OverviewTabAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentInicio();
            case 1:
                return new FragmentDepartamentos();
            case 2:
                return new FragmentSolicitacoes();
            case 3:
                return new FragmentPerfil();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}