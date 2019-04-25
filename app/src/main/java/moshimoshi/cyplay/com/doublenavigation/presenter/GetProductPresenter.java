package moshimoshi.cyplay.com.doublenavigation.presenter;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.filters.ProductFilter;
import moshimoshi.cyplay.com.doublenavigation.view.FilterResourceView;

/**
 * Created by damien on 05/05/15.
 */
public interface GetProductPresenter extends Presenter<FilterResourceView<Product, ProductFilter>> {

    void getProductInfoFromBarCode(String barCode);

    void getProduct(String productId, List<String> project);

    void getProductStock(String productId, String skuId);

}
