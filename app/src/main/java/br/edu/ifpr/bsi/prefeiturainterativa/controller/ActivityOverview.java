package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.transition.Transition;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.shape.ShapeType;
import co.mobiwise.materialintro.view.MaterialIntroView;

public class ActivityOverview extends FragmentActivity implements View.OnClickListener {

    FirebaseHelper helper;

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
                new MaterialIntroView.Builder(this)
                        .setShape(ShapeType.RECTANGLE)
                        .setInfoText("As solicitações que você cadastrar ficarão armazenadas no dispositivo até você e conectar à internet.")
                        .enableDotAnimation(false)
                        .enableFadeAnimation(true)
                        .setFocusGravity(FocusGravity.CENTER)
                        .setFocusType(Focus.NORMAL)
                        .performClick(true)
                        .setTarget(bt_offline)
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
        helper = new FirebaseHelper(this);
        if (helper.getUser() == null || !helper.getUser().isEmailVerified()) {
            chamarActivity(ActivityLogin.class);
            return;
        }
        if (helper.conexaoAtivada()) {
            bt_offline.setVisibility(View.GONE);
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
