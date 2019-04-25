package moshimoshi.cyplay.com.doublenavigation.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by romainlebouc on 06/05/16.
 */
public class FadeOutFadInAnimation {

    public FadeOutFadInAnimation() {

    }

    private FadeOutFadInAnimationOnFadeOutEndListener listener;

    public FadeOutFadInAnimationOnFadeOutEndListener getListener() {
        return listener;
    }

    public void setListener(FadeOutFadInAnimationOnFadeOutEndListener listener) {
        this.listener = listener;
    }

    public void startAnimation(final View view) {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new DecelerateInterpolator()); //add this
        fadeOut.setDuration(500);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if (FadeOutFadInAnimation.this.listener != null) {
                    FadeOutFadInAnimation.this.listener.onFadeOutEndListener();
                }

                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
                fadeIn.setDuration(500);
                view.startAnimation(fadeIn);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(fadeOut);
    }

    public interface FadeOutFadInAnimationOnFadeOutEndListener {

        void onFadeOutEndListener();

    }
}
