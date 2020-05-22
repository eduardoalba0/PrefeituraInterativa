// Generated code from Butter Knife. Do not modify!
package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.google.android.material.button.MaterialButton;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FragmentSolicitacaoLocalizacao_ViewBinding implements Unbinder {
  private FragmentSolicitacaoLocalizacao target;

  private View view7f090066;

  @UiThread
  public FragmentSolicitacaoLocalizacao_ViewBinding(final FragmentSolicitacaoLocalizacao target,
      View source) {
    this.target = target;

    View view;
    target.tv_marcadorSelecionado = Utils.findRequiredViewAsType(source, R.id.tv_marcadorSelecionado, "field 'tv_marcadorSelecionado'", TextView.class);
    view = Utils.findRequiredView(source, R.id.bt_remover, "field 'bt_remover' and method 'onClick'");
    target.bt_remover = Utils.castView(view, R.id.bt_remover, "field 'bt_remover'", MaterialButton.class);
    view7f090066 = view;
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
    FragmentSolicitacaoLocalizacao target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tv_marcadorSelecionado = null;
    target.bt_remover = null;

    view7f090066.setOnClickListener(null);
    view7f090066 = null;
  }
}
