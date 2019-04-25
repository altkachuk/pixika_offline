package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;

import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;

/**
 * Created by damien on 22/04/16.
 */
public class BasePresenter {
    private final  Context context;

    public BasePresenter(Context context) {
        ((PlayRetailApp) context.getApplicationContext()).inject(this);
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

}
