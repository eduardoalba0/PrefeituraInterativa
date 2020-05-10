// Generated code from Butter Knife. Do not modify!
package br.edu.ifpr.bsi.prefeiturainterativa.adapters;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CategoriasDepartamentoAdapter$ViewHolder_ViewBinding implements Unbinder {
  private CategoriasDepartamentoAdapter.ViewHolder target;

  private View view7f0901b2;

  @UiThread
  public CategoriasDepartamentoAdapter$ViewHolder_ViewBinding(
      final CategoriasDepartamentoAdapter.ViewHolder target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.tv_departamento, "field 'tv_departamento' and method 'onClick'");
    target.tv_departamento = Utils.castView(view, R.id.tv_departamento, "field 'tv_departamento'", TextView.class);
    view7f0901b2 = view;
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
    CategoriasDepartamentoAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tv_departamento = null;

    view7f0901b2.setOnClickListener(null);
    view7f0901b2 = null;
  }
}
