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
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.card.MaterialCardView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FragmentInicio_ViewBinding implements Unbinder {
  private FragmentInicio target;

  private View view7f0a0060;

  private View view7f0a00d9;

  @UiThread
  public FragmentInicio_ViewBinding(final FragmentInicio target, View source) {
    this.target = target;

    View view;
    target.img_usuario = Utils.findRequiredViewAsType(source, R.id.img_usuario, "field 'img_usuario'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.bt_usuario, "field 'bt_usuario' and method 'onClick'");
    target.bt_usuario = Utils.castView(view, R.id.bt_usuario, "field 'bt_usuario'", TextView.class);
    view7f0a0060 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.tv_nenhumAviso = Utils.findRequiredViewAsType(source, R.id.tv_nenhumAviso, "field 'tv_nenhumAviso'", TextView.class);
    target.tv_numAbertas = Utils.findRequiredViewAsType(source, R.id.tv_numAbertas, "field 'tv_numAbertas'", TextView.class);
    view = Utils.findRequiredView(source, R.id.l_numSolicitacoes, "field 'l_numSolicitacoes' and method 'onClick'");
    target.l_numSolicitacoes = Utils.castView(view, R.id.l_numSolicitacoes, "field 'l_numSolicitacoes'", FlexboxLayout.class);
    view7f0a00d9 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.tv_numConcluidas = Utils.findRequiredViewAsType(source, R.id.tv_numConcluidas, "field 'tv_numConcluidas'", TextView.class);
    target.card_pendentes = Utils.findRequiredViewAsType(source, R.id.card_pendentes, "field 'card_pendentes'", MaterialCardView.class);
    target.rv_solicitacoes = Utils.findRequiredViewAsType(source, R.id.rv_solicitacoes, "field 'rv_solicitacoes'", RecyclerView.class);
    target.rv_avisos = Utils.findRequiredViewAsType(source, R.id.rv_avisos, "field 'rv_avisos'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FragmentInicio target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.img_usuario = null;
    target.bt_usuario = null;
    target.tv_nenhumAviso = null;
    target.tv_numAbertas = null;
    target.l_numSolicitacoes = null;
    target.tv_numConcluidas = null;
    target.card_pendentes = null;
    target.rv_solicitacoes = null;
    target.rv_avisos = null;

    view7f0a0060.setOnClickListener(null);
    view7f0a0060 = null;
    view7f0a00d9.setOnClickListener(null);
    view7f0a00d9 = null;
  }
}
