package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.model.business.Seller;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.LogInActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractResourceRequestCallback;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.SafeResourceRequestCallbackExecutor;
import moshimoshi.cyplay.com.doublenavigation.utils.ActionEventHelper;
import moshimoshi.cyplay.com.doublenavigation.view.NotifyLostUserPasswordView;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AUser;
import ninja.cyplay.com.apilibrary.models.business.enums.EActionStatus;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.view.AuthenticationView;
import ninja.cyplay.com.apilibrary.domain.interactor.SellerInteractor;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;
import ninja.cyplay.com.apilibrary.models.business.OAuth2Credentials;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;
import ninja.cyplay.com.apilibrary.utils.ClientUtil;

/**
 * Created by damien on 29/04/15.
 */
public class AuthenticationPresenterImpl extends BasePresenter implements AuthenticationPresenter {

    @Inject
    APIValue apiValue;

    @Inject
    SellerContext sellerContext;

    @Inject
    CustomerContext customerContext;

    @Inject
    ActionEventHelper actionEventHelper;

    Context context;
    private AuthenticationView authenticationView;
    private NotifyLostUserPasswordView notifyLostUserPasswordView;
    private SellerInteractor sellerInteractor;

    public AuthenticationPresenterImpl(Context context, SellerInteractor sellerInteractor) {
        super(context);
        this.sellerInteractor = sellerInteractor;
        this.context = context;
    }

    @Override
    public void setView(AuthenticationView view) {
        authenticationView = view;
    }


    @Override
    public void replacePassword(Seller seller, String oldPassword, String newPassord) {
        sellerInteractor.replaceUserPassword(seller.getId(), oldPassword, newPassord, new AbstractResourceRequestCallback<PR_AUser>() {
            @Override
            public void onSuccess(final PR_AUser pr_aUser) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        authenticationView.onPasswordChangedSuccess(pr_aUser);
                    }
                });
            }

            @Override
            public void onError(final BaseException e) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        authenticationView.onPasswordChangedFailure(e);
                    }
                });
            }
        });
    }

    @Override
    public void setNotifyLostUserPasswordView(NotifyLostUserPasswordView view) {
        this.notifyLostUserPasswordView = view;
    }


    @Override
    public void notifyLostUserPassword(Seller seller) {
        sellerInteractor.notifyLostUserPassword(seller.getId(), new AbstractResourceRequestCallback<PR_AUser>() {
            @Override
            public void onSuccess(final PR_AUser pr_aUser) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        if (notifyLostUserPasswordView != null) {
                            notifyLostUserPasswordView.onNotifyLostUserPasswordSuccess(pr_aUser);
                        }

                    }
                });
            }

            @Override
            public void onError(final BaseException e) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        if (notifyLostUserPasswordView != null) {
                            notifyLostUserPasswordView.onNotifyLostUserPasswordFailure(e);
                        }

                    }
                });
            }
        });
    }


    @Override
    public void getAccessToken(final Seller seller, final String password) {

        sellerInteractor.accessToken(seller.getId(), password, ClientUtil.getClientId(), ClientUtil.getClientSecret(),
                new AbstractResourceRequestCallback<OAuth2Credentials>() {
                    @Override
                    public void onSuccess(final OAuth2Credentials OAuth2Credentials) {
                        super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                            @Override
                            public void execute() {
                                // save client id and secret to do request in the future
                                apiValue.setOAuth2Credentials(OAuth2Credentials);
                                apiValue.setUsername(seller.getUsername());
                                apiValue.setPassword(password);
                                //authenticationView.hideLoading();
                                authenticationView.onAccessTokenSuccess();
                                actionEventHelper.log(new LogInActionEventData().
                                        setSeller(seller).
                                        setStatus(EActionStatus.SUCCESS));
                            }
                        });
                    }

                    @Override
                    public void onError(final BaseException e) {
                        super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                            @Override
                            public void execute() {
                                doError(e);
                                actionEventHelper.log(new LogInActionEventData().
                                        setSeller(seller).
                                        setStatus(EActionStatus.FAILURE));
                            }
                        });

                    }
                });
    }

    @Override
    public void invalidateToken() {
        authenticationView.showLoading();
        if (apiValue.getOAuth2Credentials() != null) {
            sellerInteractor.invalidateToken(ClientUtil.getClientId(),
                    ClientUtil.getClientSecret(), apiValue.getOAuth2Credentials().getAccess_token(),
                    new AbstractResourceRequestCallback<Void>() {
                        @Override
                        public void onSuccess(Void v) {
                            super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                                @Override
                                public void execute() {
                                    // Remove token
                                    apiValue.clearOAuth2Credentials();
                                    sellerContext.clearContext();
                                    customerContext.clearContext();
                                    authenticationView.onInvalidateTokenSuccess();
                                }
                            });

                        }

                        @Override
                        public void onError(BaseException e) {
                            super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                                @Override
                                public void execute() {
                                    apiValue.clearOAuth2Credentials();
                                    sellerContext.clearContext();
                                    customerContext.clearContext();
                                    authenticationView.onInvalidateTokenError();
                                }
                            });


                        }
                    });
        }

    }

    private void doError(BaseException e) {
        switch (e.getType()) {
            case BUSINESS:
                if (e.getResponse() != null && e.getResponse().getStatus() != null)
                    authenticationView.onError(ExceptionType.BUSINESS, e.getResponse().getStatus(), e.getResponse().getDetail());
                else {
                    authenticationView.onError(ExceptionType.BUSINESS, null, null);
                }
                break;
            case TECHNICAL:
                authenticationView.onError(ExceptionType.TECHNICAL, null, null);
                break;
            case AUTHENTICATION:
                authenticationView.onError(ExceptionType.AUTHENTICATION, e.getResponse().getStatus(), e.getResponse().getDetail());
                break;
            default:
                authenticationView.onError(ExceptionType.TECHNICAL, null, null);
        }
    }

    // --------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------

}
