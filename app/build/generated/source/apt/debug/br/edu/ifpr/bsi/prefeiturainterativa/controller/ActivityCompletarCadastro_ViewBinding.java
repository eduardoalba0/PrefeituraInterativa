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

public class ActivityCompletarCadastro_ViewBinding implements Unbinder {
  private ActivityCompletarCadastro target;

  private View view7f0a0051;

  private View view7f0a005d;

  @UiThread
  public ActivityCompletarCadastro_ViewBinding(ActivityCompletarCadastro target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ActivityCompletarCadastro_ViewBinding(final ActivityCompletarCadastro target,
      View source) {
    this.target = target;

    View view;
    target.edt_cpf = Utils.findRequiredViewAsType(source, R.id.edt_cpf, "field 'edt_cpf'", TextInputEditText.class);
    target.edl_cpf = Utils.findRequiredViewAsType(source, R.id.edl_cpf, "field 'edl_cpf'", TextInputLayout.class);
    view = Utils.findRequiredView(source, R.id.bt_cadastrar, "field 'bt_cadastrar' and method 'onClick'");
    target.bt_cadastrar = Utils.castView(view, R.id.bt_cadastrar, "field 'bt_cadastrar'", Button.class);
    view7f0a0051 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.bt_sair, "field 'bt_sair' and method 'onClick'");
    target.bt_sair = Utils.castView(view, R.id.bt_sair, "field 'bt_sair'", Button.class);
    view7f0a005d = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.tv_usuario = Utils.findRequiredViewAsType(source, R.id.tv_usuario, "field 'tv_usuario'", TextView.class);
    target.view_root = Utils.findRequiredView(source, R.id.view_root, "field 'view_root'");
    target.img_app = Utils.findRequiredViewAsType(source, R.id.img_app, "field 'img_app'", ImageView.class);
    target.l_completar = Utils.findRequiredView(source, R.id.l_completar, "field 'l_completar'");
    target.shape_irregular = Utils.findRequiredView(source, R.id.shape_irregular, "field 'shape_irregular'");
  }

  @Override
  @CallSuper
  public void unbind() {
    ActivityCompletarCadastro target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.edt_cpf = null;
    target.edl_cpf = null;
    target.bt_cadastrar = null;
    target.bt_sair = null;
    target.tv_usuario = null;
    target.view_root = null;
    target.img_app = null;
    target.l_completar = null;
    target.shape_irregular = null;

    view7f0a0051.setOnClickListener(null);
    view7f0a0051 = null;
    view7f0a005d.setOnClickListener(null);
    view7f0a005d = null;
  }
}
