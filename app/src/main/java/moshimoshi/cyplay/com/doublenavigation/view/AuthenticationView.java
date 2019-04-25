package moshimoshi.cyplay.com.doublenavigation.view;

import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AUser;

/**
 * Created by damien on 29/04/15.
 */
public interface AuthenticationView extends BaseView {

    void showLoading();

    void onAccessTokenSuccess();

    void onInvalidateTokenSuccess();

    void onInvalidateTokenError();

    void onGetSellerSuccess();

    void onPasswordChangedSuccess(PR_AUser user);

    void onPasswordChangedFailure(BaseException exception);



}
