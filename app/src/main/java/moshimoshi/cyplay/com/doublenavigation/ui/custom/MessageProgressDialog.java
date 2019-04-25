package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.app.ProgressDialog;
import android.content.Context;

import moshimoshi.cyplay.com.doublenavigation.R;

public class MessageProgressDialog extends ProgressDialog {

    private Context context;

    public MessageProgressDialog(Context context) {
        super(context);
        init(context);
    }

    public MessageProgressDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        setTitle(context.getString(R.string.loading));
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void show() {
        setMessage(context.getString(R.string.wait_serv_resp));
        super.show();
    }

    public void showWithMessage(String message) {
        setMessage(message);
        super.show();
    }

}
