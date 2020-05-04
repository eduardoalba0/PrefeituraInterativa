package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.dao.UsuarioDAO;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.FirebaseHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Usuario;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentInicio extends Fragment implements View.OnClickListener{
    private Usuario usuario;
    private FirebaseHelper helper;
    private UsuarioDAO dao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        ButterKnife.bind(this, view);
        usuario = new Usuario();
        helper = new FirebaseHelper(getActivity());
        dao = new UsuarioDAO(getActivity());
        preencherCampos();
        return view;
    }

    @OnClick(R.id.bt_usuario)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_usuario:
                trocarPagina(3);
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
    }

    private void trocarPagina(int index) {
        ActivityOverview activity = (ActivityOverview)getActivity();
        activity.trocarPagina(index);
    }

    @BindView(R.id.img_usuario)
    ImageView img_usuario;

    @BindView(R.id.bt_usuario)
    TextView bt_usuario;
}
