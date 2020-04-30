package br.edu.ifpr.bsi.prefeiturainterativa.helpers;

import android.animation.Animator;
import android.content.Context;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;

import br.edu.ifpr.bsi.prefeiturainterativa.R;

public class TransitionHelper {

    public static Transition inflateChangeBoundsTransition(Context context) {
        Transition t = TransitionInflater.from(context).inflateTransition(R.transition.changebounds);
        return t;
    }

    public static Transition inflateChangeBoundsTransition(Context context, long duration) {
        Transition t = TransitionInflater.from(context).inflateTransition(R.transition.changebounds);
        t.setDuration(duration);
        return t;
    }

    public static Transition.TransitionListener getCircularEnterTransitionListener(View sharedElement, View view_root, View... reveals) {
        return new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                for (View reveal : reveals) {
                    reveal.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                try {
                    int cx = (sharedElement.getLeft() + sharedElement.getRight()) / 2;
                    int cy = (sharedElement.getTop() + sharedElement.getBottom()) / 2;
                    int finalRadius = Math.max(view_root.getWidth(), view_root.getHeight());
                    Animator anim = ViewAnimationUtils.createCircularReveal(view_root, cx, cy, 0, finalRadius);
                    anim.setDuration(1000);

                    for (View reveal : reveals) {
                        reveal.setVisibility(View.VISIBLE);
                    }

                    anim.setInterpolator(new AnticipateInterpolator());
                    anim.start();
                } catch (IllegalStateException ex) {
                    onTransitionCancel(transition);
                }
            }

            @Override
            public void onTransitionCancel(Transition transition) {
                for (View reveal : reveals) {
                    reveal.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTransitionPause(Transition transition) {
                for (View reveal : reveals) {
                    reveal.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTransitionResume(Transition transition) {
                for (View reveal : reveals) {
                    reveal.setVisibility(View.VISIBLE);
                }
            }
        };
    }
}
