package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.AvisoAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.SolicitacoesAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.SolicitacaoDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.FirebaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.SharedPreferencesHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Aviso;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Usuario;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentInicio extends Fragment implements View.OnClickListener, View.OnFocusChangeListener, View.OnTouchListener {
    private FirebaseHelper helper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        ButterKnife.bind(this, view);
        view.setOnTouchListener(this);
        view.setOnFocusChangeListener(this);
        helper = new FirebaseHelper(getActivity());
        preencherCampos();
        initRecyclerView();
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (helper.conexaoAtivada())
            initRecyclerView();
        return getActivity().onTouchEvent(motionEvent);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (helper.conexaoAtivada())
            initRecyclerView();
        getActivity().onWindowFocusChanged(b);
    }

    @OnClick({R.id.bt_usuario, R.id.l_numSolicitacoes})
    @Override
    public void onClick(View view) {
        ActivityOverview activity = (ActivityOverview) getActivity();
        switch (view.getId()) {
            case R.id.bt_usuario:
                activity.trocarPagina(3);
                break;
            case R.id.l_numSolicitacoes:
                activity.trocarPagina(2);
                break;
        }
    }

    private void preencherCampos() {
        Usuario usuario = new Usuario();
        usuario.set_ID(helper.getUser().getUid());
        bt_usuario.setText(helper.getUser().getDisplayName());
        if (helper.getUser().getPhotoUrl() != null)
            Glide.with(this)
                    .load(helper.getUser().getPhotoUrl())
                    .placeholder(R.drawable.ic_usuario)
                    .circleCrop()
                    .into(img_usuario);
    }

    private void initRecyclerView() {
        SharedPreferencesHelper sharedPreferences = new SharedPreferencesHelper(getActivity());
        List<Solicitacao> listPendentes = sharedPreferences.getSolicitaoes();
        List<Aviso> listAvisos = sharedPreferences.getAvisos();
        if (listPendentes.size() <= 0)
            card_pendentes.setVisibility(View.GONE);
        else {
            card_pendentes.setVisibility(View.VISIBLE);
            rv_solicitacoes.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
            rv_solicitacoes.setAdapter(new SolicitacoesAdapter(getActivity(), listPendentes, Solicitacao.STYLE_PENDENTE));
        }

        if (listAvisos.size() <= 0)
            tv_nenhumAviso.setVisibility(View.VISIBLE);
        else {
            tv_nenhumAviso.setVisibility(View.GONE);
            rv_avisos.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
            rv_avisos.setAdapter(new AvisoAdapter(getActivity(), listAvisos));
        }

        new SolicitacaoDAO(getActivity()).getAllporStatus(false)
                .addOnSuccessListener(getActivity(), queryDocumentSnapshots ->
                        tv_numAbertas.setText(("" + (queryDocumentSnapshots.toObjects(Solicitacao.class).size()))));

        new SolicitacaoDAO(getActivity()).getAllporStatus(true)
                .addOnSuccessListener(getActivity(), queryDocumentSnapshots ->
                        tv_numConcluidas.setText(("" + queryDocumentSnapshots.toObjects(Solicitacao.class).size())));
    }

    @BindView(R.id.img_usuario)
    ImageView img_usuario;

    @BindView(R.id.bt_usuario)
    TextView bt_usuario;

    @BindView(R.id.tv_nenhumAviso)
    TextView tv_nenhumAviso;

    @BindView(R.id.tv_numAbertas)
    TextView tv_numAbertas;

    @BindView(R.id.l_numSolicitacoes)
    FlexboxLayout l_numSolicitacoes;

    @BindView(R.id.tv_numConcluidas)
    TextView tv_numConcluidas;

    @BindView(R.id.card_pendentes)
    MaterialCardView card_pendentes;

    @BindView(R.id.rv_solicitacoes)
    RecyclerView rv_solicitacoes;

    @BindView(R.id.rv_avisos)
    RecyclerView rv_avisos;
}
