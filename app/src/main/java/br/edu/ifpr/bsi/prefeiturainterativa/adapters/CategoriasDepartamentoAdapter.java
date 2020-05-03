package br.edu.ifpr.bsi.prefeiturainterativa.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.TipoSolicitacaoDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Departamento;
import br.edu.ifpr.bsi.prefeiturainterativa.model.TipoSolicitacao;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoriasDepartamentoAdapter extends RecyclerView.Adapter<CategoriasDepartamentoAdapter.ViewHolder> {

    private Activity context;
    private List<Departamento> departamentos;
    private List<TipoSolicitacao> tipoSolicitacoes;
    private TipoSolicitacaoDAO dao;

    public CategoriasDepartamentoAdapter(Activity context, List<Departamento> departamentos) {
        this.context = context;
        this.departamentos = departamentos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_categorias_departamento, parent, false);
        return new CategoriasDepartamentoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(departamentos.get(position));
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.SPACE_EVENLY);
        holder.rv_tipoSolicitacao.setLayoutManager(layoutManager);
        holder.rv_tipoSolicitacao.setAdapter(new CategoriasSolicitacaoAdapter(context, departamentos.get(position).getTiposSolicitacao()));
    }

    @Override
    public int getItemCount() {
        return departamentos.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            dao = new TipoSolicitacaoDAO(context);
            tipoSolicitacoes = new ArrayList<>();
        }

        public void setData(Departamento departamento) {
            tv_departamento.setText(departamento.getDescricao());
        }

        @BindView(R.id.tv_departamento)
        TextView tv_departamento;

        @BindView(R.id.rv_tipoSolicitacao)
        RecyclerView rv_tipoSolicitacao;
    }
}
