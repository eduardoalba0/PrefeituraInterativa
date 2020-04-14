package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.os.Bundle;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.AtendimentoDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Atendimento;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Funcionario;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;
import br.edu.ifpr.bsi.prefeiturainterativa.util.FabricaFirebase;

public class ActivitySplash extends AppCompatActivity {
    Atendimento atendimento;
    List<Atendimento> atendimentos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_login);
    }

    private void logar() {
        new FabricaFirebase(this).logar("eduardoalba0@hotmail.com", "30091998");
    }

    private void inserir() {
        atendimento = new Atendimento();
        atendimento.setFuncionario_ID("1");
        atendimento.setSolicitacao_ID("1");
        atendimento.setResposta("Concluido.");

        Funcionario funcionario = new Funcionario();
        funcionario.set_ID("1");
        funcionario.setNome("Eduardo");
        atendimento.setFuncionario(funcionario);

        Solicitacao solicitacao = new Solicitacao();
        solicitacao.set_ID("1");
        solicitacao.setDescricao("Test");
        atendimento.setSolicitacao(solicitacao);

        AtendimentoDAO dao = new AtendimentoDAO(this);
        atendimento.set_ID(dao.inserir(atendimento));
    }

    private void listar() {
        AtendimentoDAO dao = new AtendimentoDAO(this);
        atendimentos = dao.listar();
    }
}
