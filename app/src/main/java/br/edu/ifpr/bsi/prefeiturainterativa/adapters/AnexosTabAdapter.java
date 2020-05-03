package br.edu.ifpr.bsi.prefeiturainterativa.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.controller.FragmentAnexosCamera;
import br.edu.ifpr.bsi.prefeiturainterativa.controller.FragmentAnexosDocumento;
import br.edu.ifpr.bsi.prefeiturainterativa.controller.FragmentAnexosGaleria;

public class AnexosTabAdapter extends FragmentPagerAdapter {
    private int count;

    public AnexosTabAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        count = 3;
    }

    public AnexosTabAdapter(FragmentManager fm, int count) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.count = count;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentAnexosCamera();
            case 1:
                return new FragmentAnexosGaleria();
            case 2:
                return new FragmentAnexosDocumento();
        }
        return null;
    }

    @Override
    public int getCount() {
        return count;
    }
}