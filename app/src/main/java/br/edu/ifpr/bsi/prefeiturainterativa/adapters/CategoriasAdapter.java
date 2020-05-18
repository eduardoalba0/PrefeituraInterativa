package br.edu.ifpr.bsi.prefeiturainterativa.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.chip.Chip;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.ViewModelsHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Categoria;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.ViewHolder> {

    private Activity context;
    private List<Categoria> categorias;
    private ViewModelsHelper viewModel;
    private boolean editavel = true;

    public CategoriasAdapter(Activity context, List<Categoria> categorias) {
        this.context = context;
        this.categorias = categorias;
        this.viewModel = new ViewModelProvider((ViewModelStoreOwner) context, new ViewModelProvider.NewInstanceFactory()).get(ViewModelsHelper.class);
    }

    public CategoriasAdapter(Activity context, List<Categoria> categorias, boolean b) {
        this.editavel = b;
        this.context = context;
        this.categorias = categorias;
        this.viewModel = new ViewModelProvider((ViewModelStoreOwner) context, new ViewModelProvider.NewInstanceFactory()).get(ViewModelsHelper.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_categorias, parent, false);
        return new CategoriasAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(categorias.get(position));
    }

    @Override
    public int getItemCount() {
        if (categorias == null) return 0;
        return categorias.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(Categoria categoria) {
            categoria.setSelecionada(viewModel.getListCategorias().contains(categoria));
            chip_solicitacao.setText(categoria.getDescricao());
            chip_solicitacao.setChecked(categoria.isSelecionada());
            chip_solicitacao.setCheckable(editavel);
            chip_solicitacao.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b) {
                    if (viewModel.getListCategorias().size() >= 10)
                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Erro!")
                                .setContentText("Você só pode selecionar no máximo 10 tópicos por vez.")
                                .show();
                    else {
                        categoria.setSelecionada(true);
                        viewModel.addCategoria(categoria);
                    }
                } else {
                    categoria.setSelecionada(false);
                    viewModel.removeCategoria(this.getAdapterPosition());
                }
            });
        }

        @BindView(R.id.chip_solicitacao)
        Chip chip_solicitacao;
    }
}
