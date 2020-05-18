package br.edu.ifpr.bsi.prefeiturainterativa.helpers.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;
import butterknife.ButterKnife;
import permissions.dispatcher.RuntimePermissions;

public class SolicitacaoTramitacao extends Fragment {

    public Solicitacao solicitacao;

    public SolicitacaoTramitacao(Solicitacao solicitacao) {
        this.solicitacao = solicitacao;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_solicitacao_tramitacao, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

}
