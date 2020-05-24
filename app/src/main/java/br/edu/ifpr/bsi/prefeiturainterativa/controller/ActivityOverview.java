package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.transition.Transition;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.OverviewTabAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.Categorias_SolicitacaoDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.SolicitacaoDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.FirebaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.TransitionHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Categoria;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Categorias_Solicitacao;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class ActivityOverview extends FragmentActivity implements View.OnClickListener {

    FirebaseHelper helper;
    private boolean onUpload = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        ButterKnife.bind(this, this);
        initTabLayout();
        startAnimation();
        verificarConexao();
    }

    @OnClick({R.id.bt_foto, R.id.bt_offline})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_foto:
                chamarActivity(ActivitySolicitacaoCadastrar.class);
                break;
            case R.id.bt_offline:
                verificarConexao();
                Snackbar.make(pager_overview, R.string.str_dica_modo_offline,
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

    private void uploadsPendentes() {
        onUpload = true;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String jsonList = preferences.getString("SolicitacoesPendentes", "");

        if (jsonList.trim().equals("") || jsonList.trim().equals("[]"))
            return;
        SweetAlertDialog dialogo = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText(R.string.str_carregando_demandas)
                .setCancelText(getString(R.string.str_cancelar));
        dialogo.show();

        Gson gson = new Gson();

        List<Solicitacao> listPendentes = new ArrayList<>();
        Collections.addAll(listPendentes, gson.fromJson(jsonList, Solicitacao[].class));

        for (Solicitacao solicitacao : listPendentes) {
            Log.e("Inicio", "ListCategorias:" + solicitacao.getLocalCategorias());
            List<String> imagens = new ArrayList<>();
            List<Task<?>> uploadTasks = new ArrayList<>();

            for (String imagem : solicitacao.getLocalCaminhoImagens()) {
                Task<Uri> uploadTask = helper.carregarAnexos(Uri.parse(imagem));
                if (uploadTask != null) {
                    uploadTask.addOnSuccessListener(this, uri -> imagens.add(uri.toString()));
                    uploadTasks.add(uploadTask);
                }
            }
            Tasks.whenAllComplete(uploadTasks).addOnSuccessListener(this, o -> {
                solicitacao.setUrlImagens(imagens);
                List<Task<?>> insertTasks = new ArrayList<>();
                insertTasks.add(new SolicitacaoDAO(this).inserirAtualizar(solicitacao));

                for (Categoria categoria : solicitacao.getLocalCategorias()) {
                    Categorias_Solicitacao categorias_solicitacao = new Categorias_Solicitacao();
                    categorias_solicitacao.setSolicitacao_ID(solicitacao.get_ID());
                    categorias_solicitacao.setCategoria_ID(categoria.get_ID());
                    insertTasks.add(new Categorias_SolicitacaoDAO(this).inserirAtualizar(categorias_solicitacao));
                }

                Tasks.whenAllComplete(insertTasks).addOnSuccessListener(tasks -> {
                    List<Solicitacao> aux = new ArrayList<>();
                    Collections.addAll(listPendentes, gson.fromJson(jsonList, Solicitacao[].class));
                    aux.remove(solicitacao);
                    preferences.edit().remove("SolicitacoesPendentes")
                            .putString("SolicitacoesPendentes", gson.toJson(aux)).apply();
                    dialogo.dismiss();
                    onUpload = false;
                });
            });
        }
    }

    public void verificarConexao() {
        helper = new FirebaseHelper(this);
        if (helper.getUser() == null || !helper.getUser().isEmailVerified()) {
            chamarActivity(ActivityLogin.class);
            return;
        }
        if (helper.conexaoAtivada()) {
            bt_offline.setVisibility(View.GONE);
            if (!onUpload)
                uploadsPendentes();
        } else bt_offline.setVisibility(View.VISIBLE);
    }


    private <T> void chamarActivity(Class<T> activity) {
        Intent intent = new Intent(ActivityOverview.this, activity);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(ActivityOverview.this, bt_foto, "splash_transition");
        startActivity(intent, options.toBundle());
        finish();
    }

    private void startAnimation() {
        Transition t = TransitionHelper.inflateChangeBoundsTransition(this);
        t.addListener(TransitionHelper.getCircularEnterTransitionListener(bt_foto, view_root, pager_overview));
        getWindow().setSharedElementEnterTransition(t);
    }

    private void initTabLayout() {
        pager_overview.setAdapter(new OverviewTabAdapter(getSupportFragmentManager()));
        tab_overview.setupWithViewPager(pager_overview);
        tab_overview.getTabAt(0).setIcon(R.drawable.ic_inicio);
        tab_overview.getTabAt(1).setIcon(R.drawable.ic_departamento);
        tab_overview.getTabAt(2).setIcon(R.drawable.ic_solicitacoes);
        tab_overview.getTabAt(3).setIcon(R.drawable.ic_usuario);

        if (getIntent() != null)
            trocarPagina(getIntent().getIntExtra("Tab", 0));
    }

    public void trocarPagina(int index) {
        pager_overview.setCurrentItem(index, false);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStack();
        else {
            finishAffinity();
        }
    }

    @BindView(R.id.bt_foto)
    FloatingActionButton bt_foto;

    @BindView(R.id.tab_overview)
    TabLayout tab_overview;

    @BindView(R.id.view_root)
    View view_root;

    @BindView(R.id.pager_overview)
    ViewPager pager_overview;

    @BindView(R.id.bt_offline)
    MaterialButton bt_offline;
}
