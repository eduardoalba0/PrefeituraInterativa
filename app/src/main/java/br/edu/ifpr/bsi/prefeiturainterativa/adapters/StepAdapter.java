package br.edu.ifpr.bsi.prefeiturainterativa.adapters;

import android.content.Context;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import br.edu.ifpr.bsi.prefeiturainterativa.controller.FragmentSolicitacao1;

public class StepAdapter extends AbstractFragmentStepAdapter {

    public StepAdapter(FragmentManager fm, Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {
        switch (position) {
            case 0:
                return new FragmentSolicitacao1();
            case 1:
                return new FragmentSolicitacao1();
            case 2:
                return new FragmentSolicitacao1();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(int position) {
        String title = "";
        String subtitle = "";

        switch (position) {
            case 0:
                title = "Descrição";
                subtitle = "da solicitação";
                break;
            case 1:
                title = "Localização";
                subtitle = "do ocorrido";
                break;
            case 2:
                title = "Entidade Responsável";
                subtitle = "pela resposta";
                break;
        }
        return new StepViewModel.Builder(context)
                .setTitle(title)
                .setSubtitle(subtitle)
                .create();
    }
}
