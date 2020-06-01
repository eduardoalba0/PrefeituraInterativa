package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.DepartamentosAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.CategoriaDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.DepartamentoDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.ViewModelsHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.dialogs.DialogCategorias;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Categoria;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Departamento;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentSolicitacaoCategoria extends Fragment implements Step, View.OnClickListener {

    private ViewModelsHelper viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solicitacao_categoria, container, false);
        ButterKnife.bind(this, view);
        initRecyclerView();
        carregarDados();
        return view;
    }

    @OnClick(R.id.l_footer)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.l_footer:
                new DialogCategorias().show(getChildFragmentManager(), "Categoria");
                break;
        }
    }


    @Nullable
    @Override
    public VerificationError verifyStep() {
        if (viewModel.getListCategorias().isEmpty())
            return new VerificationError(getString(R.string.str_categoria_nao_selecionada));
        return null;
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        Snackbar.make(getView(), error.getErrorMessage(), BaseTransientBottomBar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.ms_errorColor))
                .setTextColor(getResources().getColor(R.color.ms_white))
                .show();
    }

    public void initRecyclerView() {
        rv_departamentos.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        viewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(ViewModelsHelper.class);
        viewModel.getCategorias().observe(getActivity(), categorias -> tv_numeroTopicos.setText((categorias.size() + "")));

    }

    public void carregarDados() {
        CategoriaDAO dao = new CategoriaDAO(getActivity());
        rv_departamentos.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        new DepartamentoDAO(getActivity()).getAll().addOnSuccessListener(departamentosSnapshot -> {
            List<Departamento> departamentos = new ArrayList<>();
            List<Task<?>> tasks = new ArrayList<>();
            for (Departamento departamento : departamentosSnapshot.toObjects(Departamento.class)) {
                if (departamento.getCategorias() != null && !departamento.getCategorias().isEmpty())
                    tasks.add(dao.getAllHabilitadas(departamento.getCategorias()).addOnSuccessListener(getActivity(), categoriaSnapshot -> {
                        departamento.setLocalCategorias(categoriaSnapshot.toObjects(Categoria.class));
                        departamentos.add(departamento);
                    }));
            }
            Tasks.whenAllComplete(tasks).addOnSuccessListener(getActivity(), tasks1 ->
                    rv_departamentos.setAdapter(new DepartamentosAdapter(getActivity(), departamentos, getChildFragmentManager())));
        });
    }

    @Override
    public void onSelected() {

    }

    @BindView(R.id.rv_departamentos)
    RecyclerView rv_departamentos;

    @BindView(R.id.l_footer)
    RelativeLayout sliding_categorias;

    @BindView(R.id.tv_numeroTopicos)
    TextView tv_numeroTopicos;
}
