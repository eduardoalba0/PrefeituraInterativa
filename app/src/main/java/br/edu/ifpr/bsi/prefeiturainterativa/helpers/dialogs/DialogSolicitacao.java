package br.edu.ifpr.bsi.prefeiturainterativa.helpers.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.SolicitacaoDadosTabAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DialogSolicitacao extends BottomSheetDialogFragment {

    private Solicitacao solicitacao;

    public DialogSolicitacao(Solicitacao solicitacao) {
        this.solicitacao = solicitacao;
    }

    @Override
    public void onStart() {
        super.onStart();
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(windowParams);
        FrameLayout bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_solicitacao, container, false);
        ButterKnife.bind(this, view);
        initTabLayout();
        return view;
    }

    public void initTabLayout() {
        pager_solicitacao.setAdapter(new SolicitacaoDadosTabAdapter(getChildFragmentManager(), solicitacao));
        tabs_solicitacao.setupWithViewPager(pager_solicitacao);
        tabs_solicitacao.getTabAt(0).setText(R.string.str_dados_solicitacao);
        tabs_solicitacao.getTabAt(1).setText(R.string.str_tramitacao);
    }

    @BindView(R.id.pager_solicitacao)
    ViewPager pager_solicitacao;

    @BindView(R.id.tab_solicitacao)
    TabLayout tabs_solicitacao;
}