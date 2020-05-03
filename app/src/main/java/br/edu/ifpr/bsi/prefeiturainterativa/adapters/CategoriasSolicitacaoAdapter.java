package br.edu.ifpr.bsi.prefeiturainterativa.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.chip.Chip;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.model.TipoSolicitacao;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoriasSolicitacaoAdapter extends RecyclerView.Adapter<CategoriasSolicitacaoAdapter.ViewHolder> {

    private Activity context;
    private List<TipoSolicitacao> tiposSolicitacao;

    public CategoriasSolicitacaoAdapter(Activity context, List<TipoSolicitacao> solicitacoes) {
        this.context = context;
        this.tiposSolicitacao = solicitacoes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_categorias_solicitacao, parent, false);
        return new CategoriasSolicitacaoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(tiposSolicitacao.get(position));
    }

    @Override
    public int getItemCount() {
        return tiposSolicitacao.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(TipoSolicitacao tipoSolicitacao) {
            chip_solicitacao.setText(tipoSolicitacao.getDescricao());
        }

        @BindView(R.id.chip_solicitacao)
        Chip chip_solicitacao;
    }
}
