package moshimoshi.cyplay.com.doublenavigation.presenter;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.Sale;
import moshimoshi.cyplay.com.doublenavigation.view.ChangeSaleView;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;

/**
 * Created by damien on 04/05/15.
 */
public interface SalesPresenter extends Presenter<ResourceView<List<Sale>>> {

    void setRemoveView(ChangeSaleView removeSaleView);
    void getAllSales();
    void removeSale(String id);
    void editSale(Sale sale);

}