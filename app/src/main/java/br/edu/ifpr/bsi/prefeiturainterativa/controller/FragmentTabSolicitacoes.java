package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.SolicitacoesAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.CategoriaDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.SolicitacaoDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Categoria;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentTabSolicitacoes extends Fragment {

    //TODO CONSTRUTOR PADRÃO EM TODOS OS FRAGMENTS SEM PARÂMETRO
    private boolean status;

    public FragmentTabSolicitacoes(boolean concluidas) {
        this.status = concluidas;
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
        rv_solicitacoes.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        new SolicitacaoDAO(getActivity()).getAllporStatus(status).addOnSuccessListener(getActivity(), o -> {
            List<Solicitacao> solicitacoes = o.toObjects(Solicitacao.class);
            for (Solicitacao solicitacao : solicitacoes) {
                Task<QuerySnapshot> task = new CategoriaDAO(getActivity()).getAll(solicitacao.getCategorias());
                task.addOnSuccessListener(getActivity(), categoriaSnapshot ->
                        solicitacao.setLocalCategorias(categoriaSnapshot.toObjects(Categoria.class)));
            }
            rv_solicitacoes.setAdapter(new SolicitacoesAdapter(getActivity(), solicitacoes));
        });
    }

    @BindView(R.id.rv_solicitacoes)
    RecyclerView rv_solicitacoes;
}
