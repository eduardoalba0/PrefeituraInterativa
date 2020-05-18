// Generated code from Butter Knife. Do not modify!
package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.viewpager.widget.ViewPager;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.tabs.TabLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FragmentSolicitacoes_ViewBinding implements Unbinder {
  private FragmentSolicitacoes target;

  @UiThread
  public FragmentSolicitacoes_ViewBinding(FragmentSolicitacoes target, View source) {
    this.target = target;

    target.pager_solicitacoes = Utils.findRequiredViewAsType(source, R.id.pager_solicitacoes, "field 'pager_solicitacoes'", ViewPager.class);
    target.tab_solicitacoes = Utils.findRequiredViewAsType(source, R.id.tab_solicitacoes, "field 'tab_solicitacoes'", TabLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FragmentSolicitacoes target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.pager_solicitacoes = null;
    target.tab_solicitacoes = null;
  }
}
