package br.edu.ifpr.bsi.prefeiturainterativa.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Atendimento;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TramitacaoAdapter extends RecyclerView.Adapter<TramitacaoAdapter.ViewHolder> {

    private Activity context;
    private List<Atendimento> atendimentos;

    public TramitacaoAdapter(Activity context, List<Atendimento> atendimentos) {
        this.context = context;
        this.atendimentos = atendimentos;
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

    protected class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(Atendimento atendimento) {
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, new Locale("pt", "BR"));
            tv_data.setText(df.format(atendimento.getData()));

            if (atendimento.getResposta() == null || atendimento.getResposta().trim().isEmpty())
                card_resposta.setVisibility(View.GONE);
            else {
                card_resposta.setVisibility(View.VISIBLE);
                edt_descricao.setText(atendimento.getResposta());
                edl_autor.setHelperText(atendimento.getFuncionario().getDadosFuncionais().getDepartamento().getDescricao());
                edl_autor.setHint(atendimento.getFuncionario().getNome());
            }

            if (atendimento.getAcao() == null || atendimento.getAcao().trim().isEmpty())
                tv_acao.setVisibility(View.GONE);
            else {
                tv_acao.setVisibility(View.VISIBLE);
                tv_acao.setText(atendimento.getAcao());
            }

        }

        @BindView(R.id.edl_autor)
        TextInputLayout edl_autor;

        @BindView(R.id.edt_descricao)
        TextInputEditText edt_descricao;

        @BindView(R.id.tv_data)
        TextView tv_data;

        @BindView(R.id.tv_acao)
        TextView tv_acao;

        @BindView(R.id.card_resposta)
        MaterialCardView card_resposta;
    }
}
