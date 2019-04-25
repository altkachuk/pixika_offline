package moshimoshi.cyplay.com.doublenavigation.presenter;

import moshimoshi.cyplay.com.doublenavigation.view.ImportView;

/**
 * Created by andre on 18-Mar-19.
 */

public interface ImportPresenter {

    void setView(ImportView view);
    void loadConfig();
    void getDataOut();
}
