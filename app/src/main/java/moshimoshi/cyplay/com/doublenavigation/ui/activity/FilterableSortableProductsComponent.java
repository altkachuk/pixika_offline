package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.filters.ProductFilter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.constants.ProductSearch;
import ninja.cyplay.com.apilibrary.models.filters.ResourceFilter;
import ninja.cyplay.com.apilibrary.models.meta.Pagination;
import ninja.cyplay.com.apilibrary.models.meta.ResourceFieldSorting;

/**
 * Created by romainlebouc on 23/12/2016.
 */

public interface FilterableSortableProductsComponent<TResourceFilter extends ResourceFilter> {

    void clearFilters();

    List<ProductFilter> getAvailableFilters();

    List<ProductFilter> getActiveFilters();

    void setAvailableFilters(List<ProductFilter> productFilters);

    List<Product> getProducts();

    void setProducts(List<Product> products);

    void loadProducts(ProductSearch search, Pagination pagination);

    void loadExtraProducts(Pagination pagination,
                           ResourceFieldSorting resourceFieldSorting,
                           List<TResourceFilter> resourceFilters);

    ResourceFieldSorting getResourceFieldSorting();

    void loadResource(Pagination pagination);

    int getProductsColumnCount();

    void onBackBtnClicked();

}
