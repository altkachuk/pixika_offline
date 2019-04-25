package moshimoshi.cyplay.com.doublenavigation.presenter;

import moshimoshi.cyplay.com.doublenavigation.view.HomeTweetView;

/**
 * Created by anishosni on 17/06/15.
 */
public interface HomeTweetPresenter extends Presenter<HomeTweetView> {

    void initSocket();

    void disconnectSocket(DisconnectTweetSocketCallback callback);

    void markTweetAsRead(String uid);

    interface DisconnectTweetSocketCallback {
        void onDisconnect();
    }

}