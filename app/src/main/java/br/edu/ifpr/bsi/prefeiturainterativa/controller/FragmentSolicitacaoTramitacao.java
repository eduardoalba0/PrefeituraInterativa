package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
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
import br.edu.ifpr.bsi.prefeiturainterativa.dao.UsuarioDAO;
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
            edl_autor.setHint(helper.getUser().getDisplayName() + getString(R.string.str_nome_anonimo));
        else
            edl_autor.setHint(helper.getUser().getDisplayName());

        edt_acao.setText(R.string.str_demanda_cadastrada);
        Glide.with(this)
                .load(helper.getUser().getPhotoUrl())
                .placeholder(R.drawable.ic_usuario)
                .circleCrop()
                .into(img_usuario);
        if (solicitacao.isConcluida() && (solicitacao.getAvaliacao() == null || solicitacao.getAvaliacao().getData() == null)){
            bt_avaliar.setVisibility(View.VISIBLE);
        } else
            bt_avaliar.setVisibility(View.GONE);
    }

    private void initRecyclerView() {
        rv_atendimentos.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        if (solicitacao.getAtendimentos() != null && !solicitacao.getAtendimentos().isEmpty()) {
            new AtendimentoDAO(getActivity()).getAll(solicitacao.getAtendimentos()).addOnSuccessListener(getActivity(), atendimentoSnapshots -> {
                UsuarioDAO usuarioDAO = new UsuarioDAO(getActivity());
                DepartamentoDAO departamentoDAO = new DepartamentoDAO(getActivity());
                List<Task<?>> tasks = new ArrayList<>();
                List<Atendimento> atendimentos = new ArrayList<>();
                for (Atendimento atendimento : atendimentoSnapshots.toObjects(Atendimento.class)) {
                    usuarioDAO.get(atendimento.getFuncionario_ID()).addOnSuccessListener(getActivity(), funcionarioSnapshot -> {
                        Funcionario funcionario = funcionarioSnapshot.toObject(Funcionario.class);
                        tasks.add(departamentoDAO.get(atendimento.getDepartamento_ID()).addOnSuccessListener(getActivity(), departamentoSnapshot -> {
                            funcionario.setDepartamento(departamentoSnapshot.toObject(Departamento.class));
                            atendimento.setFuncionario(funcionario);
                            atendimentos.add(atendimento);
                            rv_atendimentos.setAdapter(new TramitacaoAdapter(getActivity(), atendimentos));
                        }));
                    });
                }
                //Tasks.whenAllComplete(tasks).addOnSuccessListener(getActivity(), o ->);
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

    @BindView(R.id.bt_avaliar)
    MaterialButton bt_avaliar;
}
