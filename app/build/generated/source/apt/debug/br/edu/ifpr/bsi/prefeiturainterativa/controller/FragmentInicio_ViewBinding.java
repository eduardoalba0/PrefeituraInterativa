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
import com.google.android.material.card.MaterialCardView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FragmentInicio_ViewBinding implements Unbinder {
  private FragmentInicio target;

  private View view7f09006a;

  @UiThread
  public FragmentInicio_ViewBinding(final FragmentInicio target, View source) {
    this.target = target;

    View view;
    target.img_usuario = Utils.findRequiredViewAsType(source, R.id.img_usuario, "field 'img_usuario'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.bt_usuario, "field 'bt_usuario' and method 'onClick'");
    target.bt_usuario = Utils.castView(view, R.id.bt_usuario, "field 'bt_usuario'", TextView.class);
    view7f09006a = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.card_pendentes = Utils.findRequiredViewAsType(source, R.id.card_pendentes, "field 'card_pendentes'", MaterialCardView.class);
    target.rv_solicitacoes = Utils.findRequiredViewAsType(source, R.id.rv_solicitacoes, "field 'rv_solicitacoes'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FragmentInicio target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.img_usuario = null;
    target.bt_usuario = null;
    target.card_pendentes = null;
    target.rv_solicitacoes = null;

    view7f09006a.setOnClickListener(null);
    view7f09006a = null;
  }
}
