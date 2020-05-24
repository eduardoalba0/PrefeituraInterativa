package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.SolicitacoesAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.FirebaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Usuario;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentInicio extends Fragment implements View.OnClickListener, View.OnFocusChangeListener, View.OnTouchListener {
    private FirebaseHelper helper;

    private boolean onUpload = false;

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

    @OnClick({R.id.bt_usuario, R.id.rv_solicitacoes})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_usuario:
                ActivityOverview activity = (ActivityOverview) getActivity();
                activity.trocarPagina(3);
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
        String jsonList = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("SolicitacoesPendentes", "");
        Gson gson = new Gson();

        if (jsonList.trim().equals("") || jsonList.trim().equals("[]"))
            card_pendentes.setVisibility(View.GONE);
        else {
            List<Solicitacao> listPendentes = new ArrayList<>();
            Collections.addAll(listPendentes, gson.fromJson(jsonList, Solicitacao[].class));
            card_pendentes.setVisibility(View.VISIBLE);
            rv_solicitacoes.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
            rv_solicitacoes.setAdapter(new SolicitacoesAdapter(getActivity(), listPendentes, getChildFragmentManager(), SolicitacoesAdapter.STYLE_PENDENTE));
        }
    }

    @BindView(R.id.img_usuario)
    ImageView img_usuario;

    @BindView(R.id.bt_usuario)
    TextView bt_usuario;

    @BindView(R.id.card_pendentes)
    MaterialCardView card_pendentes;

    @BindView(R.id.rv_solicitacoes)
    RecyclerView rv_solicitacoes;
}
