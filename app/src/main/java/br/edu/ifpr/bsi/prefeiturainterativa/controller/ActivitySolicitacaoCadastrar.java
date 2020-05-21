package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.SolicitacaoStepAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.Categorias_SolicitacaoDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.SolicitacaoDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.FirebaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.ViewModelsHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Categoria;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Categorias_Solicitacao;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class ActivitySolicitacaoCadastrar extends FragmentActivity implements StepperLayout.StepperListener {

    private Solicitacao solicitacao;
    private Categorias_SolicitacaoDAO cat_solDAO;
    private SolicitacaoDAO solicitacaoDAO;
    private ViewModelsHelper viewModel;
    private FirebaseHelper helper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitacao_cadastrar);
        ButterKnife.bind(this, this);
        initStepper();
        helper = new FirebaseHelper(this);
    }

    @Override
    public void onCompleted(View completeButton) {
        SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText(R.string.str_carregando)
                .setContentText(getString(R.string.str_cadastrando_demanda));
        dialog.show();
        solicitacao = viewModel.getObjetoSolicitacao();
        solicitacao.setConcluida(false);
        solicitacao.setUsuario_ID(helper.getUser().getUid());

        List<String> imagens = new ArrayList<>();
        List<Task<?>> uploadTasks = new ArrayList<>();

        for (Uri imagem : viewModel.getListImagens()) {
            Task<Uri> task = helper.carregarAnexos(imagem);
            if (task != null) {
                task.addOnSuccessListener(this, uri -> imagens.add(uri.toString()));
                uploadTasks.add(task);
            }
        }
        Tasks.whenAllComplete(uploadTasks).addOnSuccessListener(this, o -> {
            solicitacao.setUrlImagens(imagens);
            List<Task<?>> insertTasks = new ArrayList<>();
            insertTasks.add(solicitacaoDAO.inserirAtualizar(solicitacao));
            for (Categoria categoria : viewModel.getListCategorias()) {
                Categorias_Solicitacao categorias_solicitacao = new Categorias_Solicitacao();
                categorias_solicitacao.setSolicitacao_ID(solicitacao.get_ID());
                categorias_solicitacao.setCategoria_ID(categoria.get_ID());
                insertTasks.add(cat_solDAO.inserirAtualizar(categorias_solicitacao));
            }

            Tasks.whenAllComplete(insertTasks).addOnSuccessListener(tasks -> {
                dialog.dismiss();
                chamarActivity(ActivityOverview.class);
            });
        });
    }

    @Override
    public void onError(VerificationError verificationError) {
        Snackbar.make(stepperLayout, verificationError.getErrorMessage(), BaseTransientBottomBar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.ms_errorColor))
                .setTextColor(getResources().getColor(R.color.ms_white))
                .show();
    }

    public void initStepper() {
        cat_solDAO = new Categorias_SolicitacaoDAO(this);
        solicitacaoDAO = new SolicitacaoDAO(this);
        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ViewModelsHelper.class);
        SolicitacaoStepAdapter adapter = new SolicitacaoStepAdapter(getSupportFragmentManager(), this);
        adapter.addFragment(new FragmentSolicitacaoLocalizacao(), getString(R.string.str_titulo_localizacao));
        adapter.addFragment(new FragmentSolicitacaoAnexos(), getString(R.string.str_titulo_detalhes));
        adapter.addFragment(new FragmentSolicitacaoCategoria(), getString(R.string.str_titulo_categorias));
        stepperLayout.setAdapter(adapter);
        stepperLayout.setListener(this);

    }

    public <T> void chamarActivity(Class<T> activity) {
        Intent intent = new Intent(ActivitySolicitacaoCadastrar.this, activity);
        intent.putExtra("Tab", 2);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(ActivitySolicitacaoCadastrar.this, stepperLayout, "splash_transition");
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {

    }

    @BindView(R.id.stepperLayout)
    StepperLayout stepperLayout;
}
