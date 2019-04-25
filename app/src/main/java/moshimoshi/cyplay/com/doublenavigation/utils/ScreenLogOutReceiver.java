package moshimoshi.cyplay.com.doublenavigation.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractResourceRequestCallback;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.SafeResourceRequestCallbackExecutor;
import ninja.cyplay.com.apilibrary.domain.interactor.SellerInteractor;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;
import ninja.cyplay.com.apilibrary.utils.ClientUtil;

/**
 * Created by romainlebouc on 26/08/16.
 */
public class ScreenLogOutReceiver extends BroadcastReceiver {

    public static boolean wasScreenOn = true;
    private final SellerInteractor sellerInteractor;
    private final SellerContext sellerContext;
    private final CustomerContext customerContext;
    private final APIValue apiValue;

    public ScreenLogOutReceiver(SellerInteractor sellerInteractor,
                                APIValue apiValue,
                                SellerContext sellerContext,
                                CustomerContext customerContext) {
        this.sellerInteractor = sellerInteractor;
        this.sellerContext = sellerContext;
        this.customerContext = customerContext;
        this.apiValue = apiValue;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            if (wasScreenOn) {
                // Invalidate current token
                if (apiValue != null && sellerInteractor != null && apiValue.getOAuth2Credentials() != null) {
                    sellerInteractor.invalidateToken(ClientUtil.getClientId(),
                            ClientUtil.getClientSecret(), apiValue.getOAuth2Credentials().getAccess_token(),
                            new AbstractResourceRequestCallback<Void>() {
                                @Override
                                public void onError(BaseException e) {
                                    super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                                        @Override
                                        public void execute() {
                                            apiValue.clearOAuth2Credentials();
                                            if (sellerContext != null) {
                                                sellerContext.clearContext();
                                            }
                                            if (customerContext != null) {
                                                customerContext.clearContext();
                                            }
                                        }
                                    });

                                }

                                @Override
                                public void onSuccess(Void v) {
                                    super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                                        @Override
                                        public void execute() {
                                            apiValue.clearOAuth2Credentials();
                                            if (sellerContext != null) {
                                                sellerContext.clearContext();
                                            }
                                            if (customerContext != null) {
                                                customerContext.clearContext();
                                            }
                                        }
                                    });

                                }
                            });
                }
            }
            wasScreenOn = false;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            wasScreenOn = true;
        }
    }

}