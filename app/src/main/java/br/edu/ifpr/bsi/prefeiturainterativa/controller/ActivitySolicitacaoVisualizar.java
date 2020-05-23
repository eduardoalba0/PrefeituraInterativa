package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.os.Bundle;
import android.os.Handler;
import android.transition.Transition;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.SolicitacaoVisualizarTabAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.FirebaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.TransitionHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.shape.ShapeType;
import co.mobiwise.materialintro.view.MaterialIntroView;

public class ActivitySolicitacaoVisualizar extends FragmentActivity implements View.OnClickListener,
        ViewPager.OnPageChangeListener {

    public static final int STYLE_NORMAL = 0, STYLE_PENDENTE = 1;

    private int estilo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitacao_visualizar);
        ButterKnife.bind(this, this);
        verificarConexao();
        initTabLayout();
        startAnimation();
    }

    @OnClick(R.id.bt_offline)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_offline:
                verificarConexao();
                new MaterialIntroView.Builder(this)
                        .setShape(ShapeType.RECTANGLE)
                        .setUsageId("Info_Offline")
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
        if (new FirebaseHelper(this).conexaoAtivada())
            bt_offline.setVisibility(View.GONE);
        else bt_offline.setVisibility(View.VISIBLE);
    }

    public void initTabLayout() {
        Solicitacao solicitacao = (Solicitacao) getIntent().getSerializableExtra("Solicitacao");
        estilo = getIntent().getIntExtra("Estilo", STYLE_NORMAL);
        if (solicitacao == null) {
            onBackPressed();
            finish();
            return;
        }
        pager_solicitacao.setAdapter(new SolicitacaoVisualizarTabAdapter(getSupportFragmentManager(), solicitacao, estilo));
        tabs_solicitacao.setupWithViewPager(pager_solicitacao);
        tabs_solicitacao.getTabAt(0).setText(R.string.str_dados_solicitacao);
        tabs_solicitacao.getTabAt(1).setText(R.string.str_tramitacao);
        tabs_solicitacao.setEnabled(false);
        pager_solicitacao.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (estilo == STYLE_PENDENTE)
            if (position == 1)
                pager_solicitacao.setCurrentItem(0, false);
    }

    @Override
    public void onPageSelected(int position) {
        if (estilo == STYLE_PENDENTE)
            if (position == 1)
                pager_solicitacao.setCurrentItem(0, false);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void startAnimation() {
        Transition t = TransitionHelper.inflateExplodeTransition(1500);
        getWindow().setEnterTransition(t);
    }

    @BindView(R.id.pager_solicitacao)
    ViewPager pager_solicitacao;

    @BindView(R.id.tab_solicitacao)
    TabLayout tabs_solicitacao;

    @BindView(R.id.bt_offline)
    MaterialButton bt_offline;
}