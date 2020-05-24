package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.SolicitacaoDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Avaliacao;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class FragmentSolicitacaoAvaliacao extends Fragment implements View.OnClickListener {

    private Solicitacao solicitacao;
    private Avaliacao avaliacao;

    public FragmentSolicitacaoAvaliacao(Solicitacao solicitacao) {
        this.solicitacao = solicitacao;
        this.avaliacao = solicitacao.getAvaliacao();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solicitacao_avaliacao, container, false);
        ButterKnife.bind(this, view);
        carregarDados();
        return view;
    }

    @OnClick(R.id.bt_avaliar)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_avaliar:
                salvarAvaliacao();
                break;
        }
    }

    private void carregarDados() {
        if (solicitacao.isAvaliada() || !solicitacao.isConcluida()) {
            bar_avaliacao.setEnabled(false);
            tv_solucionada.setEnabled(false);
            sw_solucionada.setEnabled(false);
            edl_comentario.setEnabled(false);
            edt_comentario.setEnabled(false);
            bt_avaliar.setEnabled(false);
        }
        if (solicitacao.isAvaliada()) {
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, new Locale("pt", "BR"));
            bar_avaliacao.setRating(avaliacao.getNota());
            sw_solucionada.setChecked(avaliacao.isSolucionada());
            edt_comentario.setText(avaliacao.getComentario());
            tv_data.setVisibility(View.VISIBLE);
            tv_data.setText(("Avaliação cadastrada em :" + df.format(avaliacao.getData())));
        }
    }

    public void salvarAvaliacao() {
        avaliacao.setComentario(edt_comentario.getText().toString());
        avaliacao.setNota(bar_avaliacao.getRating());
        avaliacao.setSolucionada(sw_solucionada.isChecked());
        solicitacao.setAvaliacao(avaliacao);

        new SolicitacaoDAO(getActivity()).inserirAtualizar(solicitacao)
                .addOnSuccessListener(getActivity(), aVoid ->
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText(R.string.str_sucesso)
                                .setContentText(getResources().getString(R.string.str_avaliacao_concluida))
                                .setConfirmText(getResources().getString(R.string.dialog_ok))
                                .setConfirmClickListener(sweetAlertDialog -> chamarActivity(ActivityOverview.class)));
    }

    private <T> void chamarActivity(Class<T> activity) {
        Intent intent = new Intent(getActivity(), activity);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(getActivity(), bt_avaliar, "splash_transition");
        startActivity(intent, options.toBundle());
    }

    @BindView(R.id.bar_avaliacao)
    RatingBar bar_avaliacao;

    @BindView(R.id.tv_solucionada)
    TextView tv_solucionada;

    @BindView(R.id.tv_data)
    TextView tv_data;

    @BindView(R.id.sw_solucionada)
    SwitchMaterial sw_solucionada;

    @BindView(R.id.edt_comentario)
    TextInputEditText edt_comentario;

    @BindView(R.id.edl_comentario)
    TextInputLayout edl_comentario;

    @BindView(R.id.bt_avaliar)
    MaterialButton bt_avaliar;
}
