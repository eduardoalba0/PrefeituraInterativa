// Generated code from Butter Knife. Do not modify!
package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FragmentPerfil_ViewBinding implements Unbinder {
  private FragmentPerfil target;

  private View view7f0900da;

  private View view7f090066;

  private View view7f090064;

  private View view7f09005c;

  @UiThread
  public FragmentPerfil_ViewBinding(final FragmentPerfil target, View source) {
    this.target = target;

    View view;
    target.edt_nome = Utils.findRequiredViewAsType(source, R.id.edt_nome, "field 'edt_nome'", TextInputEditText.class);
    target.edt_cpf = Utils.findRequiredViewAsType(source, R.id.edt_cpf, "field 'edt_cpf'", TextInputEditText.class);
    target.edt_email = Utils.findRequiredViewAsType(source, R.id.edt_email, "field 'edt_email'", TextInputEditText.class);
    view = Utils.findRequiredView(source, R.id.img_usuario, "field 'img_usuario' and method 'onClick'");
    target.img_usuario = Utils.castView(view, R.id.img_usuario, "field 'img_usuario'", ImageView.class);
    view7f0900da = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.tv_usuario = Utils.findRequiredViewAsType(source, R.id.tv_usuario, "field 'tv_usuario'", TextView.class);
    view = Utils.findRequiredView(source, R.id.bt_sair, "field 'bt_sair' and method 'onClick'");
    target.bt_sair = Utils.castView(view, R.id.bt_sair, "field 'bt_sair'", Button.class);
    view7f090066 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.bt_redefinir, "field 'bt_redefinir' and method 'onClick'");
    target.bt_redefinir = Utils.castView(view, R.id.bt_redefinir, "field 'bt_redefinir'", Button.class);
    view7f090064 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.bt_atualizar, "field 'bt_atualizar' and method 'onClick'");
    target.bt_atualizar = Utils.castView(view, R.id.bt_atualizar, "field 'bt_atualizar'", Button.class);
    view7f09005c = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.view_root = Utils.findRequiredView(source, R.id.view_root, "field 'view_root'");
    target.l_perfil = Utils.findRequiredView(source, R.id.l_perfil, "field 'l_perfil'");
    target.edl_nome = Utils.findRequiredViewAsType(source, R.id.edl_nome, "field 'edl_nome'", TextInputLayout.class);
    target.edl_cpf = Utils.findRequiredViewAsType(source, R.id.edl_cpf, "field 'edl_cpf'", TextInputLayout.class);
    target.edl_email = Utils.findRequiredViewAsType(source, R.id.edl_email, "field 'edl_email'", TextInputLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FragmentPerfil target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.edt_nome = null;
    target.edt_cpf = null;
    target.edt_email = null;
    target.img_usuario = null;
    target.tv_usuario = null;
    target.bt_sair = null;
    target.bt_redefinir = null;
    target.bt_atualizar = null;
    target.view_root = null;
    target.l_perfil = null;
    target.edl_nome = null;
    target.edl_cpf = null;
    target.edl_email = null;

    view7f0900da.setOnClickListener(null);
    view7f0900da = null;
    view7f090066.setOnClickListener(null);
    view7f090066 = null;
    view7f090064.setOnClickListener(null);
    view7f090064 = null;
    view7f09005c.setOnClickListener(null);
    view7f09005c = null;
  }
}
