package moshimoshi.cyplay.com.doublenavigation.presenter;

import moshimoshi.cyplay.com.doublenavigation.model.business.PaymentShare;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;

/**
 * Created by wentongwang on 30/03/2017.
 */

public interface PaymentSharePresenter extends Presenter<ResourceView<PaymentShare>> {
    void createPaymentShare(PaymentShare paymentShare);
}
