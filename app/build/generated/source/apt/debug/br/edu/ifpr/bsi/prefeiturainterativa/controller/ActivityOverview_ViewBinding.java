// Generated code from Butter Knife. Do not modify!
package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.viewpager.widget.ViewPager;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ActivityOverview_ViewBinding implements Unbinder {
  private ActivityOverview target;

  private View view7f09005e;

  @UiThread
  public ActivityOverview_ViewBinding(ActivityOverview target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ActivityOverview_ViewBinding(final ActivityOverview target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.bt_foto, "field 'bt_foto' and method 'onClick'");
    target.bt_foto = Utils.castView(view, R.id.bt_foto, "field 'bt_foto'", FloatingActionButton.class);
    view7f09005e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.tab_overview = Utils.findRequiredViewAsType(source, R.id.tab_overview, "field 'tab_overview'", TabLayout.class);
    target.view_root = Utils.findRequiredView(source, R.id.view_root, "field 'view_root'");
    target.pager_overview = Utils.findRequiredViewAsType(source, R.id.pager_overview, "field 'pager_overview'", ViewPager.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ActivityOverview target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.bt_foto = null;
    target.tab_overview = null;
    target.view_root = null;
    target.pager_overview = null;

    view7f09005e.setOnClickListener(null);
    view7f09005e = null;
  }
}
