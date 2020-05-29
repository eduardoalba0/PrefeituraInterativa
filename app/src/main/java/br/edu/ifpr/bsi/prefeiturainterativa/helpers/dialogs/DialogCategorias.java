package br.edu.ifpr.bsi.prefeiturainterativa.helpers.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.CategoriasAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.ViewModelsHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Departamento;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DialogCategorias extends BottomSheetDialogFragment {

    public static final int TYPE_EDITABLE = 0, TYPE_ONLYVIEW = 1, TYPE_LIST_EDITABLE = 2;

    private Departamento departamento;
    private ViewModelsHelper viewModel;
    private int style;
    private int tipo;

    public DialogCategorias() {
        this.style = Departamento.STYLE_RED;
        this.tipo = TYPE_LIST_EDITABLE;
    }

    public DialogCategorias(Departamento departamento, boolean b, int style) {
        this.departamento = departamento;
        this.style = style;
        this.tipo = b ? TYPE_EDITABLE : TYPE_ONLYVIEW;
    }


    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(windowParams);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_categorias, container, false);
        ButterKnife.bind(this, view);
        initDialog();
        return view;
    }

    private void initDialog() {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getActivity());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.SPACE_EVENLY);
        rv_categorias.setLayoutManager(layoutManager);

        switch (style) {
            case Departamento.STYLE_RED:
                tv_hintCategoria.setTextColor(getContext().getResources().getColor(R.color.colorRed));
                bt_continuar.setIconTintResource(R.color.colorRed);
                bt_continuar.setTextColor(getResources().getColor(R.color.colorRed));
                break;
            case Departamento.STYLE_BLUE:
                tv_hintCategoria.setTextColor(getContext().getResources().getColor(R.color.colorDarkBlueWhite));
                bt_continuar.setIconTintResource(R.color.colorDarkBlueWhite);
                bt_continuar.setTextColor(getResources().getColor(R.color.colorDarkBlueWhite));
                break;
            case Departamento.STYLE_GREEN:
                tv_hintCategoria.setTextColor(getContext().getResources().getColor(R.color.colorDarkGreenWhite));
                bt_continuar.setIconTintResource(R.color.colorDarkGreenWhite);
                bt_continuar.setTextColor(getResources().getColor(R.color.colorDarkGreenWhite));
                break;
        }

        switch (tipo) {
            case TYPE_EDITABLE:
                tv_titulo.setText(departamento.getDescricao());
                tv_hintCategoria.setVisibility(View.VISIBLE);
                tv_hintCategoria.setText(R.string.str_solicitacao_tipo);
                rv_categorias.setAdapter(new CategoriasAdapter(getActivity(), departamento.getLocalCategorias(), true, style));
                bt_continuar.setOnClickListener(view -> this.dismiss());
                break;
            case TYPE_ONLYVIEW:
                bt_continuar.setVisibility(View.GONE);
                tv_titulo.setText(departamento.getDescricao());
                tv_hintCategoria.setVisibility(View.VISIBLE);
                tv_hintCategoria.setText(R.string.str_categoria_hint);
                rv_categorias.setAdapter(new CategoriasAdapter(getActivity(), departamento.getLocalCategorias(), false, style));
                break;
            case TYPE_LIST_EDITABLE:
                bt_continuar.setOnClickListener(view -> this.dismiss());
                viewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(ViewModelsHelper.class);
                viewModel.getCategorias().observe(getActivity(), categorias -> {
                    if (categorias.isEmpty()) {
                        tv_titulo.setText(R.string.str_topicos_selecionados);
                        tv_hintCategoria.setVisibility(View.VISIBLE);
                        tv_hintCategoria.setText(R.string.str_topicos_vazios);
                    } else {
                        tv_titulo.setText(R.string.str_topicos_selecionados);
                        tv_hintCategoria.setVisibility(View.GONE);
                    }
                    rv_categorias.setAdapter(new CategoriasAdapter(getActivity(), categorias, true, style));
                });
        }
    }

    @BindView(R.id.rv_categorias)
    RecyclerView rv_categorias;

    @BindView(R.id.tv_titulo)
    TextView tv_titulo;

    @BindView(R.id.tv_hintCategoria)
    TextView tv_hintCategoria;

    @BindView(R.id.bt_continuar)
    MaterialButton bt_continuar;

}
