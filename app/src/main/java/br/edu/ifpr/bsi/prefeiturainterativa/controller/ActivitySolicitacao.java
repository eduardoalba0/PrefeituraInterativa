package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.os.Bundle;
import android.view.View;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.StepAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivitySolicitacao extends FragmentActivity implements StepperLayout.StepperListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitacao);
        ButterKnife.bind(this, this);
        stepperLayout.setAdapter(new StepAdapter(getSupportFragmentManager(), this));

    }

    @Override
    public void onCompleted(View completeButton) {

    }

    @Override
    public void onError(VerificationError verificationError) {

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
