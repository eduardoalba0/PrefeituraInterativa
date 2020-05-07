package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.button.MaterialButton;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.AnexosAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.ViewModelsHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.dialogs.DialogSelector;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentSolicitacaoAnexos extends Fragment implements Step, View.OnClickListener {

    private DialogSelector dialogo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solicitacao_anexos, container, false);
        ButterKnife.bind(this, view);
        dialogo = new DialogSelector(false);
        initRecyclerView();
        return view;
    }

    @OnClick(R.id.bt_adicionar)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_adicionar:
                dialogo.show(getChildFragmentManager(), "Selector");
                break;
        }
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    public void initRecyclerView() {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getActivity());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.CENTER);
        rv_imagens.setLayoutManager(layoutManager);

        ViewModelsHelper viewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(ViewModelsHelper.class);
        viewModel.getImagensString().observe(getViewLifecycleOwner(),
                strings -> {
                    if (strings.isEmpty())
                        rv_imagens.setAdapter(new AnexosAdapter(getActivity(), new ArrayList<>(), getChildFragmentManager()));
                    else {
                        rv_imagens.setAdapter(new AnexosAdapter(getActivity(), strings, getChildFragmentManager()));
                        dialogo.dismiss();
                    }
                });
    }

    @BindView(R.id.rv_imagens)
    RecyclerView rv_imagens;

    @BindView(R.id.bt_adicionar)
    MaterialButton bt_adicionar;
}
