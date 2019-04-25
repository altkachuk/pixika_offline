package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;
import android.util.Log;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.model.business.config.Config;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.SafeResourceRequestCallbackExecutor;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractResourceRequestCallback;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;
import moshimoshi.cyplay.com.doublenavigation.view.GetConfigView;
import ninja.cyplay.com.apilibrary.domain.interactor.ClientInteractor;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AConfig;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;
import ninja.cyplay.com.apilibrary.utils.ClientUtil;
import ninja.cyplay.com.apilibrary.utils.LogUtils;

public class GetConfigPresenterImpl extends BasePresenter implements GetConfigPresenter {

    public final static String APP_NOT_SUPPORTED_CODE = "2003";

    @Inject
    APIValue apiValue;

    @Inject
    ConfigHelper configHelper;

    private GetConfigView getConfigView;
    private ClientInteractor clientInteractor;

    public GetConfigPresenterImpl(Context context, ClientInteractor clientInteractor) {
        super(context);
        this.clientInteractor = clientInteractor;
    }

    @Override
    public void onReloadClick() {
        getConfig(configHelper.getCurrentShop().getId());
    }

    public void getConfig(String shopId) {
        getConfigView.showLoading();


        clientInteractor.getConfig(apiValue.getDeviceId(), moshimoshi.cyplay.com.doublenavigation.BuildConfig.VERSION_NAME, shopId, new AbstractResourceRequestCallback<PR_AConfig>() {
            @Override
            public void onSuccess(final PR_AConfig pr_aConfig) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        Config config = (Config) pr_aConfig;
                        configHelper.setConfig(config);
                        apiValue.setConfig(pr_aConfig);
                        if (((Config) pr_aConfig).getShop() !=null ){
                            ClientUtil.setShopId(((Config) pr_aConfig).getShop().getId());
                        }
                        getConfigView.onConfigSuccess();
                    }
                });
            }

            @Override
            public void onError(final BaseException e) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        Log.e(LogUtils.generateTag(GetConfigPresenterImpl.this), "Error on GetConfigPresenterImpl");
                        if (e.getType() == ExceptionType.BUSINESS &&
                                e.getResponse() != null && "11".equals(e.getResponse().getStatus())) {
                            getConfigView.goToDeviceRegistration();
                        }else if (e.getType() == ExceptionType.BUSINESS &&
                                e.getResponse() != null && GetConfigPresenterImpl.APP_NOT_SUPPORTED_CODE.equals(e.getResponse().getStatus())) {
                            getConfigView.appNotSupported(moshimoshi.cyplay.com.doublenavigation.BuildConfig.VERSION_NAME);
                        }else if (e.getResponse() != null)
                            getConfigView.onError(e.getType(), e.getResponse().getStatus(), e.getResponse().getDetail());
                        else
                            getConfigView.onError(e.getType(), null, null);
                    }
                });

            }
        } );
    }

    @Override
    public void setView(GetConfigView view) {
        this.getConfigView = view;
    }
}
