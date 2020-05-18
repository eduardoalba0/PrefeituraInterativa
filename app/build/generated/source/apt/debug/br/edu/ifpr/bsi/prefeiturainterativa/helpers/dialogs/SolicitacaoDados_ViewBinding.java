// Generated code from Butter Knife. Do not modify!
package br.edu.ifpr.bsi.prefeiturainterativa.helpers.dialogs;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SolicitacaoDados_ViewBinding implements Unbinder {
  private SolicitacaoDados target;

  private View view7f09005f;

  @UiThread
  public SolicitacaoDados_ViewBinding(final SolicitacaoDados target, View source) {
    this.target = target;

    View view;
    target.rv_categorias = Utils.findRequiredViewAsType(source, R.id.rv_categorias, "field 'rv_categorias'", RecyclerView.class);
    target.rv_imagens = Utils.findRequiredViewAsType(source, R.id.rv_imagens, "field 'rv_imagens'", RecyclerView.class);
    target.edt_descricao = Utils.findRequiredViewAsType(source, R.id.edt_descricao, "field 'edt_descricao'", TextInputEditText.class);
    view = Utils.findRequiredView(source, R.id.bt_localizacao, "field 'bt_localizacao' and method 'onClick'");
    target.bt_localizacao = Utils.castView(view, R.id.bt_localizacao, "field 'bt_localizacao'", MaterialButton.class);
    view7f09005f = view;
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
    SolicitacaoDados target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rv_categorias = null;
    target.rv_imagens = null;
    target.edt_descricao = null;
    target.bt_localizacao = null;

    view7f09005f.setOnClickListener(null);
    view7f09005f = null;
  }
}
