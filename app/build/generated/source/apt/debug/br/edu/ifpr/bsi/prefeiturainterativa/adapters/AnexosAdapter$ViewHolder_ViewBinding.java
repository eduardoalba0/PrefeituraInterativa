// Generated code from Butter Knife. Do not modify!
package br.edu.ifpr.bsi.prefeiturainterativa.adapters;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.google.android.material.button.MaterialButton;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AnexosAdapter$ViewHolder_ViewBinding implements Unbinder {
  private AnexosAdapter.ViewHolder target;

  private View view7f0900d4;

  private View view7f090064;

  @UiThread
  public AnexosAdapter$ViewHolder_ViewBinding(final AnexosAdapter.ViewHolder target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.img_anexo, "field 'img_anexo' and method 'onClick'");
    target.img_anexo = Utils.castView(view, R.id.img_anexo, "field 'img_anexo'", ImageView.class);
    view7f0900d4 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.bt_remover, "field 'bt_remover' and method 'onClick'");
    target.bt_remover = Utils.castView(view, R.id.bt_remover, "field 'bt_remover'", MaterialButton.class);
    view7f090064 = view;
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
    AnexosAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.img_anexo = null;
    target.bt_remover = null;

    view7f0900d4.setOnClickListener(null);
    view7f0900d4 = null;
    view7f090064.setOnClickListener(null);
    view7f090064 = null;
  }
}
