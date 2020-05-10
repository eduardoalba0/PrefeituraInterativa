// Generated code from Butter Knife. Do not modify!
package br.edu.ifpr.bsi.prefeiturainterativa.helpers.dialogs;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SelectorGaleria_ViewBinding implements Unbinder {
  private SelectorGaleria target;

  @UiThread
  public SelectorGaleria_ViewBinding(SelectorGaleria target, View source) {
    this.target = target;

    target.rv_galeria = Utils.findRequiredViewAsType(source, R.id.rv_galeria, "field 'rv_galeria'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SelectorGaleria target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rv_galeria = null;
  }
}
