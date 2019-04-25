package moshimoshi.cyplay.com.doublenavigation.presenter;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.filters.ProductFilter;
import ninja.cyplay.com.apilibrary.models.meta.Pagination;
import moshimoshi.cyplay.com.doublenavigation.view.FilterResourceView;
import ninja.cyplay.com.apilibrary.models.meta.ResourceFieldSorting;

/**
 * Created by damien on 05/05/15.
 */
public interface GetProductsPresenter extends Presenter<FilterResourceView<List<Product>, ProductFilter>> {

    void getProductsFromIds(List<String> productIds,
                            List<String> projection);

    void getProductsFromSkuIds(List<String> skuIds,
                               List<String> projection);

    void getProductsForBrand(String brand,
                             List<String> projection,
                             Pagination pagination,
                             ResourceFieldSorting resourceFieldSortings,
                             List<?> resourceFilters);

    void getProductsForAssortment(String assortment,
                                  List<String> projection,
                                  ResourceFieldSorting resourceFieldSortings);

    void getProductsForFamily(String familyId,
                              List<String> projection,
                              Pagination pagination,
                              ResourceFieldSorting resourceFieldSortings,
                              List<?> resourceFilters);

}
