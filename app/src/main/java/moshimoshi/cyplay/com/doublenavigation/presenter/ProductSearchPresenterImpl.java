package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.filters.ProductFilter;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.SearchActionEventData;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractFilteredPaginatedResourceRequestCallback;
import moshimoshi.cyplay.com.doublenavigation.utils.ActionEventHelper;
import moshimoshi.cyplay.com.doublenavigation.view.FilterResourceView;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.business.enums.EActionStatus;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;
import ninja.cyplay.com.apilibrary.models.filters.ResourceFilter;
import ninja.cyplay.com.apilibrary.models.filters.ResourceFilterValue;
import ninja.cyplay.com.apilibrary.models.meta.ResourceFieldSorting;
import ninja.cyplay.com.apilibrary.models.meta.Pagination;
import ninja.cyplay.com.apilibrary.domain.interactor.ProductInteractor;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;

/**
 * Created by damien on 05/05/15.
 */
public class ProductSearchPresenterImpl extends BasePresenter implements ProductSearchPresenter {

    @Inject
    APIValue apiValue;

    @Inject
    ActionEventHelper actionEventHelper;

    private Context context;
    private FilterResourceView<List<Product>, ProductFilter> productSearchView;
    private ProductInteractor productInteractor;

    public ProductSearchPresenterImpl(Context context, ProductInteractor productInteractor) {
        super(context);
        this.context = context;
        this.productInteractor = productInteractor;
    }

    @Override
    public void setView(FilterResourceView<List<Product>, ProductFilter> view) {
        this.productSearchView = view;
    }

    private final AtomicInteger productSearchCounter = new AtomicInteger();


    @Override
    public void searchProduct(final String product,
                              List<String> projection,
                              final Pagination pagination,
                              final ResourceFieldSorting resourceFieldSorting,
                              final List<?> resourceFilters) {

        productInteractor.searchByName(product,
                pagination.getOffset(),
                pagination.getLimit(),
                projection,
                resourceFieldSorting,
                (List<ResourceFilter<ResourceFilter, ResourceFilterValue>>) resourceFilters,

                new AbstractFilteredPaginatedResourceRequestCallback(productSearchView,
                        EResourceType.PRODUCTS,
                        pagination,
                        productSearchCounter) {
                    @Override
                    public void onSuccess(List resource, Integer count, String previous, String next, List list) {
                        this.onDefaultSuccess(resource, count, previous, next, list);
                        // Log event
                        // We monitor only one search event and not all the next pagination requests
                        if (pagination == null || !pagination.isExtraData()) {
                            actionEventHelper.log(new SearchActionEventData()
                                    .setResource(EResource.PRODUCT)
                                    .setValue(product)
                                    .setStatus(EActionStatus.SUCCESS)
                                    .addSearchQueryParams("q", product)
                                    .addSortParams(resourceFieldSorting)
                                    .addFilterParams(resourceFilters)
                                    .addPaginationParams(pagination));
                        }
                    }

                    @Override
                    public void onError(BaseException e) {
                        this.onDefaultError(e);
                        if (pagination == null || !pagination.isExtraData()) {
                            actionEventHelper.log(new SearchActionEventData()
                                    .setResource(EResource.PRODUCT)
                                    .setValue(product)
                                    .setStatus(EActionStatus.FAILURE)
                                    .addSearchQueryParams("q", product)
                                    .addSortParams(resourceFieldSorting)
                                    .addFilterParams(resourceFilters)
                                    .addPaginationParams(pagination));
                        }
                    }
                }


        );


    }

}
