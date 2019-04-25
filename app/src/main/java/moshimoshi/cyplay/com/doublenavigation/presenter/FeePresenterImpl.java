package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.Fee;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.PaginatedResourceRequestCallbackDefaultImpl;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.domain.interactor.FeeInteractor;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AFee;

/**
 * Created by romainlebouc on 30/01/2017.
 */

public class FeePresenterImpl extends BasePresenter implements FeePresenter {

    protected Context context;
    protected FeeInteractor feeInteractor;
    private ResourceView<List<Fee>> view;

    private final static String PRODUCT_ID_PARAMETER = "product_id";
    private final static String SKU_ID_PARAMETER = "sku_id";
    private final static String SHOP_ID_PARAMETER = "shop_id";

    public FeePresenterImpl(Context context, FeeInteractor feeInteractor) {
        super(context);
        this.context = context;
        this.feeInteractor = feeInteractor;
    }

    @Override
    public void setView(ResourceView<List<Fee>> view) {
        this.view = view;
    }

    public void getFees(String productId,
                        String skuId,
                        String shopId) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(PRODUCT_ID_PARAMETER, productId);
        parameters.put(SKU_ID_PARAMETER, skuId);
        parameters.put(SHOP_ID_PARAMETER, shopId);

        feeInteractor.getResources(parameters,
                null,
                new PaginatedResourceRequestCallbackDefaultImpl<Fee, PR_AFee>(context,
                        view,
                        EResourceType.FEES));


    }


}
