package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.TramitacaoAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.AtendimentoDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.DepartamentoDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.FuncionarioDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.FirebaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Atendimento;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Departamento;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Funcionario;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentSolicitacaoTramitacao extends Fragment {

    private Solicitacao solicitacao;

    public FragmentSolicitacaoTramitacao(Solicitacao solicitacao) {
        this.solicitacao = solicitacao;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solicitacao_tramitacao, container, false);
        ButterKnife.bind(this, view);
        preencherCampos();
        initRecyclerView();
        return view;
    }

    private void preencherCampos() {
        FirebaseHelper helper = new FirebaseHelper(getActivity());
        if (solicitacao.getData() == null) {
            card_solicitacao.setVisibility(View.GONE);
            tv_data.setVisibility(View.GONE);
            return;
        } else {
            card_solicitacao.setVisibility(View.VISIBLE);
            tv_data.setVisibility(View.VISIBLE);
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, new Locale("pt", "BR"));
            tv_data.setText(df.format(solicitacao.getData()));
        }

        if (solicitacao.isAnonima())
            edl_autor.setHint(helper.getUser().getDisplayName() + "(Anônimo)");
        else
            edl_autor.setHint(helper.getUser().getDisplayName());

        edt_acao.setText(R.string.str_demanda_cadastrada);
        Glide.with(this)
                .load(helper.getUser().getPhotoUrl())
                .placeholder(R.drawable.ic_usuario)
                .circleCrop()
                .into(img_usuario);
    }

    private void initRecyclerView() {
        rv_atendimentos.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        if (solicitacao.getAtendimentos() != null && !solicitacao.getAtendimentos().isEmpty()) {
            new AtendimentoDAO(getActivity()).getAll(solicitacao.getAtendimentos()).addOnSuccessListener(getActivity(), atendimentoSnapshots -> {
                FuncionarioDAO funcionarioDAO = new FuncionarioDAO(getActivity());
                DepartamentoDAO departamentoDAO = new DepartamentoDAO(getActivity());
                List<Task<?>> tasks = new ArrayList<>();
                List<Atendimento> atendimentos = atendimentoSnapshots.toObjects(Atendimento.class);
                for (Atendimento atendimento : atendimentos) {
                    funcionarioDAO.get(atendimento.getFuncionario_ID()).addOnSuccessListener(getActivity(), funcionarioSnapshot -> {
                        Funcionario funcionario = funcionarioSnapshot.toObject(Funcionario.class);
                        tasks.add(departamentoDAO.get(funcionario.getDepartamento_ID()).addOnSuccessListener(getActivity(), documentSnapshot -> {
                            funcionario.setDepartamento(documentSnapshot.toObject(Departamento.class));
                            atendimento.setFuncionario(funcionario);
                        }));
                    });
                }
                Tasks.whenAllComplete(tasks).addOnSuccessListener(getActivity(), o ->
                        rv_atendimentos.setAdapter(new TramitacaoAdapter(getActivity(), atendimentos)));
            });
        }
    }


    @BindView(R.id.tv_data)
    TextView tv_data;

    @BindView(R.id.edl_autor)
    TextInputLayout edl_autor;

    @BindView(R.id.edt_acao)
    TextInputEditText edt_acao;

    @BindView(R.id.img_usuario)
    ImageView img_usuario;

    @BindView(R.id.rv_atendimentos)
    RecyclerView rv_atendimentos;

    @BindView(R.id.card_solicitacao)
    MaterialCardView card_solicitacao;
}
