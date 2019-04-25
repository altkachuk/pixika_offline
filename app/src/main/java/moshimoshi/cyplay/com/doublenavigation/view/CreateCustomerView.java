package moshimoshi.cyplay.com.doublenavigation.view;

import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;

/**
 * Created by damien on 20/01/16.
 */
public interface CreateCustomerView extends BaseView {

    void showLoading();

    void onCreateCustomerSuccess(Customer customer);

    void onCreateCustomerError(ExceptionType exceptionType, String status, String message);

}