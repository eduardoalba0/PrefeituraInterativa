// Generated code from Butter Knife. Do not modify!
package br.edu.ifpr.bsi.prefeiturainterativa.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SolicitacoesAdapter$ViewHolder_ViewBinding implements Unbinder {
  private SolicitacoesAdapter.ViewHolder target;

  @UiThread
  public SolicitacoesAdapter$ViewHolder_ViewBinding(SolicitacoesAdapter.ViewHolder target,
      View source) {
    this.target = target;

    target.img_solicitacao = Utils.findRequiredViewAsType(source, R.id.img_solicitacao, "field 'img_solicitacao'", ImageView.class);
    target.tv_data = Utils.findRequiredViewAsType(source, R.id.tv_data, "field 'tv_data'", TextView.class);
    target.rv_categorias = Utils.findRequiredViewAsType(source, R.id.rv_categorias, "field 'rv_categorias'", RecyclerView.class);
    target.card_solicitacao = Utils.findRequiredViewAsType(source, R.id.card_solicitacao, "field 'card_solicitacao'", CardView.class);
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
    target.card_solicitacao = null;
  }
}
