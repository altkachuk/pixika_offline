package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractPaginatedResourceRequestCallbackImpl;
import moshimoshi.cyplay.com.doublenavigation.view.CustomerCompletionView;
import ninja.cyplay.com.apilibrary.domain.interactor.CustomerInteractor;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;
import ninja.cyplay.com.apilibrary.utils.LogUtils;

/**
 * Created by damien on 05/05/15.
 */
public class CustomerCompletionPresenterImpl extends BasePresenter implements CustomerCompletionPresenter {

    @Inject
    APIValue apiValue;

    private CustomerCompletionView customerCompletionView;
    private CustomerInteractor customerInteractor;

    public CustomerCompletionPresenterImpl(Context context, CustomerInteractor customerInteractor) {
        super(context);
        this.customerInteractor = customerInteractor;
    }

    @Override
    public void setView(CustomerCompletionView view) {
        this.customerCompletionView = view;
    }

    @Override
    public void completionCustomer(String customer) {
        customerCompletionView.showLoading();
        customerInteractor.completionFromName(customer, new  AbstractPaginatedResourceRequestCallbackImpl<String>() {

            @Override
            public void onSuccess(List<String> resource, Integer count, String previous, String next) {
                customerCompletionView.onCompletionSuccess(resource);
            }

            @Override
            public void onError(BaseException e) {
                Log.e(LogUtils.generateTag(this), "Error on CustomerCompletionPresenterImpl");
                customerCompletionView.onError(e.getType(), e.getResponse().getStatus(), e.getResponse().getDetail());
            }
        });
    }

}
