package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;
import android.util.Log;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractResourceRequestCallback;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.SafeResourceRequestCallbackExecutor;
import moshimoshi.cyplay.com.doublenavigation.view.ProvisionDeviceView;
import ninja.cyplay.com.apilibrary.domain.interactor.ClientInteractor;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ADevice;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;
import ninja.cyplay.com.apilibrary.utils.LogUtils;

/**
 * Created by damien on 29/04/15.
 */
public class ProvisionDevicePresenterImpl extends BasePresenter implements ProvisionDevicePresenter {

    @Inject
    APIValue apiValue;

    private Context context;
    private ProvisionDeviceView provisionDeviceView;
    private ClientInteractor clientInteractor;

    public ProvisionDevicePresenterImpl(Context context, ClientInteractor clientInteractor) {
        super(context);
        this.context = context;
        this.clientInteractor = clientInteractor;
    }

    @Override
    public void setView(ProvisionDeviceView view) {
        this.provisionDeviceView = view;
    }

    @Override
    public void provisionDevice(String storeId) {

        provisionDeviceView.showLoading();
        clientInteractor.provisionDevice(apiValue.getDeviceId(), storeId, new AbstractResourceRequestCallback<PR_ADevice>() {

            @Override
            public void onSuccess(PR_ADevice pr_aDevice) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        provisionDeviceView.onProvisionDeviceSuccess();
                    }
                });

            }

            @Override
            public void onError(final BaseException e) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        // If Device already provisioned
                        if (e != null && e.getResponse() != null && "409".equals(e.getResponse().getStatus()))
                            provisionDeviceView.onProvisionDeviceSuccess();
                        else {
                            Log.e(LogUtils.generateTag(this), "Error on presenter provision device");
                            provisionDeviceView.onProvisionDeviceError();
                        }
                    }
                });


            }

        });
    }

    @Override
    public void updateStore(String storeId) {

        provisionDeviceView.showLoading();
        clientInteractor.updateDeviceStore(apiValue.getDeviceId(), storeId, new AbstractResourceRequestCallback<PR_ADevice>() {

            @Override
            public void onSuccess(PR_ADevice pr_aDevice) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        provisionDeviceView.onProvisionDeviceSuccess();
                    }
                });

            }

            @Override
            public void onError(BaseException e) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        Log.e(LogUtils.generateTag(this), "Error on presenter provision device");
                        provisionDeviceView.onProvisionDeviceError();
                    }
                });

            }

        });
    }

}
