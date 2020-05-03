// Generated code from Butter Knife. Do not modify!
package br.edu.ifpr.bsi.prefeiturainterativa.helpers;

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

public class AnexosBottomSheetDialog_ViewBinding implements Unbinder {
  private AnexosBottomSheetDialog target;

  @UiThread
  public AnexosBottomSheetDialog_ViewBinding(AnexosBottomSheetDialog target, View source) {
    this.target = target;

    target.pager_anexos = Utils.findRequiredViewAsType(source, R.id.pager_anexos, "field 'pager_anexos'", ViewPager.class);
    target.tabs_anexos = Utils.findRequiredViewAsType(source, R.id.tabs_anexos, "field 'tabs_anexos'", TabLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    AnexosBottomSheetDialog target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.pager_anexos = null;
    target.tabs_anexos = null;
  }
}
