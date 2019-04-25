package moshimoshi.cyplay.com.doublenavigation.utils;

import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

/**
 * Created by damien on 25/05/16.
 */
public class SnackBarHelper {

    public static Snackbar buildSnackbar(View view, String message, @Nullable String dismissMessage) {
        return buildSnackbar(view, message, Snackbar.LENGTH_LONG, dismissMessage);
    }

    public static Snackbar buildSnackbar(View view, String message, int displayLength, @Nullable String dismissMessage) {
        // SnackBar
        final Snackbar snackbar = Snackbar.make(view, message, displayLength);
        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(4);

        // Dismiss message and action
        if (dismissMessage != null) {
            View.OnClickListener dismissListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            };
            snackbar.setAction(dismissMessage, dismissListener);
        }
        return snackbar;
    }



}
