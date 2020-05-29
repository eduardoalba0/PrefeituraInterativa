package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.CategoriasAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.GaleriaAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Departamento;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

import static br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao.STYLE_NORMAL;
import static br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao.STYLE_PENDENTE;

public class FragmentSolicitacaoDados extends Fragment implements View.OnClickListener, View.OnTouchListener {

    private Solicitacao solicitacao;
    private int estilo;

    public FragmentSolicitacaoDados(Solicitacao solicitacao) {
        this.solicitacao = solicitacao;
        this.estilo = STYLE_NORMAL;
    }

    public FragmentSolicitacaoDados(Solicitacao solicitacao, int estilo) {
        this.solicitacao = solicitacao;
        this.estilo = estilo;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solicitacao_dados, container, false);
        ButterKnife.bind(this, view);
        initRecyclerView();
        carregarDados();
        return view;
    }

    @OnClick(R.id.bt_localizacao)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_localizacao:
                Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/search/"
                        + solicitacao.getLocalizacao().getLatitude()
                        + "," + solicitacao.getLocalizacao().getLongitude());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                break;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @OnTouch(R.id.rv_categorias)
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        ((ViewGroup) getView()).requestDisallowInterceptTouchEvent(true);
        return false;
    }

    private void initRecyclerView() {
        FlexboxLayoutManager categoriasLayoutManager = new FlexboxLayoutManager(getActivity());
        categoriasLayoutManager.setFlexDirection(FlexDirection.ROW);
        categoriasLayoutManager.setJustifyContent(JustifyContent.SPACE_EVENLY);
        rv_categorias.setLayoutManager(categoriasLayoutManager);

        FlexboxLayoutManager imagensLayoutManager = new FlexboxLayoutManager(getActivity());
        imagensLayoutManager.setFlexDirection(FlexDirection.COLUMN);
        imagensLayoutManager.setJustifyContent(JustifyContent.CENTER);
        rv_imagens.setLayoutManager(imagensLayoutManager);
    }

    private void carregarDados() {
        switch (estilo) {
            case STYLE_NORMAL:
                if (solicitacao.getUrlImagens() == null || solicitacao.getUrlImagens().isEmpty())
                    l_imagens.setVisibility(View.GONE);
                else
                    rv_imagens.setAdapter(new GaleriaAdapter(getActivity(), solicitacao.getUrlImagens(), getChildFragmentManager()));
                break;
            case STYLE_PENDENTE:
                if (solicitacao.getLocalCaminhoImagens() == null || solicitacao.getLocalCaminhoImagens().isEmpty())
                    l_imagens.setVisibility(View.GONE);
                else
                    rv_imagens.setAdapter(new GaleriaAdapter(getActivity(), solicitacao.getLocalCaminhoImagens(), getChildFragmentManager()));
                break;
        }
        if (solicitacao.getDescricao() == null || solicitacao.getDescricao().trim().equals(""))
            edl_descricao.setVisibility(View.GONE);
        else
            edt_descricao.setText(solicitacao.getDescricao());
        rv_categorias.setAdapter(new CategoriasAdapter(getActivity(), solicitacao.getLocalCategorias(), false, Departamento.STYLE_RED));
    }

    @BindView(R.id.rv_categorias)
    RecyclerView rv_categorias;

    @BindView(R.id.rv_imagens)
    RecyclerView rv_imagens;

    @BindView(R.id.edt_descricao)
    TextInputEditText edt_descricao;

    @BindView(R.id.edl_descricao)
    TextInputLayout edl_descricao;

    @BindView(R.id.bt_localizacao)
    MaterialButton bt_localizacao;

    @BindView(R.id.l_imagens)
    LinearLayout l_imagens;
}
