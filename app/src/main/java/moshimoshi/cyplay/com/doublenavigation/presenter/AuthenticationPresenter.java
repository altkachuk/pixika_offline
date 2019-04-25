package moshimoshi.cyplay.com.doublenavigation.presenter;


import moshimoshi.cyplay.com.doublenavigation.model.business.Seller;
import moshimoshi.cyplay.com.doublenavigation.view.AuthenticationView;
import moshimoshi.cyplay.com.doublenavigation.view.NotifyLostUserPasswordView;

/**
 * Created by damien on 29/04/15.
 */
public interface AuthenticationPresenter extends Presenter<AuthenticationView> {

    void getAccessToken(Seller seller, String password);

    void invalidateToken();

    void replacePassword(Seller seller, String oldPassword, String newPassord);

    void setNotifyLostUserPasswordView(NotifyLostUserPasswordView view);

    void notifyLostUserPassword(Seller seller);
}