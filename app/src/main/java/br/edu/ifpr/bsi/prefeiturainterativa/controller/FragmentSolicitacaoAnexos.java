package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.AnexosBottomSheetDialog;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.ViewModelsHelper;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentSolicitacaoAnexos extends Fragment implements Step, View.OnClickListener {

    private AnexosBottomSheetDialog dialogo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solicitacao_anexos, container, false);
        ButterKnife.bind(this, view);
        initRecyclerView();
        dialogo = new AnexosBottomSheetDialog();
        ViewModelsHelper viewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(ViewModelsHelper.class);
        Observer<File> cameraObserver = file -> {
            setImage(file);
            dialogo.dismiss();
        };
        viewModel.getImagemCamera().observe(getViewLifecycleOwner(), cameraObserver);
        return view;
    }

    @OnClick(R.id.tv_titulo)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_titulo:
                dialogo.show(getChildFragmentManager(), "AnexosDialog");
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
        layoutManager.setFlexDirection(FlexDirection.COLUMN);
        layoutManager.setJustifyContent(JustifyContent.FLEX_END);
        rv_imagens.setLayoutManager(layoutManager);
    }

    public void setImage(File file) {
        dialogo.dismiss();
        Glide.with(this)
                .load(file)
                .into(img_test);
    }

    @BindView(R.id.tv_titulo)
    TextView tv_titulo;

    @BindView(R.id.rv_imagens)
    RecyclerView rv_imagens;

    @BindView(R.id.img_test)
    ImageView img_test;
}
