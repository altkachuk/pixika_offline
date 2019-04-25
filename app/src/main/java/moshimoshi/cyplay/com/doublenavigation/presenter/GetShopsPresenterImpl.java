package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.model.business.Shop;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EShopType;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.SafeResourceRequestCallbackExecutor;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractPaginatedResourceRequestCallbackImpl;
import moshimoshi.cyplay.com.doublenavigation.view.GetShopsView;
import ninja.cyplay.com.apilibrary.domain.interactor.ClientInteractor;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AShop;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;
import ninja.cyplay.com.apilibrary.utils.LogUtils;

/**
 * Created by damien on 29/04/15.
 */
public class GetShopsPresenterImpl extends BasePresenter implements GetShopsPresenter {

    @Inject
    APIValue apiValue;

    private Context context;
    private GetShopsView getShopsView;
    private ClientInteractor clientInteractor;

    public GetShopsPresenterImpl(Context context, ClientInteractor clientInteractor) {
        super(context);
        this.clientInteractor = clientInteractor;
        this.context = context;
    }

    @Override
    public void setView(GetShopsView view) {
        this.getShopsView = view;
    }

    @Override
    public void getShops(boolean all, List<String> fields, List<String> sortBys, EShopType eShopType) {
        getShopsView.showLoading();

        HashMap<String, String> filters = new HashMap<>();
        if (eShopType != null) {
            filters.put("shop_type", eShopType.getCode());
            filters.put("active", "true");
        }


        clientInteractor.getShops(apiValue.getDeviceId(),
                all,
                fields,
                sortBys,
                filters,
                new AbstractPaginatedResourceRequestCallbackImpl<PR_AShop>() {
                    @Override
                    public void onSuccess(final List<PR_AShop> resource, Integer count, String previous, String next) {
                        super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                            @Override
                            public void execute() {
                                getShopsView.onShopsSuccess((List<Shop>) (List<?>) resource);
                            }
                        });
                    }

                    @Override
                    public void onError(final BaseException e) {
                        super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                            @Override
                            public void execute() {
                                Log.e(LogUtils.generateTag(this), "Error on presenter Shops");
                                switch (e.getType()) {
                                    case BUSINESS:
                                        getShopsView.onShopsError();
                                        break;
                                    case TECHNICAL:
                                        getShopsView.onShopsError();
                                        break;
                                }
                            }
                        });


                    }
                });
    }

}
