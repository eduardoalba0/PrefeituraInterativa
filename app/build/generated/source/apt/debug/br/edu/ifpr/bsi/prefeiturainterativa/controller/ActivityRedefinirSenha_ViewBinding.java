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

public class ActivityRedefinirSenha_ViewBinding implements Unbinder {
  private ActivityRedefinirSenha target;

  private View view7f090065;

  @UiThread
  public ActivityRedefinirSenha_ViewBinding(ActivityRedefinirSenha target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ActivityRedefinirSenha_ViewBinding(final ActivityRedefinirSenha target, View source) {
    this.target = target;

    View view;
    target.edt_email = Utils.findRequiredViewAsType(source, R.id.edt_email, "field 'edt_email'", TextInputEditText.class);
    target.edl_email = Utils.findRequiredViewAsType(source, R.id.edl_email, "field 'edl_email'", TextInputLayout.class);
    view = Utils.findRequiredView(source, R.id.bt_redefinir, "field 'bt_redefinir' and method 'onClick'");
    target.bt_redefinir = Utils.castView(view, R.id.bt_redefinir, "field 'bt_redefinir'", Button.class);
    view7f090065 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.view_root = Utils.findRequiredView(source, R.id.view_root, "field 'view_root'");
    target.img_app = Utils.findRequiredView(source, R.id.img_app, "field 'img_app'");
    target.l_redefinicao = Utils.findRequiredView(source, R.id.l_redefinicao, "field 'l_redefinicao'");
  }

  @Override
  @CallSuper
  public void unbind() {
    ActivityRedefinirSenha target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.edt_email = null;
    target.edl_email = null;
    target.bt_redefinir = null;
    target.view_root = null;
    target.img_app = null;
    target.l_redefinicao = null;

    view7f090065.setOnClickListener(null);
    view7f090065 = null;
  }
}
