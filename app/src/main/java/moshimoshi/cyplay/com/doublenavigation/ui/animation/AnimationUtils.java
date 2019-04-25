package moshimoshi.cyplay.com.doublenavigation.ui.animation;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by romainlebouc on 25/01/2017.
 */

public class AnimationUtils {


    public static void animDimBackground(View view, int startDelay) {
        if (view != null) {
            ValueAnimator colorAnim = ObjectAnimator.ofInt(view, "backgroundColor", Color.TRANSPARENT, 0xAA000000);
            colorAnim.setDuration(view.getContext().getResources().getInteger(R.integer.transition_speed));
            colorAnim.setStartDelay(startDelay);
            colorAnim.setEvaluator(new ArgbEvaluator());
            colorAnim.start();
        }

    }

    public static Animation buildCollapseAnimation(final View v,
                                                   long offset,
                                                   long duration) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setStartOffset(offset);
        a.setDuration(duration);
        return a;
    }

}
