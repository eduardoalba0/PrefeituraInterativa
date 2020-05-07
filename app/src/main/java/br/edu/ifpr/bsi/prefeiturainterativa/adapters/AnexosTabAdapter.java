package br.edu.ifpr.bsi.prefeiturainterativa.adapters;

import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.dialogs.SelectorCamera;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.dialogs.SelectorGaleria;

public class AnexosTabAdapter extends FragmentPagerAdapter {


    private boolean resultadoUnico;

    public AnexosTabAdapter(boolean resultadoUnico, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.resultadoUnico = resultadoUnico;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SelectorCamera(resultadoUnico);
            case 1:
                return new SelectorGaleria(resultadoUnico);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

}