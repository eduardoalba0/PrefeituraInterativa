// Generated code from Butter Knife. Do not modify!
package br.edu.ifpr.bsi.prefeiturainterativa.controller;

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

public class FragmentSolicitacaoAnexos_ViewBinding implements Unbinder {
  private FragmentSolicitacaoAnexos target;

  private View view7f0901b4;

  @UiThread
  public FragmentSolicitacaoAnexos_ViewBinding(final FragmentSolicitacaoAnexos target,
      View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.tv_titulo, "field 'tv_titulo' and method 'onClick'");
    target.tv_titulo = Utils.castView(view, R.id.tv_titulo, "field 'tv_titulo'", TextView.class);
    view7f0901b4 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.rv_imagens = Utils.findRequiredViewAsType(source, R.id.rv_imagens, "field 'rv_imagens'", RecyclerView.class);
    target.img_test = Utils.findRequiredViewAsType(source, R.id.img_test, "field 'img_test'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FragmentSolicitacaoAnexos target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tv_titulo = null;
    target.rv_imagens = null;
    target.img_test = null;

    view7f0901b4.setOnClickListener(null);
    view7f0901b4 = null;
  }
}
