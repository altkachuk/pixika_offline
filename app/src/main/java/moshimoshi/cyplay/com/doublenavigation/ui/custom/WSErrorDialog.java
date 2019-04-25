package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by wentongwang on 28/04/2017.
 */

public class WSErrorDialog {

    private static AlertDialog errorDialog;

    public interface DialogButtonClickListener {
        void retry();

        void cancel();
    }

    public static void showWSErrorDialog(Context context, String errorStr, final DialogButtonClickListener listener) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        //retry
                        if (listener != null)
                            listener.retry();
                        errorDialog.dismiss();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        //cancel
                        if (listener != null)
                            listener.cancel();
                        errorDialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_AlertDialog);
        errorDialog = builder
                .setTitle(R.string.error)
                .setMessage(errorStr)
                .setPositiveButton(R.string.retry, dialogClickListener)
                .setNegativeButton(R.string.cancel, dialogClickListener)
                .create();
        errorDialog.show();

    }

}
