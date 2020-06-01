package br.edu.ifpr.bsi.prefeiturainterativa.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.card.MaterialCardView;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.controller.ActivitySolicitacaoVisualizar;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Categoria;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Departamento;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;
import butterknife.BindView;
import butterknife.ButterKnife;

import static br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao.STYLE_NORMAL;
import static br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao.STYLE_PENDENTE;

public class SolicitacoesAdapter extends RecyclerView.Adapter<SolicitacoesAdapter.ViewHolder> {

    private Activity context;
    private List<Solicitacao> solicitacoes;
    private int estilo;

    public SolicitacoesAdapter(Activity context, List<Solicitacao> solicitacoes, int estilo) {
        this.context = context;
        this.solicitacoes = solicitacoes;
        this.estilo = estilo;
    }

    public SolicitacoesAdapter(Activity context, List<Solicitacao> solicitacoes) {
        this.context = context;
        this.solicitacoes = solicitacoes;
        this.estilo = STYLE_NORMAL;
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
            intent.putExtra("Estilo", estilo);
            context.startActivity(intent);
        }

        public void setData(Solicitacao solicitacao) {
            this.solicitacao = solicitacao;
            DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, new Locale("pt", "BR"));
            switch (estilo) {
                case STYLE_NORMAL:
                    carregarImagens(solicitacao.getUrlImagens());
                    tv_data.setVisibility(View.VISIBLE);
                    tv_data.setText(df.format(solicitacao.getData()));
                    if (solicitacoes.size() > 1) {
                        int index = solicitacoes.indexOf(solicitacao) - 1;
                        if (index >= 0) {
                            Solicitacao aux = solicitacoes.get(index);
                            if (df.format(aux.getData()).equals(df.format(solicitacao.getData())))
                                tv_data.setVisibility(View.GONE);
                        }
                    }
                    break;
                case STYLE_PENDENTE:
                    carregarImagens(solicitacao.getLocalCaminhoImagens());
                    tv_data.setVisibility(View.GONE);
                    break;
            }
            initRecyclerView(solicitacao.getLocalCategorias());
        }

        private void carregarImagens(List<String> imagens) {
            if (imagens != null && !imagens.isEmpty())
                Glide.with(itemView)
                        .load(Uri.parse(imagens.get(0)))
                        .circleCrop()
                        .placeholder(R.mipmap.ic_maca)
                        .into(img_solicitacao);
        }

        private void initRecyclerView(List<Categoria> categorias) {
            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
            layoutManager.setFlexDirection(FlexDirection.ROW);
            layoutManager.setJustifyContent(JustifyContent.SPACE_EVENLY);
            rv_categorias.setLayoutManager(layoutManager);
            rv_categorias.setAdapter(new CategoriasAdapter(context, categorias, false, Departamento.STYLE_RED));
        }

        @BindView(R.id.img_solicitacao)
        ImageView img_solicitacao;

        @BindView(R.id.tv_data)
        TextView tv_data;

        @BindView(R.id.rv_categorias)
        RecyclerView rv_categorias;

        @BindView(R.id.card_solicitacao)
        MaterialCardView card_solicitacao;
    }
}
