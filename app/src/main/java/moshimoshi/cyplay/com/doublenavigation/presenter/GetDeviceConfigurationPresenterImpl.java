package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.model.business.DeviceConfiguration;
import moshimoshi.cyplay.com.doublenavigation.model.business.Seller;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractResourceRequestCallback;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.SafeResourceRequestCallbackExecutor;
import moshimoshi.cyplay.com.doublenavigation.view.GetSellersView;
import ninja.cyplay.com.apilibrary.domain.interactor.SellerInteractor;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ADeviceConfiguration;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;
import ninja.cyplay.com.apilibrary.utils.LogUtils;

/**
 * Created by damien on 28/04/15.
 */
public class GetDeviceConfigurationPresenterImpl extends BasePresenter implements GetDeviceConfigurationPresenter {

    @Inject
    APIValue apiValue;

    private Context context;
    private GetSellersView getSellersView;
    private SellerInteractor sellerInteractor;

    public GetDeviceConfigurationPresenterImpl(Context context, SellerInteractor sellerInteractor) {
        super(context);
        this.sellerInteractor = sellerInteractor;
        this.context = context;
    }

    @Override
    public void setView(GetSellersView view) {
        this.getSellersView = view;
    }

    @Override
    public void onDeviceConfiguration(List<Seller> PRSellers) {
        getSellersView.onSellersSuccess(PRSellers);
    }

    public void getDeviceConfiguration() {
        sellerInteractor.getDeviceConfiguration(apiValue.getDeviceId(), new AbstractResourceRequestCallback<PR_ADeviceConfiguration>() {
            @Override
            public void onSuccess(final PR_ADeviceConfiguration pr_aDeviceConfiguration) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        onDeviceConfiguration(((DeviceConfiguration)pr_aDeviceConfiguration).getSellers());
                    }
                });


            }

            @Override
            public void onError(final BaseException e) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        Log.e(LogUtils.generateTag(this), "Error on interactor Seller");
                        getSellersView.onError(e.getType(), e.getResponse().getStatus(), e.getResponse().getDetail());
                    }
                });

            }
        });
    }

}
