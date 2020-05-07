package br.edu.ifpr.bsi.prefeiturainterativa.helpers.dialogs;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.adapters.GaleriaAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectorGaleria extends Fragment {

    public boolean resultadoUnico;

    public SelectorGaleria(boolean resultadoUnico) {
        this.resultadoUnico = resultadoUnico;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_selector_galeria, container, false);
        ButterKnife.bind(this, view);
        initRecyclerView();
        return view;
    }

    private void initRecyclerView() {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getActivity());
        layoutManager.setFlexDirection(FlexDirection.COLUMN_REVERSE);
        layoutManager.setJustifyContent(JustifyContent.FLEX_END);
        rv_galeria.setLayoutManager(layoutManager);
        rv_galeria.setAdapter(new GaleriaAdapter(getActivity(), getImagens(), getChildFragmentManager(), resultadoUnico));
    }

    private List<String> getImagens() {
        Cursor cursor;
        int index_imagem;
        ArrayList<String> imagens = new ArrayList<>();

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        cursor = getActivity().getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Images.Media.DATE_MODIFIED + " DESC");

        index_imagem = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        while (cursor.moveToNext())
            imagens.add(cursor.getString(index_imagem));
        cursor.close();
        return imagens;
    }

    @BindView(R.id.rv_galeria)
    RecyclerView rv_galeria;


}
