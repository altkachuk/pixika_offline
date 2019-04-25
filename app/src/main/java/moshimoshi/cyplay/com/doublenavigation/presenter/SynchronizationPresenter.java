package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.app.Activity;

import moshimoshi.cyplay.com.doublenavigation.view.ImportView;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import moshimoshi.cyplay.com.doublenavigation.view.SynchronizationView;

/**
 * Created by andre on 18-Mar-19.
 */

public interface SynchronizationPresenter {

    void setView(SynchronizationView view);
    void loadSyncData();
    void sendDataIn();
    void clear();
}
