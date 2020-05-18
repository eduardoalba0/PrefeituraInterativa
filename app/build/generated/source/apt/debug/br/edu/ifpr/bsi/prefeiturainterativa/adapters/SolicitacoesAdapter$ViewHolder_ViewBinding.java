// Generated code from Butter Knife. Do not modify!
package br.edu.ifpr.bsi.prefeiturainterativa.adapters;

import android.view.View;
import android.widget.ImageView;
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

public class SolicitacoesAdapter$ViewHolder_ViewBinding implements Unbinder {
  private SolicitacoesAdapter.ViewHolder target;

  private View view7f0900da;

  @UiThread
  public SolicitacoesAdapter$ViewHolder_ViewBinding(final SolicitacoesAdapter.ViewHolder target,
      View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.img_solicitacao, "field 'img_solicitacao' and method 'onClick'");
    target.img_solicitacao = Utils.castView(view, R.id.img_solicitacao, "field 'img_solicitacao'", ImageView.class);
    view7f0900da = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.tv_data = Utils.findRequiredViewAsType(source, R.id.tv_data, "field 'tv_data'", TextView.class);
    target.rv_categorias = Utils.findRequiredViewAsType(source, R.id.rv_categorias, "field 'rv_categorias'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SolicitacoesAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.img_solicitacao = null;
    target.tv_data = null;
    target.rv_categorias = null;

    view7f0900da.setOnClickListener(null);
    view7f0900da = null;
  }
}
