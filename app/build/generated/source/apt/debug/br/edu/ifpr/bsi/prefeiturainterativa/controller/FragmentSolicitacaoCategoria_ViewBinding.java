// Generated code from Butter Knife. Do not modify!
package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FragmentSolicitacaoCategoria_ViewBinding implements Unbinder {
  private FragmentSolicitacaoCategoria target;

  private View view7f090172;

  @UiThread
  @SuppressLint("ClickableViewAccessibility")
  public FragmentSolicitacaoCategoria_ViewBinding(final FragmentSolicitacaoCategoria target,
      View source) {
    this.target = target;

    View view;
    target.rv_departamentos = Utils.findRequiredViewAsType(source, R.id.rv_departamentos, "field 'rv_departamentos'", RecyclerView.class);
    target.rv_topicosSelecionados = Utils.findRequiredViewAsType(source, R.id.rv_topicosSelecionados, "field 'rv_topicosSelecionados'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.sliding_categorias, "field 'sliding_categorias' and method 'onTouch'");
    target.sliding_categorias = Utils.castView(view, R.id.sliding_categorias, "field 'sliding_categorias'", SlidingUpPanelLayout.class);
    view7f090172 = view;
    view.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View p0, MotionEvent p1) {
        return target.onTouch(p0, p1);
      }
    });
    target.tv_numeroTopicos = Utils.findRequiredViewAsType(source, R.id.tv_numeroTopicos, "field 'tv_numeroTopicos'", TextView.class);
    target.tv_topicosVazios = Utils.findRequiredViewAsType(source, R.id.tv_topicosVazios, "field 'tv_topicosVazios'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FragmentSolicitacaoCategoria target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rv_departamentos = null;
    target.rv_topicosSelecionados = null;
    target.sliding_categorias = null;
    target.tv_numeroTopicos = null;
    target.tv_topicosVazios = null;

    view7f090172.setOnTouchListener(null);
    view7f090172 = null;
  }
}
