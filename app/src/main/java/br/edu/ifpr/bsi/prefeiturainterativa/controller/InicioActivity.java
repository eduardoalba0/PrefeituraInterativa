package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import butterknife.ButterKnife;

public class InicioActivity extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_inicio, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
