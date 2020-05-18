package br.edu.ifpr.bsi.prefeiturainterativa.adapters;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.ViewModelsHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.dialogs.DialogViewer;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AnexosAdapter extends RecyclerView.Adapter<AnexosAdapter.ViewHolder> {
    private Activity context;
    private List<Uri> imagens;
    private List<String> imagensUrl;
    private FragmentManager fm;
    private boolean editavel;
    private boolean isUrl;

    public AnexosAdapter(Activity context, List<Uri> imagens, FragmentManager manager) {
        this.context = context;
        this.imagens = imagens;
        this.fm = manager;
        this.editavel = true;
        this.isUrl = false;
    }

    public AnexosAdapter(Activity context, List<String> imagens, FragmentManager manager, boolean b) {
        this.context = context;
        this.imagensUrl = imagens;
        this.fm = manager;
        this.editavel = b;
        this.isUrl = true;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_anexos, parent, false);
        return new AnexosAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (isUrl)
            holder.setData(imagensUrl.get(position));
        else
            holder.setData(imagens.get(position));
    }

    @Override
    public int getItemCount() {
        if (isUrl) {
            if (imagensUrl == null) return 0;
            else return imagensUrl.size();
        } else {
            if (imagens == null) return 0;
            else return imagens.size();
        }
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Uri imagem;
        private String imagemUrl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (editavel) bt_remover.setVisibility(View.VISIBLE);
            else bt_remover.setVisibility(View.GONE);
        }

        @OnClick({R.id.img_anexo, R.id.bt_remover})
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.img_anexo:
                    DialogViewer dialog;
                    if (isUrl)
                        dialog = new DialogViewer(imagemUrl);
                    else
                        dialog = new DialogViewer(imagem);
                    dialog.show(fm, "Viewer");
                    break;
                case R.id.bt_remover:
                    if (!isUrl) {
                        ViewModelsHelper viewModel = new ViewModelProvider((ViewModelStoreOwner) context, new ViewModelProvider.NewInstanceFactory()).get(ViewModelsHelper.class);
                        viewModel.removeImage(this.getAdapterPosition());
                    }
                    break;
            }
        }

        public void setData(Uri imagem) {
            this.imagem = imagem;
            Glide.with(itemView).load(imagem)
                    .placeholder(R.drawable.ic_adicionar_galeria)
                    .fitCenter()
                    .into(img_anexo);
        }

        public void setData(String imagem) {
            this.imagemUrl = imagem;
            Glide.with(itemView).load(imagem)
                    .placeholder(R.drawable.ic_adicionar_galeria)
                    .fitCenter()
                    .into(img_anexo);
        }

        @BindView(R.id.img_anexo)
        ImageView img_anexo;

        @BindView(R.id.bt_remover)
        MaterialButton bt_remover;
    }
}
