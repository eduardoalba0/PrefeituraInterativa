package br.edu.ifpr.bsi.prefeiturainterativa.helpers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.AnexosTabAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AnexosBottomSheetDialog extends BottomSheetDialogFragment {

    private Fragment invocador;

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0f;
        windowParams.flags += WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(windowParams);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anexos, container, false);
        ButterKnife.bind(this, view);
        initTabLayout();
        return view;
    }

    public void initTabLayout() {
        AnexosTabAdapter adapter = new AnexosTabAdapter(getChildFragmentManager());
        pager_anexos.setAdapter(adapter);
        tabs_anexos.setupWithViewPager(pager_anexos);
        tabs_anexos.getTabAt(0).setIcon(R.drawable.ic_adicionar_foto);
        tabs_anexos.getTabAt(1).setIcon(R.drawable.ic_adicionar_galeria);
        tabs_anexos.getTabAt(2).setIcon(R.drawable.ic_adicionar_arquivo);
    }

    @BindView(R.id.pager_anexos)
    ViewPager pager_anexos;

    @BindView(R.id.tabs_anexos)
    TabLayout tabs_anexos;
}
