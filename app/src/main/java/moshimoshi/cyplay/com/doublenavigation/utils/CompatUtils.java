package moshimoshi.cyplay.com.doublenavigation.utils;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;

/**
 * Created by romainlebouc on 03/02/2017.
 */

public class CompatUtils {

    public static void setDrawableTint(@NonNull Drawable drawable, @ColorInt int tint){
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable.mutate(), tint);
    }

}
