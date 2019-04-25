package moshimoshi.cyplay.com.doublenavigation.view;

import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AUser;

/**
 * Created by wentongwang on 03/07/2017.
 */

public interface NotifyLostUserPasswordView extends BaseView {

    void onNotifyLostUserPasswordSuccess(PR_AUser user);

    void onNotifyLostUserPasswordFailure(BaseException exception);
}
