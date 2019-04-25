package moshimoshi.cyplay.com.doublenavigation.view;

import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;

/**
 * Created by damien on 06/05/15.
 */
public interface UpdateCustomerInfoView extends BaseView {

    void showLoading();

    void onUpdatedCustomerSuccess(String customerId);

    void onUpdatedCustomerFailure(ExceptionType exceptionType, String status, String message);

}
