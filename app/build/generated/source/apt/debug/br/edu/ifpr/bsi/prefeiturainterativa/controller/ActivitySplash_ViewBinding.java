// Generated code from Butter Knife. Do not modify!
package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ActivitySplash_ViewBinding implements Unbinder {
  private ActivitySplash target;

  @UiThread
  public ActivitySplash_ViewBinding(ActivitySplash target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ActivitySplash_ViewBinding(ActivitySplash target, View source) {
    this.target = target;

    target.img_app = Utils.findRequiredViewAsType(source, R.id.img_app, "field 'img_app'", ImageView.class);
    target.view_root = Utils.findRequiredView(source, R.id.view_root, "field 'view_root'");
  }

  @Override
  @CallSuper
  public void unbind() {
    ActivitySplash target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.img_app = null;
    target.view_root = null;
  }
}
