package br.edu.ifpr.bsi.prefeiturainterativa.adapters;

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

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.ViewModelsHelper;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewerBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    String imagem;

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
            carregarImagem();
        return view;
    }

    @OnClick({R.id.bt_aceitar, R.id.bt_recusar})
    @Override
    public void onClick(View view) {
        ViewModelsHelper viewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(ViewModelsHelper.class);
        switch (view.getId()) {
            case R.id.bt_aceitar:
                dismiss();
                viewModel.getImagemString().postValue(imagem);
                break;
            case R.id.bt_recusar:
                dismiss();
                break;
        }
    }

    private void carregarImagem() {
        Glide.with(this)
                .load(imagem)
                .placeholder(R.drawable.ic_adicionar_galeria)
                .into(img_viewer);
    }

    @BindView(R.id.img_viewer)
    ImageView img_viewer;

    @BindView(R.id.bt_aceitar)
    FloatingActionButton bt_aceitar;

    @BindView(R.id.bt_recusar)
    FloatingActionButton bt_recusar;
}
