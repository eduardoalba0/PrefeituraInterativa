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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.CategoriasAdapter;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Departamento;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DialogCategorias extends BottomSheetDialogFragment {

    private Departamento departamento;
    private boolean editavel = true;

    public DialogCategorias(Departamento departamento) {
        this.departamento = departamento;
    }

    public DialogCategorias(Departamento departamento, boolean b) {
        this.departamento = departamento;
        this.editavel = b;
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
        initRecyclerView();
        return view;
    }

    private void initRecyclerView() {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getActivity());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.SPACE_EVENLY);
        tv_departamento.setText(departamento.getDescricao());
        tv_hintCategoria.setText(editavel ? R.string.str_solicitacao_tipo: R.string.str_categoria_hint);
        rv_categorias.setLayoutManager(layoutManager);
        rv_categorias.setAdapter(new CategoriasAdapter(getActivity(), departamento.getTiposSolicitacao(), editavel));
    }

    @BindView(R.id.rv_categorias)
    RecyclerView rv_categorias;

    @BindView(R.id.tv_departamento)
    TextView tv_departamento;

    @BindView(R.id.tv_hintCategoria)
    TextView tv_hintCategoria;

}
