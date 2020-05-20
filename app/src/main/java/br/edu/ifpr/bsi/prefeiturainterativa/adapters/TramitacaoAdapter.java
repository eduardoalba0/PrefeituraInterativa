package br.edu.ifpr.bsi.prefeiturainterativa.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Atendimento;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TramitacaoAdapter extends RecyclerView.Adapter<TramitacaoAdapter.ViewHolder> {

    private Activity context;
    private List<Atendimento> atendimentos;
    private FragmentManager fm;

    public TramitacaoAdapter(Activity context, List<Atendimento> atendimentos, FragmentManager fm) {
        this.context = context;
        this.atendimentos = atendimentos;
        this.fm = fm;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_tramitacao, parent, false);
        return new TramitacaoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(atendimentos.get(position));
    }

    @Override
    public int getItemCount() {
        return atendimentos.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Atendimento atendimento;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //abrir dialog ampliando resposta
        }

        public void setData(Atendimento atendimento) {
            this.atendimento = atendimento;
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, new Locale("pt", "BR"));
            tv_data.setText(df.format(atendimento.getData()));
            edt_descricao.setText(atendimento.getResposta());
            edl_autor.setHelperText(atendimento.getFuncionario().getDepartamento().getDescricao());
            edl_autor.setHint(atendimento.getFuncionario().getUsuario().getNome());
        }

        @BindView(R.id.edl_autor)
        TextInputLayout edl_autor;

        @BindView(R.id.edt_descricao)
        TextInputEditText edt_descricao;

        @BindView(R.id.tv_data)
        TextView tv_data;
    }
}
