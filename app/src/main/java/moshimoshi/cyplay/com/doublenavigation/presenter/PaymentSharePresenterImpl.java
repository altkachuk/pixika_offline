package moshimoshi.cyplay.com.doublenavigation.presenter;


import android.content.Context;

import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.PaymentShare;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.ResourceRequestCallbackImpl;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.domain.interactor.PaymentShareInteractor;

/**
 * Created by wentongwang on 30/03/2017.
 */

public class PaymentSharePresenterImpl extends BasePresenter implements PaymentSharePresenter {

    private ResourceView createResourceView;

    private PaymentShareInteractor paymentShareInteractor;

    private Context context;
    // Device selected

    public PaymentSharePresenterImpl(Context context, PaymentShareInteractor paymentShareInteractor) {
        super(context);
        this.context = context;
        this.paymentShareInteractor = paymentShareInteractor;
    }

    @Override
    public void createPaymentShare(PaymentShare paymentShare) {
        paymentShareInteractor.addResource(paymentShare,
                new ResourceRequestCallbackImpl<>(
                        context,
                        createResourceView,
                        EResourceType.PAYMENT_SHARE));
    }

    @Override
    public void setView(ResourceView<PaymentShare> view) {
        this.createResourceView = view;
    }
}
