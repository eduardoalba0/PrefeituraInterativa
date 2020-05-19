package br.edu.ifpr.bsi.prefeiturainterativa.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.dialogs.DialogCategorias;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Departamento;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DepartamentosAdapter extends RecyclerView.Adapter<DepartamentosAdapter.ViewHolder> {

    private Activity context;
    private List<Departamento> departamentos;
    private FragmentManager fm;
    private boolean editavel;

    public DepartamentosAdapter(Activity context, List<Departamento> departamentos, FragmentManager fm) {
        this.editavel = true;
        this.context = context;
        this.departamentos = departamentos;
        this.fm = fm;
    }

    public DepartamentosAdapter(Activity context, List<Departamento> departamentos, FragmentManager fm, boolean b) {
        this.editavel = b;
        this.context = context;
        this.departamentos = departamentos;
        this.fm = fm;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_departamento, parent, false);
        return new DepartamentosAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(departamentos.get(position), position);
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
                    new DialogCategorias(departamento, editavel).show(fm, "TiposSolicitacao");
                    break;
            }
        }

        public void setData(Departamento departamento, int posicao) {
            this.departamento = departamento;
            tv_departamento.setText(departamento.getDescricao());
            float pos = (float) posicao + 1;
            if (pos % 3 == 0)
                l_gradient.setBackground(context.getDrawable(R.drawable.shape_quadrado_arr_vermelho));
            else if (pos % 2 == 0)
                l_gradient.setBackground(context.getDrawable(R.drawable.shape_quadrado_arr_azul));
            else
                l_gradient.setBackground(context.getDrawable(R.drawable.shape_quadrado_arr_verde));


        }

        @BindView(R.id.tv_departamento)
        TextView tv_departamento;

        @BindView(R.id.l_gradient)
        RelativeLayout l_gradient;
    }
}
