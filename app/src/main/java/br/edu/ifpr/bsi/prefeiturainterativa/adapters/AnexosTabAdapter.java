package br.edu.ifpr.bsi.prefeiturainterativa.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.controller.FragmentAnexosCamera;
import br.edu.ifpr.bsi.prefeiturainterativa.controller.FragmentAnexosGaleria;

public class AnexosTabAdapter extends FragmentPagerAdapter {

    public AnexosTabAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentAnexosCamera();
            case 1:
                return new FragmentAnexosGaleria();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}