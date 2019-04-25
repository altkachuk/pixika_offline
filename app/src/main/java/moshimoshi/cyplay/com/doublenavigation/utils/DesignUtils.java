package moshimoshi.cyplay.com.doublenavigation.utils;

import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.TextView;

/**
 * Created by damien on 25/04/16.
 */
public class DesignUtils {


    public static void setTextColor(TextView textview, int color) {
        if (textview != null)
            textview.setTextColor(color);
    }

    public static void setBackgroundColor(View view, int color) {
        try {
            if (view != null) {
                GradientDrawable drawable = (GradientDrawable) view.getBackground();
                if (drawable != null)
                    drawable.setColor(color);
                else
                    view.setBackgroundColor(color);
            }
        } catch (Exception e) {
            view.setBackgroundColor(color);
        }
    }

}
