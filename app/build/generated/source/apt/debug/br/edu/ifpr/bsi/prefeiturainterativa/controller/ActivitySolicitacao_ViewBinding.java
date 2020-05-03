// Generated code from Butter Knife. Do not modify!
package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.stepstone.stepper.StepperLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ActivitySolicitacao_ViewBinding implements Unbinder {
  private ActivitySolicitacao target;

  @UiThread
  public ActivitySolicitacao_ViewBinding(ActivitySolicitacao target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ActivitySolicitacao_ViewBinding(ActivitySolicitacao target, View source) {
    this.target = target;

    target.stepperLayout = Utils.findRequiredViewAsType(source, R.id.stepperLayout, "field 'stepperLayout'", StepperLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ActivitySolicitacao target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.stepperLayout = null;
  }
}
