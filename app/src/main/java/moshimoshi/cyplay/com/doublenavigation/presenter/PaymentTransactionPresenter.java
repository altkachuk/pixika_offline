package moshimoshi.cyplay.com.doublenavigation.presenter;

import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerPaymentTransaction;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerPaymentTransactionStatus;
import moshimoshi.cyplay.com.doublenavigation.view.PaymentTransactionView;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.requests.ResourceRequest;


/**
 * Created by damien on 29/06/15.
 */

public interface PaymentTransactionPresenter extends Presenter<PaymentTransactionView> {

    void addTransactionToCustomerPayment(ResourceRequest<CustomerPaymentTransaction> resourceRequest);

    void updateCustomerTransactionPayment(ResourceRequest<CustomerPaymentTransactionStatus> resourceRequest);
}
