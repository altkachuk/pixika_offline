package moshimoshi.cyplay.com.doublenavigation.bg.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;

/**
 * Created by romainlebouc on 08/03/2017.
 */

public abstract class BaseService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        injectDependencies();
    }

    protected void injectDependencies() {
        PlayRetailApp.get(this.getApplicationContext()).inject(this);
    }

    @Nullable
    public abstract IBinder onBind(Intent intent);
}
