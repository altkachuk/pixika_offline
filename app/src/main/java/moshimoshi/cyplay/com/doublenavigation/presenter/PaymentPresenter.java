package moshimoshi.cyplay.com.doublenavigation.presenter;

import moshimoshi.cyplay.com.doublenavigation.model.business.Payment;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerPaymentStatus;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;

/**
 * Created by damien on 29/06/15.
 */

public interface PaymentPresenter extends Presenter<ResourceView<Payment>> {

    void getCustomerPayment(String customerId, String paymentId);

    void addCustomerPayment(String customerId, Payment payment);

    void updateCustomerPayment(String customerId,String customerPaymentId, CustomerPaymentStatus customerPayment);

}
