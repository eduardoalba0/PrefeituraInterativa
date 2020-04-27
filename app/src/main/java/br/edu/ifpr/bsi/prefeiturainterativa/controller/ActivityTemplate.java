package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Transition;
import android.view.View;

import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.FirebaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.TransitionHelper;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityTemplate extends FragmentActivity {

    FirebaseHelper helper;

    @Override
    protected void onStart() {
        super.onStart();
        helper = new FirebaseHelper(this);
        if (helper.getUser() == null || !helper.getUser().isEmailVerified()) {
            Intent intent = new Intent(ActivityTemplate.this, UsuarioLoginActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);
        ButterKnife.bind(this, this);
        carregarFragment(new UsuarioPerfilActivity());
        startAnimation();
    }

    public void startAnimation() {
        Transition t = TransitionHelper.inflateChangeBoundsTransition(this);
        t.addListener(TransitionHelper.getCircularEnterTransitionListener(view_root, view_root, l_content));
        getWindow().setSharedElementEnterTransition(t);
    }

    public void carregarFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.l_content, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStack();
        else {
            getApplication();
            finishAffinity();
        }
    }

    @BindView(R.id.view_root)
    View view_root;

    @BindView(R.id.l_content)
    View l_content;
}
