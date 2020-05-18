// Generated code from Butter Knife. Do not modify!
package br.edu.ifpr.bsi.prefeiturainterativa.helpers.dialogs;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DialogCategorias_ViewBinding implements Unbinder {
  private DialogCategorias target;

  @UiThread
  public DialogCategorias_ViewBinding(DialogCategorias target, View source) {
    this.target = target;

    target.rv_categorias = Utils.findRequiredViewAsType(source, R.id.rv_categorias, "field 'rv_categorias'", RecyclerView.class);
    target.tv_departamento = Utils.findRequiredViewAsType(source, R.id.tv_departamento, "field 'tv_departamento'", TextView.class);
    target.tv_hintCategoria = Utils.findRequiredViewAsType(source, R.id.tv_hintCategoria, "field 'tv_hintCategoria'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    DialogCategorias target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rv_categorias = null;
    target.tv_departamento = null;
    target.tv_hintCategoria = null;
  }
}