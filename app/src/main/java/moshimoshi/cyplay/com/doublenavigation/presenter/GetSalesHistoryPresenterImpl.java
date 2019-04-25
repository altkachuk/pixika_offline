package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.Ticket;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.PaginatedResourceRequestCallbackDefaultImpl;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.domain.interactor.CustomerInteractor;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ATicket;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;

/**
 * Created by damien on 06/05/15.
 */
public class GetSalesHistoryPresenterImpl extends BasePresenter implements GetSalesHistoryPresenter {

    @Inject
    APIValue apiValue;

    @Inject
    EventBus bus;

    private Context context;
    private ResourceView<List<Ticket>> getSalesHistoryView;
    private CustomerInteractor customerInteractor;

    public GetSalesHistoryPresenterImpl(Context context, CustomerInteractor customerInteractor) {
        super(context);
        this.context = context;
        this.customerInteractor = customerInteractor;
    }

    @Override
    public void getCustomerSalesHistory(String customerId) {
        bus.post(new ResourceRequestEvent<List<Ticket>>(EResourceType.TICKETS));
        customerInteractor.getCustomerSalesHistory(
                customerId,
                new PaginatedResourceRequestCallbackDefaultImpl<Ticket, PR_ATicket>(context,getSalesHistoryView, EResourceType.TICKETS));
    }

    @Override
    public void setView(ResourceView<List<Ticket>> view) {
        getSalesHistoryView = view;
    }

}
