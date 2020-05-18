// Generated code from Butter Knife. Do not modify!
package br.edu.ifpr.bsi.prefeiturainterativa.helpers.dialogs;

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

public class DialogSolicitacao_ViewBinding implements Unbinder {
  private DialogSolicitacao target;

  @UiThread
  public DialogSolicitacao_ViewBinding(DialogSolicitacao target, View source) {
    this.target = target;

    target.pager_solicitacao = Utils.findRequiredViewAsType(source, R.id.pager_solicitacao, "field 'pager_solicitacao'", ViewPager.class);
    target.tabs_solicitacao = Utils.findRequiredViewAsType(source, R.id.tab_solicitacao, "field 'tabs_solicitacao'", TabLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    DialogSolicitacao target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.pager_solicitacao = null;
    target.tabs_solicitacao = null;
  }
}
