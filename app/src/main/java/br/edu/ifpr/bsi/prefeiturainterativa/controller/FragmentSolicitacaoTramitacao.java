package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
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
import br.edu.ifpr.bsi.prefeiturainterativa.dao.UsuarioDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.FirebaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Atendimento;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Departamento;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Funcionario;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Usuario;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentSolicitacaoTramitacao extends Fragment {

    private Solicitacao solicitacao;
    private AtendimentoDAO dao;
    private FirebaseHelper helper;

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
        helper = new FirebaseHelper(getActivity());
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, new Locale("pt", "BR"));
        tv_data.setText(df.format(solicitacao.getData()));
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
        dao = new AtendimentoDAO(getActivity());
        rv_atendimentos.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        dao.getAllBySolicitacao(solicitacao).addOnSuccessListener(getActivity(), o -> {
            List<Atendimento> result = o.toObjects(Atendimento.class);
            for (Atendimento atendimento : result) {
                Funcionario funcionario = new Funcionario();
                funcionario.set_ID(atendimento.getFuncionario_ID());
                new FuncionarioDAO(getActivity()).get(funcionario).addOnSuccessListener(getActivity(), funcionarioSnapshot -> {
                    Usuario usuario = new Usuario();
                    usuario.set_ID(funcionario.get_ID());
                    new UsuarioDAO(getActivity()).get(usuario).addOnSuccessListener(getActivity(), documentSnapshot -> {
                        Funcionario aux = funcionarioSnapshot.toObject(Funcionario.class);
                        aux.setUsuario(documentSnapshot.toObject(Usuario.class));
                        atendimento.setFuncionario(aux);
                        Departamento departamento = new Departamento();
                        departamento.set_ID(aux.getDepartamento_ID());
                        new DepartamentoDAO(getActivity()).get(departamento).addOnSuccessListener(getActivity(), departamentoSnapshot -> {
                            atendimento.getFuncionario().setDepartamento(departamentoSnapshot.toObject(Departamento.class));
                            rv_atendimentos.setAdapter(new TramitacaoAdapter(getActivity(), result, getChildFragmentManager()));
                        });
                    });
                });
            }
        });
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
}
