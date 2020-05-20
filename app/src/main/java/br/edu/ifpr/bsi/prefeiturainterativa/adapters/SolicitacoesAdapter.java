package br.edu.ifpr.bsi.prefeiturainterativa.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.controller.ActivitySolicitacaoVisualizar;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SolicitacoesAdapter extends RecyclerView.Adapter<SolicitacoesAdapter.ViewHolder> {

    private Activity context;
    private List<Solicitacao> solicitacoes;
    private FragmentManager fm;

    public SolicitacoesAdapter(Activity context, List<Solicitacao> solicitacoes, FragmentManager fm) {
        this.context = context;
        this.solicitacoes = solicitacoes;
        this.fm = fm;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_solicitacoes, parent, false);
        return new SolicitacoesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(solicitacoes.get(position));
    }

    @Override
    public int getItemCount() {
        return solicitacoes.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Solicitacao solicitacao;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, ActivitySolicitacaoVisualizar.class);
            intent.putExtra("Solicitacao", solicitacao);
            context.startActivity(intent);
        }

        public void setData(Solicitacao solicitacao) {
            this.solicitacao = solicitacao;
            if (solicitacao.getUrlImagens() != null && !solicitacao.getUrlImagens().isEmpty())
                Glide.with(itemView)
                        .load(solicitacao.getUrlImagens().get(0))
                        .circleCrop()
                        .placeholder(R.mipmap.ic_maca)
                        .into(img_solicitacao);
            DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, new Locale("pt", "BR"));
            tv_data.setText(df.format(solicitacao.getData()));
            if (solicitacoes.size() > 1) {
                int index = solicitacoes.indexOf(solicitacao) - 1;
                if (index >= 0) {
                    Solicitacao aux = solicitacoes.get(index);
                    if (df.format(aux.getData()).equals(df.format(solicitacao.getData())))
                        tv_data.setVisibility(View.GONE);
                }
            }
            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
            layoutManager.setFlexDirection(FlexDirection.ROW);
            layoutManager.setJustifyContent(JustifyContent.SPACE_EVENLY);
            rv_categorias.setLayoutManager(layoutManager);
            rv_categorias.setAdapter(new CategoriasAdapter(context, solicitacao.getLocalCategorias(), false, CategoriasAdapter.STYLE_RED));
        }

        @BindView(R.id.img_solicitacao)
        ImageView img_solicitacao;

        @BindView(R.id.tv_data)
        TextView tv_data;

        @BindView(R.id.rv_categorias)
        RecyclerView rv_categorias;

        @BindView(R.id.card_solicitacao)
        CardView card_solicitacao;
    }
}
