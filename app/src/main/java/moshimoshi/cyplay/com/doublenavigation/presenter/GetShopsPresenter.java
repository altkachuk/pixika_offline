package moshimoshi.cyplay.com.doublenavigation.presenter;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EShopType;
import moshimoshi.cyplay.com.doublenavigation.view.GetShopsView;

/**
 * Created by damien on 29/04/15.
 */
public interface GetShopsPresenter extends Presenter<GetShopsView> {

    void getShops(boolean all, List<String> fields, List<String> sortBys, EShopType eShopType);

}
