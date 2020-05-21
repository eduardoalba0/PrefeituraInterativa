// Generated code from Butter Knife. Do not modify!
package br.edu.ifpr.bsi.prefeiturainterativa.adapters;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class GaleriaAdapter$ViewHolder_ViewBinding implements Unbinder {
  private GaleriaAdapter.ViewHolder target;

  private View view7f0900d9;

  @UiThread
  public GaleriaAdapter$ViewHolder_ViewBinding(final GaleriaAdapter.ViewHolder target,
      View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.img_galeria, "field 'img_galeria' and method 'onClick'");
    target.img_galeria = Utils.castView(view, R.id.img_galeria, "field 'img_galeria'", ImageView.class);
    view7f0900d9 = view;
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
    GaleriaAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.img_galeria = null;

    view7f0900d9.setOnClickListener(null);
    view7f0900d9 = null;
  }
}
