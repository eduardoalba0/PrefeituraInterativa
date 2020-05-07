package br.edu.ifpr.bsi.prefeiturainterativa.helpers.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.ViewModelsHelper;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialogViewer extends BottomSheetDialogFragment implements View.OnClickListener {


    private boolean mostrarBotoes;
    private boolean resultadoUnico;
    private String imagem;

    public DialogViewer(boolean resultadoUnico, boolean mostrarBotoes) {
        this.resultadoUnico = resultadoUnico;
        this.mostrarBotoes = mostrarBotoes;
    }

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
        View view = inflater.inflate(R.layout.fragment_viewer, container, false);
        ButterKnife.bind(this, view);
        imagem = (String) getArguments().get("Imagem");
        if (imagem == null)
            this.dismiss();
        else
            initViewer();
        return view;
    }

    @OnClick({R.id.bt_aceitar, R.id.bt_recusar})
    @Override
    public void onClick(View view) {
        ViewModelsHelper viewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(ViewModelsHelper.class);
        switch (view.getId()) {
            case R.id.bt_aceitar:
                if (resultadoUnico)
                    viewModel.getImagemString().postValue(imagem);
                else viewModel.addImage(imagem);
                dismiss();
                break;
            case R.id.bt_recusar:
                dismiss();
                break;
        }
    }

    private void initViewer() {
        Glide.with(this)
                .load(imagem)
                .placeholder(R.drawable.ic_adicionar_galeria)
                .into(img_viewer);
        if (!mostrarBotoes) {
            bt_aceitar.setVisibility(View.GONE);
            bt_recusar.setVisibility(View.GONE);
        }
    }

    @BindView(R.id.img_viewer)
    ImageView img_viewer;

    @BindView(R.id.bt_aceitar)
    FloatingActionButton bt_aceitar;

    @BindView(R.id.bt_recusar)
    FloatingActionButton bt_recusar;
}
