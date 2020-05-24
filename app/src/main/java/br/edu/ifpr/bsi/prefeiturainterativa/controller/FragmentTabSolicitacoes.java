package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.SolicitacoesAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.CategoriaDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.Categorias_SolicitacaoDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.SolicitacaoDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Categoria;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Categorias_Solicitacao;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentTabSolicitacoes extends Fragment {

    private boolean concluidas;
    private SolicitacaoDAO dao;
    private CategoriaDAO daoCategorias;
    private Categorias_SolicitacaoDAO daoMerge;

    public FragmentTabSolicitacoes() {
    }

    public FragmentTabSolicitacoes(boolean concluidas) {
        this.concluidas = concluidas;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_solicitacoes, container, false);
        ButterKnife.bind(this, view);
        initRecyclerView();
        return view;
    }


    public void initRecyclerView() {
        dao = new SolicitacaoDAO(getActivity());
        daoCategorias = new CategoriaDAO(getActivity());
        daoMerge = new Categorias_SolicitacaoDAO(getActivity());
        rv_solicitacoes.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        dao.getAllporStatus(concluidas).addOnSuccessListener(getActivity(), o -> {
            List<Solicitacao> result = o.toObjects(Solicitacao.class);
            for (Solicitacao solicitacao : result) {
                List<Categoria> categorias = new ArrayList<>();
                daoMerge.getAllporSolicitacao(solicitacao).addOnSuccessListener(getActivity(), queryDocumentSnapshots -> {
                    for (Categorias_Solicitacao merge : queryDocumentSnapshots.toObjects(Categorias_Solicitacao.class)) {
                        Categoria categoria = new Categoria();
                        categoria.set_ID(merge.getCategoria_ID());
                        daoCategorias.get(categoria).addOnSuccessListener(getActivity(), documentSnapshot -> {
                            categorias.add(documentSnapshot.toObject(Categoria.class));
                            solicitacao.setLocalCategorias(categorias);
                            rv_solicitacoes.setAdapter(new SolicitacoesAdapter(getActivity(), result, getChildFragmentManager()));
                        });
                    }
                });
            }
        });
    }

    @BindView(R.id.rv_solicitacoes)
    RecyclerView rv_solicitacoes;
}
