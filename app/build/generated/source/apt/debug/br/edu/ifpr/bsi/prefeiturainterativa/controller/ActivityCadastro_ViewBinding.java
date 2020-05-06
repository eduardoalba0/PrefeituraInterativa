// Generated code from Butter Knife. Do not modify!
package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.view.View;
import android.widget.Button;
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

public class ActivityCadastro_ViewBinding implements Unbinder {
  private ActivityCadastro target;

  private View view7f09005e;

  @UiThread
  public ActivityCadastro_ViewBinding(ActivityCadastro target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ActivityCadastro_ViewBinding(final ActivityCadastro target, View source) {
    this.target = target;

    View view;
    target.edt_nome = Utils.findRequiredViewAsType(source, R.id.edt_nome, "field 'edt_nome'", TextInputEditText.class);
    target.edt_cpf = Utils.findRequiredViewAsType(source, R.id.edt_cpf, "field 'edt_cpf'", TextInputEditText.class);
    target.edt_email = Utils.findRequiredViewAsType(source, R.id.edt_email, "field 'edt_email'", TextInputEditText.class);
    target.edt_senha = Utils.findRequiredViewAsType(source, R.id.edt_senha, "field 'edt_senha'", TextInputEditText.class);
    target.edt_senha_conf = Utils.findRequiredViewAsType(source, R.id.edt_senha_conf, "field 'edt_senha_conf'", TextInputEditText.class);
    target.edl_nome = Utils.findRequiredViewAsType(source, R.id.edl_nome, "field 'edl_nome'", TextInputLayout.class);
    target.edl_cpf = Utils.findRequiredViewAsType(source, R.id.edl_cpf, "field 'edl_cpf'", TextInputLayout.class);
    target.edl_email = Utils.findRequiredViewAsType(source, R.id.edl_email, "field 'edl_email'", TextInputLayout.class);
    target.edl_senha = Utils.findRequiredViewAsType(source, R.id.edl_senha, "field 'edl_senha'", TextInputLayout.class);
    target.edl_senha_conf = Utils.findRequiredViewAsType(source, R.id.edl_senha_conf, "field 'edl_senha_conf'", TextInputLayout.class);
    view = Utils.findRequiredView(source, R.id.bt_cadastrar, "field 'bt_cadastrar' and method 'onClick'");
    target.bt_cadastrar = Utils.castView(view, R.id.bt_cadastrar, "field 'bt_cadastrar'", Button.class);
    view7f09005e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.view_root = Utils.findRequiredView(source, R.id.view_root, "field 'view_root'");
    target.img_app = Utils.findRequiredView(source, R.id.img_app, "field 'img_app'");
    target.l_cadastro = Utils.findRequiredView(source, R.id.l_cadastro, "field 'l_cadastro'");
  }

  @Override
  @CallSuper
  public void unbind() {
    ActivityCadastro target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.edt_nome = null;
    target.edt_cpf = null;
    target.edt_email = null;
    target.edt_senha = null;
    target.edt_senha_conf = null;
    target.edl_nome = null;
    target.edl_cpf = null;
    target.edl_email = null;
    target.edl_senha = null;
    target.edl_senha_conf = null;
    target.bt_cadastrar = null;
    target.view_root = null;
    target.img_app = null;
    target.l_cadastro = null;

    view7f09005e.setOnClickListener(null);
    view7f09005e = null;
  }
}
