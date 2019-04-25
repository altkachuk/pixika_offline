package moshimoshi.cyplay.com.doublenavigation.presenter;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.Ticket;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;

/**
 * Created by damien on 06/05/15.
 */
public interface GetSalesHistoryPresenter extends Presenter<ResourceView<List<Ticket>>> {

    void getCustomerSalesHistory(String customerId);

}
