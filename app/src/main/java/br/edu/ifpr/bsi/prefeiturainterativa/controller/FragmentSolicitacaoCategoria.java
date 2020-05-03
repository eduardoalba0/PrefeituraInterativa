package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.CategoriasDepartamentoAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.DepartamentoDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.TipoSolicitacaoDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Departamento;
import br.edu.ifpr.bsi.prefeiturainterativa.model.TipoSolicitacao;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentSolicitacaoCategoria extends Fragment implements Step {

    private DepartamentoDAO depDAO;
    private TipoSolicitacaoDAO tipoDAO;
    private List<TipoSolicitacao> tiposSolicitacao;
    private List<Departamento> departamentos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solicitacao_categoria, container, false);
        ButterKnife.bind(this, view);
        depDAO = new DepartamentoDAO(getActivity());
        tipoDAO = new TipoSolicitacaoDAO(getActivity());
        departamentos = new ArrayList<>();
        tiposSolicitacao = new ArrayList<>();
        initRecyclerView();
        carregarDados();
        return view;
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    public void carregarDados() {
        tiposSolicitacao.clear();
        departamentos.clear();
        depDAO.getAll().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Departamento departamento = documentSnapshot.toObject(Departamento.class);
                departamento.setTiposSolicitacao(new ArrayList<>());
                tipoDAO.getAllPorDepartamento(departamento).addOnSuccessListener(queryDocumentSnapshots1 -> {
                    departamento.setTiposSolicitacao(queryDocumentSnapshots1.toObjects(TipoSolicitacao.class));
                    if (departamento.getTiposSolicitacao() != null && !departamento.getTiposSolicitacao().isEmpty())
                        departamentos.add(departamento);
                    rv_departamentos.setAdapter(new CategoriasDepartamentoAdapter(getActivity(), departamentos));
                });
            }
        });
    }

    public void initRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
        rv_departamentos.setLayoutManager(layoutManager);
    }

    @BindView(R.id.rv_departamentos)
    RecyclerView rv_departamentos;
}
