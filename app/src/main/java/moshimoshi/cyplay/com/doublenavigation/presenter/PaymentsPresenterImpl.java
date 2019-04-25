package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.model.business.Payment;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.PaginatedResourceRequestCallbackDefaultImpl;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.domain.interactor.PaymentInteractor;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_APayment;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;

/**
 * Created by wentongwang on 20/03/2017.
 */

public class PaymentsPresenterImpl extends BasePresenter implements PaymentsPresenter {

    @Inject
    ConfigHelper configHelper;

    @Inject
    APIValue apiValue;

    private Context context;

    private ResourceView<List<Payment>> paymentsView;

    private PaymentInteractor paymentInteractor;

    public PaymentsPresenterImpl(Context context, PaymentInteractor paymentInteractor) {
        super(context);
        this.context = context;
        this.paymentInteractor = paymentInteractor;
    }

    @Override
    public void getCustomerPayments(String customerId, List<String> project) {
        paymentInteractor.getCustomerPayments(customerId,
                project,
                new PaginatedResourceRequestCallbackDefaultImpl<Payment, PR_APayment>(context,
                        paymentsView,
                        EResourceType.PAYMENT));
    }


    @Override
    public void setView(ResourceView<List<Payment>> view) {
        this.paymentsView = view;
    }
}
