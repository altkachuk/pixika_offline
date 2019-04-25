package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.model.business.DeliveryMode;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractPaginatedResourceRequestCallbackImpl;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.domain.interactor.DeliveryModeInteractor;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ADeliveryMode;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;

/**
 * Created by wentongwang on 24/03/2017.
 */

public class GetDeliveryModesPresenterImpl extends BasePresenter implements GetDeliveryModesPresenter {

    @Inject
    APIValue apiValue;

    @Inject
    EventBus bus;

    private ResourceView<List<DeliveryMode>> getDeliveryModesView;
    private DeliveryModeInteractor deliveryModeInteractor;


    public GetDeliveryModesPresenterImpl(Context context, DeliveryModeInteractor deliveryModeInteractor) {
        super(context);
        this.deliveryModeInteractor = deliveryModeInteractor;
    }

    @Override
    public void getDeliveryModes(String status) {
        deliveryModeInteractor.getResources(DeliveryModeInteractor.getParametersMap(status),
                DeliveryMode.DELIVERYMODE_PROJECTION,
                new AbstractPaginatedResourceRequestCallbackImpl<PR_ADeliveryMode>(){

                    @Override
                    public void onSuccess(List<PR_ADeliveryMode> resource, Integer count, String previous, String next) {
                        List<DeliveryMode> deliveryModes = new ArrayList<>();
                        for ( PR_ADeliveryMode resourceItem: resource) {
                            deliveryModes.add((DeliveryMode) resourceItem);
                        }
                        getDeliveryModesView.onResourceViewResponse(new ResourceResponseEvent<>(deliveryModes, null, EResourceType.DELIVERY_MODE));
                    }

                    @Override
                    public void onError(BaseException e) {

                    }
                });
    }


    @Override
    public void setView(ResourceView<List<DeliveryMode>> view) {
        this.getDeliveryModesView = view;
    }
}
