package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.AnexosAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.CategoriasAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentSolicitacaoDados extends Fragment implements View.OnClickListener {

    public Solicitacao solicitacao;

    public FragmentSolicitacaoDados(Solicitacao solicitacao) {
        this.solicitacao = solicitacao;
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
                        + solicitacao.getLatitude()
                        + "," + solicitacao.getLongitude());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                break;
        }
    }

    private void initRecyclerView() {
        FlexboxLayoutManager categoriasLayoutManager = new FlexboxLayoutManager(getActivity());
        categoriasLayoutManager.setFlexDirection(FlexDirection.ROW);
        categoriasLayoutManager.setJustifyContent(JustifyContent.SPACE_EVENLY);
        rv_categorias.setLayoutManager(categoriasLayoutManager);

        FlexboxLayoutManager imagensLayoutManager = new FlexboxLayoutManager(getActivity());
        imagensLayoutManager.setFlexDirection(FlexDirection.ROW);
        imagensLayoutManager.setJustifyContent(JustifyContent.SPACE_EVENLY);
        rv_imagens.setLayoutManager(imagensLayoutManager);
    }

    private void carregarDados() {
        edt_descricao.setText(solicitacao.getDescricao());
        rv_categorias.setAdapter(new CategoriasAdapter(getActivity(), solicitacao.getLocalCategorias(), false));
        rv_imagens.setAdapter(new AnexosAdapter(getActivity(), solicitacao.getUrlImagens(), getChildFragmentManager(), false));
    }

    @BindView(R.id.rv_categorias)
    RecyclerView rv_categorias;

    @BindView(R.id.rv_imagens)
    RecyclerView rv_imagens;

    @BindView(R.id.edt_descricao)
    TextInputEditText edt_descricao;

    @BindView(R.id.bt_localizacao)
    MaterialButton bt_localizacao;
}
