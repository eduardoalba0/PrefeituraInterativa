package br.edu.ifpr.bsi.prefeiturainterativa.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GaleriaAdapter extends RecyclerView.Adapter<GaleriaAdapter.ViewHolder> {
    private Activity context;
    private List<String> imagens;
    private FragmentManager fm;

    public GaleriaAdapter(Activity context, List<String> imagens, FragmentManager manager) {
        this.context = context;
        this.imagens = imagens;
        this.fm = manager;
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

        private String imagem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.img_galeria)
        @Override
        public void onClick(View view) {
            Log.e("GaleriaAdapter", "OnClick");
            ViewerBottomSheetDialog viewer = new ViewerBottomSheetDialog();
            Bundle bundle = new Bundle();
            bundle.putString("Imagem", imagem);
            viewer.setArguments(bundle);
            viewer.show(fm, "Viewer");
        }

        public void setData(String imagem) {
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
