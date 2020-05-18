// Generated code from Butter Knife. Do not modify!
package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FragmentTabSolicitacoes_ViewBinding implements Unbinder {
  private FragmentTabSolicitacoes target;

  @UiThread
  public FragmentTabSolicitacoes_ViewBinding(FragmentTabSolicitacoes target, View source) {
    this.target = target;

    target.rv_solicitacoes = Utils.findRequiredViewAsType(source, R.id.rv_solicitacoes, "field 'rv_solicitacoes'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FragmentTabSolicitacoes target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rv_solicitacoes = null;
  }
}
