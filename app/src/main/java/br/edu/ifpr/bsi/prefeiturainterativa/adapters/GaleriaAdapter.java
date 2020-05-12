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
    private Activity context;
    private List<Uri> imagens;
    private FragmentManager fm;
    private boolean resultadoUnico;

    public GaleriaAdapter(Activity context, List<Uri> imagens, FragmentManager manager, boolean resultadoUnico) {
        this.context = context;
        this.imagens = imagens;
        this.fm = manager;
        this.resultadoUnico = resultadoUnico;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_galeria, parent, false);
        return new GaleriaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(imagens.get(position));
    }

    @Override
    public int getItemCount() {
        return imagens.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Uri imagem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.img_galeria)
        @Override
        public void onClick(View view) {
            DialogViewer viewer = new DialogViewer(resultadoUnico, imagem, true);
            viewer.show(fm, "Viewer");
        }

        public void setData(Uri imagem) {
            this.imagem = imagem;
            Glide.with(itemView).load(imagem)
                    .placeholder(R.drawable.ic_adicionar_galeria)
                    .centerCrop()
                    .into(img_galeria);
        }

        @BindView(R.id.img_galeria)
        ImageView img_galeria;
    }
}
