// Generated code from Butter Knife. Do not modify!
package br.edu.ifpr.bsi.prefeiturainterativa.helpers.dialogs;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DialogViewer_ViewBinding implements Unbinder {
  private DialogViewer target;

  private View view7f090058;

  private View view7f090062;

  @UiThread
  public DialogViewer_ViewBinding(final DialogViewer target, View source) {
    this.target = target;

    View view;
    target.img_viewer = Utils.findRequiredViewAsType(source, R.id.img_viewer, "field 'img_viewer'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.bt_aceitar, "field 'bt_aceitar' and method 'onClick'");
    target.bt_aceitar = Utils.castView(view, R.id.bt_aceitar, "field 'bt_aceitar'", FloatingActionButton.class);
    view7f090058 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.bt_recusar, "field 'bt_recusar' and method 'onClick'");
    target.bt_recusar = Utils.castView(view, R.id.bt_recusar, "field 'bt_recusar'", FloatingActionButton.class);
    view7f090062 = view;
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
    DialogViewer target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.img_viewer = null;
    target.bt_aceitar = null;
    target.bt_recusar = null;

    view7f090058.setOnClickListener(null);
    view7f090058 = null;
    view7f090062.setOnClickListener(null);
    view7f090062 = null;
  }
}
