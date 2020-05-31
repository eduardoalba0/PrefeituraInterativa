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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ActivityLogin_ViewBinding implements Unbinder {
  private ActivityLogin target;

  private View view7f0a0056;

  private View view7f0a0057;

  private View view7f0a0059;

  private View view7f0a0051;

  @UiThread
  public ActivityLogin_ViewBinding(ActivityLogin target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ActivityLogin_ViewBinding(final ActivityLogin target, View source) {
    this.target = target;

    View view;
    target.edt_email = Utils.findRequiredViewAsType(source, R.id.edt_email, "field 'edt_email'", TextInputEditText.class);
    target.edt_senha = Utils.findRequiredViewAsType(source, R.id.edt_senha, "field 'edt_senha'", TextInputEditText.class);
    target.edl_email = Utils.findRequiredViewAsType(source, R.id.edl_email, "field 'edl_email'", TextInputLayout.class);
    target.edl_senha = Utils.findRequiredViewAsType(source, R.id.edl_senha, "field 'edl_senha'", TextInputLayout.class);
    view = Utils.findRequiredView(source, R.id.bt_login, "field 'bt_login' and method 'onClick'");
    target.bt_login = Utils.castView(view, R.id.bt_login, "field 'bt_login'", Button.class);
    view7f0a0056 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.bt_loginGoogle, "field 'bt_loginGoogle' and method 'onClick'");
    target.bt_loginGoogle = Utils.castView(view, R.id.bt_loginGoogle, "field 'bt_loginGoogle'", MaterialButton.class);
    view7f0a0057 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.bt_recuperarSenha, "field 'bt_recuperarSenha' and method 'onClick'");
    target.bt_recuperarSenha = Utils.castView(view, R.id.bt_recuperarSenha, "field 'bt_recuperarSenha'", MaterialButton.class);
    view7f0a0059 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.bt_cadastrar, "field 'bt_cadastrar' and method 'onClick'");
    target.bt_cadastrar = Utils.castView(view, R.id.bt_cadastrar, "field 'bt_cadastrar'", MaterialButton.class);
    view7f0a0051 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.view_root = Utils.findRequiredView(source, R.id.view_root, "field 'view_root'");
    target.img_app = Utils.findRequiredView(source, R.id.img_app, "field 'img_app'");
    target.l_login = Utils.findRequiredView(source, R.id.l_login, "field 'l_login'");
    target.l_footer = Utils.findRequiredView(source, R.id.l_footer, "field 'l_footer'");
  }

  @Override
  @CallSuper
  public void unbind() {
    ActivityLogin target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.edt_email = null;
    target.edt_senha = null;
    target.edl_email = null;
    target.edl_senha = null;
    target.bt_login = null;
    target.bt_loginGoogle = null;
    target.bt_recuperarSenha = null;
    target.bt_cadastrar = null;
    target.view_root = null;
    target.img_app = null;
    target.l_login = null;
    target.l_footer = null;

    view7f0a0056.setOnClickListener(null);
    view7f0a0056 = null;
    view7f0a0057.setOnClickListener(null);
    view7f0a0057 = null;
    view7f0a0059.setOnClickListener(null);
    view7f0a0059 = null;
    view7f0a0051.setOnClickListener(null);
    view7f0a0051 = null;
  }
}
