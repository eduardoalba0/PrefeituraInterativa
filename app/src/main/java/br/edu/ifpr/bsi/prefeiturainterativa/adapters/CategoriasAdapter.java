package br.edu.ifpr.bsi.prefeiturainterativa.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

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

    public static final int STYLE_GREEN = 1, STYLE_BLUE = 2, STYLE_RED = 0;

    private Activity context;
    private List<Categoria> categorias;
    private ViewModelsHelper viewModel;
    private boolean editavel;
    private int style;

    public CategoriasAdapter(Activity context, List<Categoria> categorias) {
        this.style = 0;
        this.editavel = true;
        this.context = context;
        this.categorias = categorias;
        this.viewModel = new ViewModelProvider((ViewModelStoreOwner) context, new ViewModelProvider.NewInstanceFactory()).get(ViewModelsHelper.class);
    }

    public CategoriasAdapter(Activity context, List<Categoria> categorias, boolean b, int style) {
        this.style = style;
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

    protected class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

        private Categoria categoria;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            switch (style) {
                case STYLE_RED:
                    chip_solicitacao.setChipBackgroundColorResource(R.color.colorRed);
                    break;
                case STYLE_BLUE:
                    chip_solicitacao.setChipBackgroundColorResource(R.color.colorDarkBlueWhite);
                    break;
                case STYLE_GREEN:
                    chip_solicitacao.setChipBackgroundColorResource(R.color.colorDarkGreenWhite);
                    break;
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                if (viewModel.getListCategorias().size() >= 10) {
                    compoundButton.setChecked(false);
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Erro!")
                            .setContentText("Em cada solicitação, você pode marcar apenas 10 tópicos.")
                            .show();
                } else if (!viewModel.getListCategorias().isEmpty()
                        && !viewModel.getListCategorias().get(0).getDepartamento_ID().equals(categoria.getDepartamento_ID())) {
                    compoundButton.setChecked(false);
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Erro!")
                            .setContentText("Em cada solicitação, você pode marcar apenas os tópicos relacionados a uma única secretaria.")
                            .show();
                } else {
                    compoundButton.setChecked(true);
                    categoria.setSelecionada(true);
                    viewModel.addCategoria(categoria);
                }
            } else {
                if (viewModel.getListCategorias().contains(categoria)) {
                    viewModel.removeCategoria(viewModel.getListCategorias().indexOf(categoria));
                }
                compoundButton.setChecked(false);
            }
        }

        public void setData(Categoria categoria) {
            this.categoria = categoria;
            categoria.setSelecionada(viewModel.getListCategorias().contains(categoria));
            chip_solicitacao.setText(categoria.getDescricao());
            chip_solicitacao.setChecked(categoria.isSelecionada());
            chip_solicitacao.setCheckable(editavel);
            chip_solicitacao.setOnCheckedChangeListener(this);
        }

        @BindView(R.id.chip_solicitacao)
        Chip chip_solicitacao;
    }
}
