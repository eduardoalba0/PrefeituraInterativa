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

import static br.edu.ifpr.bsi.prefeiturainterativa.model.Departamento.STYLE_BLUE;
import static br.edu.ifpr.bsi.prefeiturainterativa.model.Departamento.STYLE_GREEN;
import static br.edu.ifpr.bsi.prefeiturainterativa.model.Departamento.STYLE_RED;
import static br.edu.ifpr.bsi.prefeiturainterativa.model.Departamento.STYLE_YELLOW;

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
        int style = (position + 1) % 4;
        holder.setData(departamentos.get(position), style);
    }

    @Override
    public int getItemCount() {
        return departamentos.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Departamento departamento;
        private int style;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @OnClick(R.id.tv_departamento)
        @Override
        public void onClick(View view) {
            new DialogCategorias(departamento, editavel, style)
                    .show(fm, "Categorias");
        }

        public void setData(Departamento departamento, int style) {
            this.departamento = departamento;
            this.style = style;
            tv_departamento.setText(departamento.getDescricao());
            switch (style) {
                case STYLE_RED:
                l_gradient.setBackground(context.getDrawable(R.drawable.shape_quadrado_arr_vermelho));
                    break;
                case STYLE_YELLOW:
                    l_gradient.setBackground(context.getDrawable(R.drawable.shape_quadrado_arr_amarelo));
                    break;
                case STYLE_GREEN:
                    l_gradient.setBackground(context.getDrawable(R.drawable.shape_quadrado_arr_verde));
                    break;
                case STYLE_BLUE:
                    l_gradient.setBackground(context.getDrawable(R.drawable.shape_quadrado_arr_azul));
                    break;
            }
        }

        @BindView(R.id.tv_departamento)
        TextView tv_departamento;

        @BindView(R.id.l_gradient)
        RelativeLayout l_gradient;
    }
}
