package br.edu.ifpr.bsi.prefeiturainterativa.helpers.dialogs;

import android.app.Dialog;
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
import androidx.viewpager.widget.ViewPager;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.AnexosTabAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DialogSelector extends BottomSheetDialogFragment {


    private boolean resultadoUnico;

    public DialogSelector(boolean resultadoUnico) {
        this.resultadoUnico = resultadoUnico;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(windowParams);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_selector, container, false);
        ButterKnife.bind(this, view);
        initTabLayout();
        return view;
    }

    public void initTabLayout() {
        AnexosTabAdapter adapter = new AnexosTabAdapter(resultadoUnico, getChildFragmentManager());
        pager_anexos.setAdapter(adapter);
        tabs_anexos.setupWithViewPager(pager_anexos);
        tabs_anexos.getTabAt(0).setIcon(R.drawable.ic_adicionar_foto);
        tabs_anexos.getTabAt(1).setIcon(R.drawable.ic_adicionar_galeria);
    }

    @BindView(R.id.pager_anexos)
    ViewPager pager_anexos;

    @BindView(R.id.tabs_anexos)
    TabLayout tabs_anexos;
}