// Generated code from Butter Knife. Do not modify!
package br.edu.ifpr.bsi.prefeiturainterativa.adapters;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.chip.Chip;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CategoriasAdapter$ViewHolder_ViewBinding implements Unbinder {
  private CategoriasAdapter.ViewHolder target;

  @UiThread
  public CategoriasAdapter$ViewHolder_ViewBinding(CategoriasAdapter.ViewHolder target,
      View source) {
    this.target = target;

    target.chip_solicitacao = Utils.findRequiredViewAsType(source, R.id.chip_solicitacao, "field 'chip_solicitacao'", Chip.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CategoriasAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.chip_solicitacao = null;
  }
}
