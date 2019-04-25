package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.filters.ProductFilter;
import moshimoshi.cyplay.com.doublenavigation.model.events.FilterResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractFilteredPaginatedResourceRequestCallback;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.meta.Pagination;
import moshimoshi.cyplay.com.doublenavigation.view.FilterResourceView;
import ninja.cyplay.com.apilibrary.domain.interactor.callback.FilteredPaginatedResourceRequestCallback;
import ninja.cyplay.com.apilibrary.domain.interactor.ProductInteractor;
import ninja.cyplay.com.apilibrary.models.filters.ResourceFilter;
import ninja.cyplay.com.apilibrary.models.filters.ResourceFilterValue;
import ninja.cyplay.com.apilibrary.models.meta.ResourceFieldSorting;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;

/**
 * Created by damien on 05/05/15.
 */
public class GetProductsPresenterImpl extends BasePresenter implements GetProductsPresenter {

    @Inject
    APIValue apiValue;

    @Inject
    EventBus bus;

    private ProductInteractor productInteractor;
    private FilterResourceView<List<Product>, ProductFilter> getProductsView;

    private final AtomicInteger productsRequestCounter = new AtomicInteger();

    public GetProductsPresenterImpl(Context context, ProductInteractor productInteractor) {
        super(context);
        this.productInteractor = productInteractor;

    }

    @Override
    public void getProductsFromIds(List<String> productIds, List<String> projection) {
        productInteractor.getProductsFromIds(productIds, projection, buildFilteredPaginatedProductsRequestCallbackDefaultImpl());
    }

    @Override
    public void getProductsFromSkuIds(List<String> skuIds, List<String> projection) {
        productInteractor.getProductsFromSkuIds(skuIds, projection, buildFilteredPaginatedProductsRequestCallbackDefaultImpl());
    }


    public void getProductsForBrand(String brand,
                                    List<String> projection,
                                    Pagination pagination,
                                    ResourceFieldSorting resourceFieldSortings,
                                    List<?> resourceFilters) {
        productInteractor.getProductsForBrand(brand,
                pagination.getOffset(),
                pagination.getLimit(),
                projection,
                resourceFieldSortings,
                (List<ResourceFilter<ResourceFilter, ResourceFilterValue>>) resourceFilters,
                buildFilteredPaginatedResourceRequestCallbackDefaultImpl(pagination));
    }

    public void getProductsForAssortment(String assortment,
                                         List<String> projection,
                                         ResourceFieldSorting resourceFieldSortings) {

        if (assortment == null || assortment.trim().isEmpty()) {
            getProductsView.onResourceViewResponse(new FilterResourceResponseEvent<List<Product>, ProductFilter>(new ArrayList<Product>(),
                    null,
                    EResourceType.PRODUCTS,
                    null,
                    null,
                    0
            ));
        } else {
            productInteractor.getProductsForAssortment(assortment,
                    projection,
                    resourceFieldSortings,
                    buildFilteredPaginatedResourceRequestCallbackDefaultImpl(null));
        }

    }


    public void getProductsForFamily(String familyId,
                                     List<String> projection,
                                     Pagination pagination,
                                     ResourceFieldSorting resourceFieldSortings,
                                     List<?> resourceFilters) {
        productInteractor.getProductsForFamily(familyId,
                pagination.getOffset(),
                pagination.getLimit(),
                projection,
                resourceFieldSortings,
                (List<ResourceFilter<ResourceFilter, ResourceFilterValue>>) resourceFilters,
                buildFilteredPaginatedResourceRequestCallbackDefaultImpl(pagination));
    }

    @Override
    public void setView(FilterResourceView<List<Product>, ProductFilter> view) {
        getProductsView = view;
    }

    private FilteredPaginatedResourceRequestCallback buildFilteredPaginatedProductsRequestCallbackDefaultImpl() {
        return buildFilteredPaginatedResourceRequestCallbackDefaultImpl(null);
    }

    private FilteredPaginatedResourceRequestCallback buildFilteredPaginatedResourceRequestCallbackDefaultImpl(Pagination pagination) {

        return new AbstractFilteredPaginatedResourceRequestCallback(getProductsView,
                EResourceType.PRODUCTS,
                pagination,
                productsRequestCounter) {
            @Override
            public void onSuccess(List resource, Integer count, String previous, String next, List list) {
                this.onDefaultSuccess(resource, count, previous, next, list);
            }

            @Override
            public void onError(BaseException e) {
                this.onDefaultError(e);
            }
        };

    }

}
