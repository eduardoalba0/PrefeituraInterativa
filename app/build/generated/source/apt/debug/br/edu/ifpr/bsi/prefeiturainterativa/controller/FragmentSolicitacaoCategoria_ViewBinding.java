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

public class FragmentSolicitacaoCategoria_ViewBinding implements Unbinder {
  private FragmentSolicitacaoCategoria target;

  @UiThread
  public FragmentSolicitacaoCategoria_ViewBinding(FragmentSolicitacaoCategoria target,
      View source) {
    this.target = target;

    target.rv_departamentos = Utils.findRequiredViewAsType(source, R.id.rv_departamentos, "field 'rv_departamentos'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FragmentSolicitacaoCategoria target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rv_departamentos = null;
  }
}