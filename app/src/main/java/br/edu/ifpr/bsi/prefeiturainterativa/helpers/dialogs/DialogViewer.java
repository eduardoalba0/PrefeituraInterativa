package br.edu.ifpr.bsi.prefeiturainterativa.helpers.dialogs;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
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
    private boolean isUrl;
    private Uri imagem;
    private String imagemUrl;

    public DialogViewer(Uri imagem) {
        this.imagem = imagem;
        this.isUrl = false;
        this.resultadoUnico = true;
        this.mostrarBotoes = false;
    }

    public DialogViewer(String imagem) {
        this.imagemUrl = imagem;
        this.isUrl = true;
        this.resultadoUnico = true;
        this.mostrarBotoes = false;
    }

    public DialogViewer(boolean resultadoUnico, Uri imagem, boolean mostrarBotoes) {
        this.resultadoUnico = resultadoUnico;
        this.mostrarBotoes = mostrarBotoes;
        this.imagem = imagem;
        this.isUrl = false;
    }

    public DialogViewer(boolean resultadoUnico, String imagem, boolean mostrarBotoes) {
        this.resultadoUnico = resultadoUnico;
        this.mostrarBotoes = mostrarBotoes;
        this.imagemUrl = imagem;
        this.isUrl = true;
    }


    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(windowParams);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_viewer, container, false);
        ButterKnife.bind(this, view);
        if (isUrl) {
            if (imagemUrl.isEmpty())
                this.dismiss();
            else
                initViewer();
        } else {
            if (imagem == null)
                this.dismiss();
            else
                initViewer();
        }
        return view;
    }

    @OnClick({R.id.bt_aceitar, R.id.bt_recusar})
    @Override
    public void onClick(View view) {
        ViewModelsHelper viewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(ViewModelsHelper.class);
        switch (view.getId()) {
            case R.id.bt_aceitar:
                if (resultadoUnico)
                    viewModel.getImagem().postValue(imagem);
                else viewModel.addImage(imagem);
                dismiss();
                break;
            case R.id.bt_recusar:
                dismiss();
                break;
        }
    }

    private void initViewer() {
        if (isUrl)
            Glide.with(this)
                    .load(imagemUrl)
                    .placeholder(R.drawable.ic_adicionar_galeria)
                    .into(img_viewer);
        else
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
    MaterialButton bt_aceitar;

    @BindView(R.id.bt_recusar)
    MaterialButton bt_recusar;
}
