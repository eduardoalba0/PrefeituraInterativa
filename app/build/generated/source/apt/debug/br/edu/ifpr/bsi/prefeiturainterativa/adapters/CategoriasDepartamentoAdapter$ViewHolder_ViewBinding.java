// Generated code from Butter Knife. Do not modify!
package br.edu.ifpr.bsi.prefeiturainterativa.adapters;

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

public class CategoriasDepartamentoAdapter$ViewHolder_ViewBinding implements Unbinder {
  private CategoriasDepartamentoAdapter.ViewHolder target;

  @UiThread
  public CategoriasDepartamentoAdapter$ViewHolder_ViewBinding(
      CategoriasDepartamentoAdapter.ViewHolder target, View source) {
    this.target = target;

    target.tv_departamento = Utils.findRequiredViewAsType(source, R.id.tv_departamento, "field 'tv_departamento'", TextView.class);
    target.rv_tipoSolicitacao = Utils.findRequiredViewAsType(source, R.id.rv_tipoSolicitacao, "field 'rv_tipoSolicitacao'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CategoriasDepartamentoAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tv_departamento = null;
    target.rv_tipoSolicitacao = null;
  }
}
