package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
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
import br.edu.ifpr.bsi.prefeiturainterativa.dao.Categorias_SolicitacaoDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.SolicitacaoDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.FirebaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Categoria;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Categorias_Solicitacao;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Usuario;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class FragmentInicio extends Fragment implements View.OnClickListener, View.OnFocusChangeListener, View.OnTouchListener {
    private Usuario usuario;
    private FirebaseHelper helper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        ButterKnife.bind(this, view);
        view.setOnTouchListener(this);
        view.setOnFocusChangeListener(this);
        usuario = new Usuario();
        helper = new FirebaseHelper(getActivity());
        preencherCampos();
        initRecyclerView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (helper.conexaoAtivada()) {
            uploadsPendentes();
        }
        initRecyclerView();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (helper.conexaoAtivada()) {
            uploadsPendentes();
        }
        initRecyclerView();
        return getActivity().onTouchEvent(motionEvent);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (helper.conexaoAtivada()) {
            uploadsPendentes();
        }
        initRecyclerView();
        getActivity().onWindowFocusChanged(b);
    }

    @OnClick(R.id.bt_usuario)
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
        usuario = new Usuario();
        usuario.set_ID(helper.getUser().getUid());
        bt_usuario.setText(helper.getUser().getDisplayName());
        if (helper.getUser().getPhotoUrl() != null)
            Glide.with(this)
                    .load(helper.getUser().getPhotoUrl())
                    .placeholder(R.drawable.ic_usuario)
                    .circleCrop()
                    .into(img_usuario);
        if (helper.conexaoAtivada()) {
            uploadsPendentes();
        }
    }

    private void uploadsPendentes() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String gsonListPendentes = preferences.getString("SolicitacoesPendentes", "");

        if (gsonListPendentes.trim().equals("") || gsonListPendentes.trim().equals("[]"))
            return;
        SweetAlertDialog dialogo = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText(R.string.str_carregando_demandas)
                .setCancelText(getString(R.string.str_cancelar));
        dialogo.show();

        Gson gson = new Gson();

        List<Solicitacao> listPendentes = new ArrayList<>();
        Collections.addAll(listPendentes, gson.fromJson(gsonListPendentes, Solicitacao[].class));

        for (Solicitacao solicitacao : listPendentes) {

            List<String> imagens = new ArrayList<>();
            List<Task<?>> uploadTasks = new ArrayList<>();

            for (String imagem : solicitacao.getLocalCaminhoImagens()) {
                Task<Uri> uploadTask = helper.carregarAnexos(Uri.parse(imagem));
                if (uploadTask != null) {
                    uploadTask.addOnSuccessListener(getActivity(), uri -> imagens.add(uri.toString()));
                    uploadTasks.add(uploadTask);
                }
            }
            Tasks.whenAllComplete(uploadTasks).addOnSuccessListener(getActivity(), o -> {
                solicitacao.setUrlImagens(imagens);
                List<Task<?>> insertTasks = new ArrayList<>();
                insertTasks.add(new SolicitacaoDAO(getActivity()).inserirAtualizar(solicitacao));
                for (Categoria categoria : solicitacao.getLocalCategorias()) {
                    Categorias_Solicitacao categorias_solicitacao = new Categorias_Solicitacao();
                    categorias_solicitacao.setSolicitacao_ID(solicitacao.get_ID());
                    categorias_solicitacao.setCategoria_ID(categoria.get_ID());
                    insertTasks.add(new Categorias_SolicitacaoDAO(getActivity()).inserirAtualizar(categorias_solicitacao));
                }
                Tasks.whenAllComplete(insertTasks).addOnSuccessListener(tasks -> {
                    List<Solicitacao> aux = new ArrayList<>();
                    Collections.addAll(listPendentes, gson.fromJson(gsonListPendentes, Solicitacao[].class));
                    aux.remove(solicitacao);
                    preferences.edit().remove("SolicitacoesPendentes")
                            .putString("SolicitacoesPendentes", gson.toJson(aux)).apply();
                    initRecyclerView();
                    dialogo.dismiss();
                });
            });
        }
    }

    private void initRecyclerView() {
        String gsonListPendentes = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("SolicitacoesPendentes", "");
        Gson gson = new Gson();

        if (gsonListPendentes.trim().equals("") || gsonListPendentes.trim().equals("[]"))
            card_pendentes.setVisibility(View.GONE);
        else {
            List<Solicitacao> listPendentes = new ArrayList<>();
            Collections.addAll(listPendentes, gson.fromJson(gsonListPendentes, Solicitacao[].class));
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
