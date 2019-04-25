package moshimoshi.cyplay.com.doublenavigation.presenter;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.filters.ProductFilter;
import moshimoshi.cyplay.com.doublenavigation.view.FilterResourceView;
import ninja.cyplay.com.apilibrary.models.meta.ResourceFieldSorting;
import ninja.cyplay.com.apilibrary.models.meta.Pagination;

/**
 * Created by damien on 05/05/15.
 */
public interface ProductSearchPresenter extends Presenter<FilterResourceView<List<Product>, ProductFilter>> {

    void searchProduct(String productName,
                       List<String> projection,
                       Pagination pagination,
                       ResourceFieldSorting resourceFieldSortings,
                       List<?> resourceFilters);

}
