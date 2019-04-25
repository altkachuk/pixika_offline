package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerProfile;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.UpdateActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractResourceRequestCallback;
import moshimoshi.cyplay.com.doublenavigation.utils.ActionEventHelper;
import moshimoshi.cyplay.com.doublenavigation.view.UpdateCustomerInfoView;
import ninja.cyplay.com.apilibrary.domain.interactor.CustomerInteractor;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ACustomer;
import ninja.cyplay.com.apilibrary.models.business.enums.EActionStatus;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;

/**
 * Created by damien on 06/05/15.
 */
public class UpdateCustomerInfoPresenterImpl extends BasePresenter implements UpdateCustomerInfoPresenter {

    @Inject
    APIValue apiValue;

    @Inject
    CustomerContext customerContext;

    @Inject
    SellerContext sellerContext;

    @Inject
    ActionEventHelper actionEventHelper;

    private UpdateCustomerInfoView updateCustomerInfoView;
    private CustomerInteractor customerInteractor;

    public UpdateCustomerInfoPresenterImpl(Context context, CustomerInteractor customerInteractor) {
        super(context);
        this.customerInteractor = customerInteractor;
    }

    @Override
    public void updateCustomerProfile(final CustomerProfile customer) {
        updateCustomerInfoView.showLoading();
        customerInteractor.updateCustomerInfo(customer.getId(), customer, new AbstractResourceRequestCallback<PR_ACustomer>() {

            @Override
            public void onSuccess(PR_ACustomer pr_aCustomer) {
                updateCustomerInfoView.onUpdatedCustomerSuccess((pr_aCustomer).getId());
                actionEventHelper.log(new UpdateActionEventData()
                        .addObjectParams(customer)
                        .setValue((pr_aCustomer).getId())
                        .setResource(EResource.CUSTOMER)
                        .setStatus(EActionStatus.SUCCESS)
                );
            }

            @Override
            public void onError(BaseException e) {
                updateCustomerInfoView.onError(e.getType(), e.getResponse().getStatus(), e.getResponse().getDetail());
                actionEventHelper.log(new UpdateActionEventData()
                        .setResource(EResource.CUSTOMER)
                        .addObjectParams(customer)
                        .setStatus(EActionStatus.FAILURE)
                );
                updateCustomerInfoView.onUpdatedCustomerFailure(e.getType(), e.getResponse().getStatus(), e.getResponse().getDetail());
            }

        });
    }

    @Override
    public void setView(UpdateCustomerInfoView view) {
        updateCustomerInfoView = view;
    }

}
