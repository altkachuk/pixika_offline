package moshimoshi.cyplay.com.doublenavigation.utils;

import android.graphics.Color;

/**
 * Created by damien on 13/04/16.
 */
public class ColorUtil {

    /**
     * Darker a color by a given factor.
     *
     * @param color  The color to lighten
     * @param factor The factor to lighten the color. 0 will make the color unchanged. 1 will make the
     *               color black.
     * @return darker version of the specified color.
     */
    public static int darker(int color, float factor) {
        int red = Math.max((int) (Color.red(color) * factor), 0);
        int green = Math.max((int) (Color.green(color) * factor), 0);
        int blue = Math.max((int) (Color.blue(color) * factor), 0);
        return Color.argb(Color.alpha(color), red, green, blue);
    }

    /**
     * Lightens a color by a given factor.
     *
     * @param color  The color to lighten
     * @param factor The factor to lighten the color. 0 will make the color unchanged. 1 will make the
     *               color white.
     * @return lighter version of the specified color.
     */
    public static int lighter(int color, float factor) {
        int red = (int) ((Color.red(color) * (1 - factor) / 255 + factor) * 255);
        int green = (int) ((Color.green(color) * (1 - factor) / 255 + factor) * 255);
        int blue = (int) ((Color.blue(color) * (1 - factor) / 255 + factor) * 255);
        return Color.argb(Color.alpha(color), red, green, blue);
    }

}
