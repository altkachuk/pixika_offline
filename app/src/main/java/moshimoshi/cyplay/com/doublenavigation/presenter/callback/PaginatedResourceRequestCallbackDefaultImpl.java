package moshimoshi.cyplay.com.doublenavigation.presenter.callback;

import android.content.Context;
import android.util.Log;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceException;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.domain.interactor.callback.PaginatedResourceRequestCallback;
import ninja.cyplay.com.apilibrary.domain.interactor.callback.RequestCallbackLoger;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.meta.Pagination;
import ninja.cyplay.com.apilibrary.utils.LogUtils;

/**
 * Created by romainlebouc on 09/08/16.
 */
public class PaginatedResourceRequestCallbackDefaultImpl<Resource extends AbstractResource, AbstractResource> extends AbstractPaginatedResourceRequestCallbackImpl<AbstractResource> implements PaginatedResourceRequestCallback<AbstractResource> {


    private Context context;
    private ResourceView<List<Resource>> resourceView;
    private EResourceType eResourceType;
    private Pagination pagination;
    private RequestCallbackLoger requestCallbackLoger;


    public PaginatedResourceRequestCallbackDefaultImpl(Context context,
                                                       ResourceView<List<Resource>> resourceView,
                                                       EResourceType eResourceType
    ) {
        this(context, resourceView, eResourceType, null);
    }

    public PaginatedResourceRequestCallbackDefaultImpl(Context context,
                                                       RequestCallbackLoger requestCallbackLoger,
                                                       ResourceView<List<Resource>> resourceView,
                                                       EResourceType eResourceType
    ) {
        this(context, resourceView, eResourceType, null);
        this.requestCallbackLoger = requestCallbackLoger;
    }


    public PaginatedResourceRequestCallbackDefaultImpl(Context context,
                                                       ResourceView<List<Resource>> resourceView,
                                                       EResourceType eResourceType,
                                                       Pagination pagination) {
        this.context = context;
        this.resourceView = resourceView;
        this.eResourceType = eResourceType;
        this.pagination = pagination;
    }

    public void onSuccess(final List<AbstractResource> resource, final Integer count, final String previous, final String next) {
        super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
            @Override
            public void execute() {
                ResourceResponseEvent<List<Resource>> resourceResourceResponseEvent = new ResourceResponseEvent<>((List<Resource>) resource,
                        null,
                        eResourceType,
                        previous,
                        next,
                        count,
                        pagination
                );

                resourceView.onResourceViewResponse(resourceResourceResponseEvent);
                if (requestCallbackLoger != null) {
                    requestCallbackLoger.log(true);
                }
            }
        });


    }

    public void onError(final BaseException e) {
        super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
            @Override
            public void execute() {
                Log.e(LogUtils.generateTag(this), "Error on PaginatedResourceRequestCallbackDefaultImpl");
                ResourceResponseEvent<List<Resource>> resourceResourceResponseEvent = new ResourceResponseEvent<>(null, new ResourceException(e), eResourceType);
                resourceView.onResourceViewResponse(resourceResourceResponseEvent);
                if (requestCallbackLoger != null) {
                    requestCallbackLoger.log(false);
                }
            }
        });

    }


}
