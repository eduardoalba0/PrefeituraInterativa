package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.DepartamentosAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.CategoriaDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.DepartamentoDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Categoria;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Departamento;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentDepartamentos extends Fragment {

    private List<Departamento> departamentos;
    private DepartamentoDAO depDAO;
    private CategoriaDAO categoriaDAO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_departamentos, container, false);
        ButterKnife.bind(this, view);
        depDAO = new DepartamentoDAO(getActivity());
        categoriaDAO = new CategoriaDAO(getActivity());
        departamentos = new ArrayList<>();
        initRecyclerView();
        return view;
    }

    public void initRecyclerView() {
        rv_departamentos.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        departamentos.clear();
        depDAO.getAll().addOnSuccessListener(departamentosSnapshot -> {
            for (DocumentSnapshot depsnapshot : departamentosSnapshot) {
                Departamento departamento = depsnapshot.toObject(Departamento.class);
                departamento.setTiposSolicitacao(new ArrayList<>());
                categoriaDAO.getAllPorDepartamento(departamento).addOnSuccessListener(tipossolicitacaoSnapshot -> {
                    departamento.setTiposSolicitacao(tipossolicitacaoSnapshot.toObjects(Categoria.class));
                    if (departamento.getTiposSolicitacao() != null && !departamento.getTiposSolicitacao().isEmpty())
                        departamentos.add(departamento);
                    rv_departamentos.setAdapter(new DepartamentosAdapter(getActivity(), departamentos, getChildFragmentManager(), false));
                });
            }
        });
    }

    @BindView(R.id.rv_departamentos)
    RecyclerView rv_departamentos;
}
