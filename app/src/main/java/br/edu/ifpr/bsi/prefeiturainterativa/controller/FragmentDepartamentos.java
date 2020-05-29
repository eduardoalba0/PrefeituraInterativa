package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

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
    //TODO MAIS CORES PARA OS DEPARTAMENTOS

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_departamentos, container, false);
        ButterKnife.bind(this, view);
        initRecyclerView();
        return view;
    }

    public void initRecyclerView() {
        CategoriaDAO dao = new CategoriaDAO(getActivity());
        rv_departamentos.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        new DepartamentoDAO(getActivity()).getAll().addOnSuccessListener(departamentosSnapshot -> {
            List<Departamento> departamentos = new ArrayList<>();
            List<Task<?>> tasks = new ArrayList<>();
            for (Departamento departamento : departamentosSnapshot.toObjects(Departamento.class)) {
                if (departamento.getCategorias() != null && !departamento.getCategorias().isEmpty()) {
                    tasks.add(dao.getAllHabilitadas(departamento.getCategorias()).addOnSuccessListener(getActivity(), categoriaSnapshot -> {
                        departamento.setLocalCategorias(categoriaSnapshot.toObjects(Categoria.class));
                        departamentos.add(departamento);
                    }));
                }
            }
            Tasks.whenAllComplete(tasks).addOnSuccessListener(getActivity(), tasks1 ->
                    rv_departamentos.setAdapter(new DepartamentosAdapter(getActivity(), departamentos, getChildFragmentManager(), false)));
        });
    }

    @BindView(R.id.rv_departamentos)
    RecyclerView rv_departamentos;
}
