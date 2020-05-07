package br.edu.ifpr.bsi.prefeiturainterativa.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.dialogs.DialogTiposSolicitacao;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Departamento;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoriasDepartamentoAdapter extends RecyclerView.Adapter<CategoriasDepartamentoAdapter.ViewHolder> {

    private Activity context;
    private List<Departamento> departamentos;
    private FragmentManager fm;

    public CategoriasDepartamentoAdapter(Activity context, List<Departamento> departamentos, FragmentManager fm) {
        this.context = context;
        this.departamentos = departamentos;
        this.fm = fm;
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
    }

    @Override
    public int getItemCount() {
        return departamentos.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Departamento departamento;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.tv_departamento)
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_departamento:
                    new DialogTiposSolicitacao(departamento).show(fm, "TiposSolicitacao");
                    break;
            }
        }

        public void setData(Departamento departamento) {
            this.departamento = departamento;
            tv_departamento.setText(departamento.getDescricao());
        }

        @BindView(R.id.tv_departamento)
        TextView tv_departamento;
    }
}
