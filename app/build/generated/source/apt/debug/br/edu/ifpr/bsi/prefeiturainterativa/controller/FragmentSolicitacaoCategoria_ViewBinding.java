// Generated code from Butter Knife. Do not modify!
package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FragmentSolicitacaoCategoria_ViewBinding implements Unbinder {
  private FragmentSolicitacaoCategoria target;

  private View view7f0900e4;

  @UiThread
  public FragmentSolicitacaoCategoria_ViewBinding(final FragmentSolicitacaoCategoria target,
      View source) {
    this.target = target;

    View view;
    target.rv_departamentos = Utils.findRequiredViewAsType(source, R.id.rv_departamentos, "field 'rv_departamentos'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.l_footer, "field 'sliding_categorias' and method 'onClick'");
    target.sliding_categorias = Utils.castView(view, R.id.l_footer, "field 'sliding_categorias'", RelativeLayout.class);
    view7f0900e4 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.tv_numeroTopicos = Utils.findRequiredViewAsType(source, R.id.tv_numeroTopicos, "field 'tv_numeroTopicos'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FragmentSolicitacaoCategoria target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rv_departamentos = null;
    target.sliding_categorias = null;
    target.tv_numeroTopicos = null;

    view7f0900e4.setOnClickListener(null);
    view7f0900e4 = null;
  }
}
