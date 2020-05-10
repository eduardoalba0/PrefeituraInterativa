// Generated code from Butter Knife. Do not modify!
package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.google.android.material.button.MaterialButton;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FragmentSolicitacaoAnexos_ViewBinding implements Unbinder {
  private FragmentSolicitacaoAnexos target;

  private View view7f09005a;

  @UiThread
  public FragmentSolicitacaoAnexos_ViewBinding(final FragmentSolicitacaoAnexos target,
      View source) {
    this.target = target;

    View view;
    target.rv_imagens = Utils.findRequiredViewAsType(source, R.id.rv_imagens, "field 'rv_imagens'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.bt_adicionar, "field 'bt_adicionar' and method 'onClick'");
    target.bt_adicionar = Utils.castView(view, R.id.bt_adicionar, "field 'bt_adicionar'", MaterialButton.class);
    view7f09005a = view;
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
    FragmentSolicitacaoAnexos target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rv_imagens = null;
    target.bt_adicionar = null;

    view7f09005a.setOnClickListener(null);
    view7f09005a = null;
  }
}
