// Generated code from Butter Knife. Do not modify!
package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.view.View;
import android.widget.CompoundButton;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FragmentSolicitacaoAnexos_ViewBinding implements Unbinder {
  private FragmentSolicitacaoAnexos target;

  private View view7f09005a;

  private View view7f09018f;

  @UiThread
  public FragmentSolicitacaoAnexos_ViewBinding(final FragmentSolicitacaoAnexos target,
      View source) {
    this.target = target;

    View view;
    target.rv_imagens = Utils.findRequiredViewAsType(source, R.id.rv_imagens, "field 'rv_imagens'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.bt_adicionar, "field 'bt_adicionar' and method 'onClick'");
    target.bt_adicionar = Utils.castView(view, R.id.bt_adicionar, "field 'bt_adicionar'", MaterialButton.class);
    view7f09005a = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.sw_anonimo, "field 'sw_anonimo' and method 'onCheckedChanged'");
    target.sw_anonimo = Utils.castView(view, R.id.sw_anonimo, "field 'sw_anonimo'", SwitchMaterial.class);
    view7f09018f = view;
    ((CompoundButton) view).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton p0, boolean p1) {
        target.onCheckedChanged(p0, p1);
      }
    });
    target.edt_descricao = Utils.findRequiredViewAsType(source, R.id.edt_descricao, "field 'edt_descricao'", TextInputEditText.class);
    target.edl_descricao = Utils.findRequiredViewAsType(source, R.id.edl_descricao, "field 'edl_descricao'", TextInputLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FragmentSolicitacaoAnexos target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rv_imagens = null;
    target.bt_adicionar = null;
    target.sw_anonimo = null;
    target.edt_descricao = null;
    target.edl_descricao = null;

    view7f09005a.setOnClickListener(null);
    view7f09005a = null;
    ((CompoundButton) view7f09018f).setOnCheckedChangeListener(null);
    view7f09018f = null;
  }
}
