// Generated code from Butter Knife. Do not modify!
package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FragmentInicio_ViewBinding implements Unbinder {
  private FragmentInicio target;

  private View view7f090069;

  @UiThread
  public FragmentInicio_ViewBinding(final FragmentInicio target, View source) {
    this.target = target;

    View view;
    target.img_usuario = Utils.findRequiredViewAsType(source, R.id.img_usuario, "field 'img_usuario'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.bt_usuario, "field 'bt_usuario' and method 'onClick'");
    target.bt_usuario = Utils.castView(view, R.id.bt_usuario, "field 'bt_usuario'", TextView.class);
    view7f090069 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    FragmentInicio target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.img_usuario = null;
    target.bt_usuario = null;

    view7f090069.setOnClickListener(null);
    view7f090069 = null;
  }
}
