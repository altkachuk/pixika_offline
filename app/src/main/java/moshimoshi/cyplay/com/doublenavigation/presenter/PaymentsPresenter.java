package moshimoshi.cyplay.com.doublenavigation.presenter;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.Payment;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;

/**
 * Created by wentongwang on 20/03/2017.
 */

public interface PaymentsPresenter extends Presenter<ResourceView<List<Payment>>> {

    void getCustomerPayments(String customerId, List<String> project);


}
