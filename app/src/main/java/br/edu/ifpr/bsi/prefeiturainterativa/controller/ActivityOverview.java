package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Transition;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.OverviewTabAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.SolicitacaoDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.FirebaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.SharedPreferencesHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.TransitionHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Aviso;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class ActivityOverview extends FragmentActivity implements View.OnClickListener {

    private FirebaseHelper helper;
    private boolean onUpload = false;
    private SharedPreferencesHelper sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        ButterKnife.bind(this, this);
        sharedPreferences = new SharedPreferencesHelper(this);
        helper = new FirebaseHelper(this);
        initTabLayout();
        startAnimation();
        verificarConexao();
        reautenticar();
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
        if (sharedPreferences.getSolicitaoes().size() <= 0)
            return;
        onUpload = true;

        SweetAlertDialog dialogo = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText(R.string.str_carregando_demandas)
                .setCancelText(getString(R.string.str_cancelar));
        dialogo.show();

        for (Solicitacao solicitacao : sharedPreferences.getSolicitaoes()) {
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
                Task task = new SolicitacaoDAO(this).inserirAtualizar(solicitacao);
                task.addOnSuccessListener(this, aVoid -> {
                    sharedPreferences.removeSolicitacao(solicitacao);
                    dialogo.dismiss();
                    onUpload = false;
                });
            });
        }
    }

    private void verificarConexao() {
        if (helper.conexaoAtivada()) {
            bt_offline.setVisibility(View.GONE);
            if (!onUpload)
                uploadsPendentes();
        } else bt_offline.setVisibility(View.VISIBLE);
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

        if (sharedPreferences.containsCategoria(Aviso.CATEGORIA_AVALIACAO)
                || sharedPreferences.containsCategoria(Aviso.CATEGORIA_TRAMITACAO))
            tab_overview.getTabAt(2).getOrCreateBadge().setVisible(true);

    }

    private void reautenticar() {
        if (helper.getUser() == null)
            chamarActivity(ActivityLogin.class);
        else if (helper.conexaoAtivada()) {
            if (!helper.getUser().isEmailVerified())
                chamarActivity(ActivityLogin.class);
            else
                helper.reautenticar();
        }
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

    public void trocarPagina(int index) {
        pager_overview.setCurrentItem(index, false);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
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
