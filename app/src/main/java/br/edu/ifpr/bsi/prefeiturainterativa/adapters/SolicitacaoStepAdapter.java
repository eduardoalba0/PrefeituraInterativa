package br.edu.ifpr.bsi.prefeiturainterativa.adapters;

import android.content.Context;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

public class SolicitacaoStepAdapter extends AbstractFragmentStepAdapter {

    List<Step> fragments;
    List<String> viewModels;

    public SolicitacaoStepAdapter(FragmentManager fm, Context context) {
        super(fm, context);
        fragments = new ArrayList<>();
        viewModels = new ArrayList<>();
    }

    @Override
    public Step createStep(int position) {
        if (position < fragments.size())
            return fragments.get(position);
        return null;
    }

    public void addFragment(Step step, String title) {
        fragments.add(step);
        viewModels.add(title);
    }
    @Override
    public int getCount() {
        return fragments.size();
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(int position) {
        String title = viewModels.get(position).split("\n")[0];
        String subtitle = viewModels.get(position).split("\n")[1];

        return new StepViewModel.Builder(context)
                .setTitle(title)
                .setSubtitle(subtitle)
                .create();
    }
}
