package moshimoshi.cyplay.com.doublenavigation.presenter;

import moshimoshi.cyplay.com.doublenavigation.view.View;

public interface Presenter<T extends View> {

    void setView(T view);

}

