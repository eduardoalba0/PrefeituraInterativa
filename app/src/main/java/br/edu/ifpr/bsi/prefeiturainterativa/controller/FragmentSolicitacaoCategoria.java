package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
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
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.CategoriasDepartamentoAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.CategoriasSolicitacaoAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.CategoriaDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.DepartamentoDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.ViewModelsHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Categoria;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Departamento;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentSolicitacaoCategoria extends Fragment implements Step, View.OnClickListener {

    private DepartamentoDAO depDAO;
    private CategoriaDAO categoriaDAO;
    private List<Departamento> departamentos;
    private ViewModelsHelper viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solicitacao_categoria, container, false);
        ButterKnife.bind(this, view);
        depDAO = new DepartamentoDAO(getActivity());
        categoriaDAO = new CategoriaDAO(getActivity());
        departamentos = new ArrayList<>();
        initRecyclerView();
        carregarDados();
        return view;
    }

    @OnClick(R.id.sliding_categorias)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sliding_categorias:
                sliding_categorias.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
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
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {
        Snackbar.make(getView(), error.getErrorMessage(), BaseTransientBottomBar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.ms_errorColor))
                .setTextColor(getResources().getColor(R.color.ms_white))
                .show();
    }

    public void carregarDados() {
        departamentos.clear();
        depDAO.getAll().addOnSuccessListener(departamentosSnapshot -> {
            for (DocumentSnapshot depsnapshot : departamentosSnapshot) {
                Departamento departamento = depsnapshot.toObject(Departamento.class);
                departamento.setTiposSolicitacao(new ArrayList<>());
                categoriaDAO.getAllPorDepartamento(departamento).addOnSuccessListener(tipossolicitacaoSnapshot -> {
                    departamento.setTiposSolicitacao(tipossolicitacaoSnapshot.toObjects(Categoria.class));
                    if (departamento.getTiposSolicitacao() != null && !departamento.getTiposSolicitacao().isEmpty())
                        departamentos.add(departamento);
                    rv_departamentos.setAdapter(new CategoriasDepartamentoAdapter(getActivity(), departamentos, getChildFragmentManager()));
                });
            }
        });
    }

    public void initRecyclerView() {
        rv_departamentos.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getActivity());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.SPACE_EVENLY);
        rv_topicosSelecionados.setLayoutManager(layoutManager);

        viewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(ViewModelsHelper.class);
        viewModel.getCategorias().observe(getActivity(), categorias -> {
            if (!categorias.isEmpty())
                tv_topicosVazios.setVisibility(View.GONE);
            else
                tv_topicosVazios.setVisibility(View.VISIBLE);
            tv_numeroTopicos.setText(categorias.size() + "");
            rv_topicosSelecionados.setAdapter(new CategoriasSolicitacaoAdapter(getActivity(), categorias));
        });

    }

    @BindView(R.id.rv_departamentos)
    RecyclerView rv_departamentos;

    @BindView(R.id.rv_topicosSelecionados)
    RecyclerView rv_topicosSelecionados;

    @BindView(R.id.sliding_categorias)
    SlidingUpPanelLayout sliding_categorias;

    @BindView(R.id.tv_numeroTopicos)
    TextView tv_numeroTopicos;

    @BindView(R.id.tv_topicosVazios)
    TextView tv_topicosVazios;
}
