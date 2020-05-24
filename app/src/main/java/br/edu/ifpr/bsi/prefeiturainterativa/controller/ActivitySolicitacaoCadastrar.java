package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class ActivitySolicitacaoCadastrar extends FragmentActivity implements View.OnClickListener,
        StepperLayout.StepperListener {

    private Solicitacao solicitacao;
    private SolicitacaoDAO solicitacaoDAO;
    private ViewModelsHelper viewModel;
    private FirebaseHelper helper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitacao_cadastrar);
        ButterKnife.bind(this, this);
        solicitacaoDAO = new SolicitacaoDAO(this);
        helper = new FirebaseHelper(this);
        verificarConexao();
        initStepper();
    }

    @OnClick(R.id.bt_offline)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_offline:
                verificarConexao();
                Snackbar.make(stepperLayout, R.string.str_dica_modo_offline,
                        BaseTransientBottomBar.LENGTH_LONG)
                        .setBackgroundTint(getResources().getColor(R.color.ms_black_87_opacity))
                        .setTextColor(getResources().getColor(R.color.ms_white))
                        .show();
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        new Handler().postDelayed(this::verificarConexao, 2000);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        verificarConexao();
        return super.onTouchEvent(event);
    }

    public void verificarConexao() {
        if (helper.conexaoAtivada())
            bt_offline.setVisibility(View.GONE);
        else bt_offline.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCompleted(View completeButton) {
        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ViewModelsHelper.class);
        solicitacao = viewModel.getObjetoSolicitacao();
        solicitacao.set_ID(UUID.randomUUID().toString());
        solicitacao.setConcluida(false);
        solicitacao.setUsuario_ID(helper.getUser().getUid());
        solicitacao.setLocalCategorias(viewModel.getListCategorias());
        Log.e("SolicitacaoCadastrar", "ListViewlmodel: " + viewModel.getListCategorias());
        if (helper.conexaoAtivada())
            salvarOnline();
        else {
            salvarOffline();
        }
    }

    private void salvarOnline() {
        SweetAlertDialog dialogo = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText(R.string.str_carregando)
                .setContentText(getString(R.string.str_cadastrando_demanda));
        dialogo.show();
        List<String> imagens = new ArrayList<>();
        List<Task<?>> uploadTasks = new ArrayList<>();

        for (Uri imagem : viewModel.getListImagens()) {
            Task<Uri> uploadTask = helper.carregarAnexos(imagem);
            if (uploadTask != null) {
                uploadTask.addOnSuccessListener(this, uri -> imagens.add(uri.toString()));
                uploadTasks.add(uploadTask);
            }
        }
        Tasks.whenAllComplete(uploadTasks).addOnSuccessListener(this, o -> {
            solicitacao.setUrlImagens(imagens);
            List<Task<?>> insertTasks = new ArrayList<>();
            insertTasks.add(solicitacaoDAO.inserirAtualizar(solicitacao));
            for (Categoria categoria : solicitacao.getLocalCategorias()) {
                Categorias_Solicitacao categorias_solicitacao = new Categorias_Solicitacao();
                categorias_solicitacao.setSolicitacao_ID(solicitacao.get_ID());
                categorias_solicitacao.setCategoria_ID(categoria.get_ID());
                insertTasks.add(new Categorias_SolicitacaoDAO(this).inserirAtualizar(categorias_solicitacao));
            }
            Tasks.whenAllComplete(insertTasks).addOnSuccessListener(tasks -> {
                dialogo.dismiss();
                viewModel.removeAll();
                chamarActivity(ActivityOverview.class);
                finish();
            });
        });
    }

    private void salvarOffline() {
        String jsonList = PreferenceManager.getDefaultSharedPreferences(this).getString("SolicitacoesPendentes", "");
        Gson gson = new Gson();

        List<String> caminhoImagens = new ArrayList<>();
        for (Uri uri : viewModel.getListImagens())
            caminhoImagens.add(uri.toString());

        solicitacao.setLocalCaminhoImagens(caminhoImagens);

        List<Solicitacao> listPendentes = new ArrayList<>();
        if (!jsonList.isEmpty())
            Collections.addAll(listPendentes, gson.fromJson(jsonList, Solicitacao[].class));
        listPendentes.add(solicitacao);

        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
        edit.putString("SolicitacoesPendentes", gson.toJson(listPendentes));
        edit.apply();

        viewModel.removeAll();
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(R.string.str_atencao)
                .setTitleText(R.string.str_solicitacao_cadastrada_offline)
                .setConfirmText(getResources().getString(R.string.dialog_ok))
                .setConfirmClickListener(view -> {
                    view.dismiss();
                    chamarActivity(ActivityOverview.class);
                }).show();
    }
    @Override
    public void onError(VerificationError verificationError) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(R.string.str_erro)
                .setTitleText(verificationError.getErrorMessage())
                .setConfirmText(getResources().getString(R.string.dialog_ok))
                .show();

    }

    public void initStepper() {
        SolicitacaoStepAdapter adapter = new SolicitacaoStepAdapter(getSupportFragmentManager(), this);
        adapter.addFragment(new FragmentSolicitacaoLocalizacao(), getString(R.string.str_titulo_localizacao));
        adapter.addFragment(new FragmentSolicitacaoAnexos(), getString(R.string.str_titulo_detalhes));
        adapter.addFragment(new FragmentSolicitacaoCategoria(), getString(R.string.str_titulo_categorias));
        stepperLayout.setAdapter(adapter);
        stepperLayout.setListener(this);

    }

    public <T> void chamarActivity(Class<T> activity) {
        Intent intent = new Intent(ActivitySolicitacaoCadastrar.this, activity);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(ActivitySolicitacaoCadastrar.this, stepperLayout, "splash_transition");
        startActivity(intent, options.toBundle());
        finish();
    }

    @Override
    public void onBackPressed() {
        if (stepperLayout.getCurrentStepPosition() > 0)
            stepperLayout.onBackClicked();
        else
            chamarActivity(ActivityOverview.class);
    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {

    }

    @BindView(R.id.stepperLayout)
    StepperLayout stepperLayout;

    @BindView(R.id.bt_offline)
    MaterialButton bt_offline;
}
