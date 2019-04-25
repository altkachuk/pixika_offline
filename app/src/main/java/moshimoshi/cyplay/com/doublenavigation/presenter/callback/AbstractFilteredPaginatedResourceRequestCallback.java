package moshimoshi.cyplay.com.doublenavigation.presenter.callback;

import android.util.Log;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.events.FilterResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceException;
import moshimoshi.cyplay.com.doublenavigation.view.FilterResourceView;
import ninja.cyplay.com.apilibrary.domain.interactor.callback.FilteredPaginatedResourceRequestCallback;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.meta.Pagination;
import ninja.cyplay.com.apilibrary.utils.LogUtils;

/**
 * Created by romainlebouc on 09/08/16.
 */
public abstract class AbstractFilteredPaginatedResourceRequestCallback<Resource, AbstractFilter> implements FilteredPaginatedResourceRequestCallback<Resource, AbstractFilter> {


    private FilterResourceView<List<Resource>, AbstractFilter> resourceView;
    private EResourceType eResourceType;
    private Pagination pagination;
    private AtomicInteger requestCounter;
    private final int requestId;

    public AbstractFilteredPaginatedResourceRequestCallback(FilterResourceView<List<Resource>, AbstractFilter> resourceView,
                                                            EResourceType eResourceType,
                                                            Pagination pagination) {
        this.resourceView = resourceView;
        this.eResourceType = eResourceType;
        this.pagination = pagination;
        requestId = 0;
    }

    public AbstractFilteredPaginatedResourceRequestCallback(FilterResourceView<List<Resource>, AbstractFilter> resourceView,
                                                            EResourceType eResourceType,
                                                            Pagination pagination,
                                                            AtomicInteger requestCounter) {
        this.resourceView = resourceView;
        this.eResourceType = eResourceType;
        this.pagination = pagination;
        this.requestCounter = requestCounter;
        requestId = requestCounter.incrementAndGet();
    }

    public abstract void onSuccess(List<Resource> resource,
                                   Integer count,
                                   String previous,
                                   String next,
                                   List<AbstractFilter> filters);

    public abstract void onError(BaseException e);

    public void onSafeExecute(SafeResourceRequestCallbackExecutor safeResourceRequestCallbackExecutor) {

        if (safeResourceRequestCallbackExecutor != null) {
            try {
                safeResourceRequestCallbackExecutor.execute();
            } catch (Exception e) {
                Log.e(LogUtils.generateTag(this), "Error will executing Resource Callback");
            }
        }

    }

    public void onDefaultError(final BaseException e) {

        this.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
            @Override
            public void execute() {
                if (requestCounter == null || requestId == requestCounter.get()) {
                    Log.e(LogUtils.generateTag(this), "Error on PaginatedResourceRequestCallbackDefaultImpl");
                    FilterResourceResponseEvent<List<Resource>, AbstractFilter> resourceResourceResponseEvent = new FilterResourceResponseEvent<>(null, new ResourceException(e), eResourceType);
                    resourceView.onResourceViewResponse(resourceResourceResponseEvent);
                }
            }
        });

    }

    public void onDefaultSuccess(final List<Resource> resource,
                                 final Integer count,
                                 final String previous,
                                 final String next,
                                 final List<AbstractFilter> filters) {
        this.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
            @Override
            public void execute() {
                if (requestCounter == null || requestId == requestCounter.get()) {
                    FilterResourceResponseEvent<List<Resource>, AbstractFilter> resourceResourceResponseEvent = new FilterResourceResponseEvent<>(resource,
                            null,
                            eResourceType,
                            previous,
                            next,
                            count,
                            pagination,
                            filters
                    );

                    resourceView.onResourceViewResponse(resourceResourceResponseEvent);
                }
            }
        });


    }


}
