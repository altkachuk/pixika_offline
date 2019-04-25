package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.Offer;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.GetActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.PaginatedResourceRequestCallbackDefaultImpl;
import moshimoshi.cyplay.com.doublenavigation.utils.ActionEventHelper;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.domain.interactor.OfferInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.callback.RequestCallbackLoger;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AOffer;
import ninja.cyplay.com.apilibrary.models.business.enums.EActionStatus;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;

/**
 * Created by romainlebouc on 02/09/16.
 */
public class OffersPresenterImpl extends BasePresenter implements OffersPresenter {

    @Inject
    APIValue apiValue;

    @Inject
    EventBus bus;

    @Inject
    ActionEventHelper actionEventHelper;

    private Context context;
    private ResourceView<List<Offer>> offersView;
    private OfferInteractor offerInteractor;

    public OffersPresenterImpl(Context context, OfferInteractor offerInteractor) {
        super(context);
        this.context = context;
        this.offerInteractor = offerInteractor;
    }

    @Override
    public void setView(ResourceView<List<Offer>> view) {
        this.offersView = view;
    }

    @Override
    public void getShopOffers(String shopId) {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("shop_ids", shopId);

        bus.post(new ResourceRequestEvent<List<Offer>>(EResourceType.OFFERS));
        offerInteractor.getOffers(parameters,
                -1,
                0,
                Offer.OFFER_PROJECTION,
                null,
                new PaginatedResourceRequestCallbackDefaultImpl<Offer, PR_AOffer>(context,
                        new RequestCallbackLoger() {
                            @Override
                            public void log(boolean success) {
                                actionEventHelper.log(new GetActionEventData()
                                        .setResource(EResource.OFFER)
                                        .setStatus(EActionStatus.getActionStatus(success)));
                            }
                        },
                        offersView, EResourceType.OFFERS));
    }

}
