package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Transition;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.OverviewTabAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.FirebaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.TransitionHelper;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityOverview extends FragmentActivity implements View.OnClickListener {

    private FirebaseHelper helper;
    private OverviewTabAdapter pageAdapter;

    @Override
    protected void onStart() {
        super.onStart();
        helper = new FirebaseHelper(this);
        if (helper.getUser() == null || !helper.getUser().isEmailVerified()) {
            Intent intent = new Intent(ActivityOverview.this, ActivityLogin.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        ButterKnife.bind(this, this);
        initTabLayout();
        startAnimation();
    }

    @OnClick(R.id.bt_foto)
    @Override
    public void onClick(View view) {
        chamarActivity(ActivitySolicitacaoCadastrar.class);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStack();
        else {
            finishAffinity();
        }
    }


    public <T> void chamarActivity(Class<T> activity) {
        Intent intent = new Intent(ActivityOverview.this, activity);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(ActivityOverview.this, bt_foto, "splash_transition");
        startActivity(intent, options.toBundle());
    }

    public void startAnimation() {
        Transition t = TransitionHelper.inflateChangeBoundsTransition(this);
        t.addListener(TransitionHelper.getCircularEnterTransitionListener(bt_foto, view_root, pager_overview));
        getWindow().setSharedElementEnterTransition(t);
    }

    public void initTabLayout() {
        pageAdapter = new OverviewTabAdapter(getSupportFragmentManager());
        pager_overview.setAdapter(pageAdapter);
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

    @BindView(R.id.bt_foto)
    FloatingActionButton bt_foto;

    @BindView(R.id.tab_overview)
    TabLayout tab_overview;

    @BindView(R.id.view_root)
    View view_root;

    @BindView(R.id.pager_overview)
    ViewPager pager_overview;

}
