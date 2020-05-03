// Generated code from Butter Knife. Do not modify!
package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.view.TextureView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FragmentAnexosCamera_ViewBinding implements Unbinder {
  private FragmentAnexosCamera target;

  private View view7f09005e;

  private View view7f090065;

  private View view7f090066;

  @UiThread
  public FragmentAnexosCamera_ViewBinding(final FragmentAnexosCamera target, View source) {
    this.target = target;

    View view;
    target.view_camera = Utils.findRequiredViewAsType(source, R.id.view_camera, "field 'view_camera'", TextureView.class);
    view = Utils.findRequiredView(source, R.id.bt_flash, "field 'bt_flash' and method 'onCheckedChanged'");
    target.bt_flash = Utils.castView(view, R.id.bt_flash, "field 'bt_flash'", ToggleButton.class);
    view7f09005e = view;
    ((CompoundButton) view).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton p0, boolean p1) {
        target.onCheckedChanged(p0, p1);
      }
    });
    view = Utils.findRequiredView(source, R.id.bt_tirarFoto, "field 'bt_tirar_foto' and method 'onClick'");
    target.bt_tirar_foto = Utils.castView(view, R.id.bt_tirarFoto, "field 'bt_tirar_foto'", FloatingActionButton.class);
    view7f090065 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.bt_trocarCamera, "field 'bt_trocarCamera' and method 'onClick'");
    target.bt_trocarCamera = Utils.castView(view, R.id.bt_trocarCamera, "field 'bt_trocarCamera'", FloatingActionButton.class);
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
    FragmentAnexosCamera target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.view_camera = null;
    target.bt_flash = null;
    target.bt_tirar_foto = null;
    target.bt_trocarCamera = null;

    ((CompoundButton) view7f09005e).setOnCheckedChangeListener(null);
    view7f09005e = null;
    view7f090065.setOnClickListener(null);
    view7f090065 = null;
    view7f090066.setOnClickListener(null);
    view7f090066 = null;
  }
}
