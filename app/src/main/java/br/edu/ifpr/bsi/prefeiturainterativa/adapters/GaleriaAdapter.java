package br.edu.ifpr.bsi.prefeiturainterativa.adapters;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.dialogs.DialogViewer;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GaleriaAdapter extends RecyclerView.Adapter<GaleriaAdapter.ViewHolder> {

    public static final int TIPO_URI_BOTOES = 0, TIPO_STRING_SEM_BOTOES = 1;
    private Activity context;
    private List<Uri> imagens;
    private List<String> imagensString;
    private FragmentManager fm;
    private boolean resultadoUnico;

    private int tipo;

    public GaleriaAdapter(Activity context, List<Uri> imagens, FragmentManager manager, boolean resultadoUnico) {
        this.context = context;
        this.imagens = imagens;
        this.fm = manager;
        this.resultadoUnico = resultadoUnico;
        this.tipo = TIPO_URI_BOTOES;
    }

    public GaleriaAdapter(Activity context, List<String> imagensString, FragmentManager manager) {
        this.context = context;
        this.imagensString = imagensString;
        this.fm = manager;
        this.resultadoUnico = resultadoUnico;
        this.tipo = TIPO_STRING_SEM_BOTOES;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_galeria, parent, false);
        return new GaleriaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (tipo == TIPO_STRING_SEM_BOTOES)
            holder.setData(imagensString.get(position));
        else if (tipo == TIPO_URI_BOTOES)
            holder.setData(imagens.get(position));
    }

    @Override
    public int getItemCount() {
        switch (tipo) {
            case TIPO_STRING_SEM_BOTOES:
                if (imagensString != null)
                    return imagensString.size();
                break;
            case TIPO_URI_BOTOES:
                if (imagens != null)
                    return imagens.size();
                break;
        }
        return 0;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Uri imagem;
        private String imagemString;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.img_galeria)
        @Override
        public void onClick(View view) {
            DialogViewer viewer = null;
            switch (tipo) {
                case TIPO_STRING_SEM_BOTOES:
                    viewer = new DialogViewer(resultadoUnico, imagemString, false);
                    break;
                case TIPO_URI_BOTOES:
                    viewer = new DialogViewer(resultadoUnico, imagem, true);
                    break;
            }
            if (view != null)
                viewer.show(fm, "Viewer");
        }

        public void setData(Uri imagem) {
            this.imagem = imagem;
            Glide.with(itemView).load(imagem)
                    .placeholder(R.drawable.ic_adicionar_galeria)
                    .centerCrop()
                    .into(img_galeria);
        }

        public void setData(String imagem) {
            this.imagemString = imagem;
            Glide.with(itemView).load(imagem)
                    .placeholder(R.drawable.ic_adicionar_galeria)
                    .centerCrop()
                    .into(img_galeria);
        }

        @BindView(R.id.img_galeria)
        ImageView img_galeria;
    }
}
