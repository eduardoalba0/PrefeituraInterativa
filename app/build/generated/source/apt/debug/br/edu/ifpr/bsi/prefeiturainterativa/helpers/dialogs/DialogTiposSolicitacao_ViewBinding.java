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

public class DialogTiposSolicitacao_ViewBinding implements Unbinder {
  private DialogTiposSolicitacao target;

  @UiThread
  public DialogTiposSolicitacao_ViewBinding(DialogTiposSolicitacao target, View source) {
    this.target = target;

    target.rv_categorias = Utils.findRequiredViewAsType(source, R.id.rv_categorias, "field 'rv_categorias'", RecyclerView.class);
    target.tv_departamento = Utils.findRequiredViewAsType(source, R.id.tv_departamento, "field 'tv_departamento'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    DialogTiposSolicitacao target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rv_categorias = null;
    target.tv_departamento = null;
  }
}
