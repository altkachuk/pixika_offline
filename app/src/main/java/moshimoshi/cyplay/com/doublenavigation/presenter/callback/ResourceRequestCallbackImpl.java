package moshimoshi.cyplay.com.doublenavigation.presenter.callback;

import android.content.Context;

import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceException;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;

/**
 * Created by romainlebouc on 03/01/2017.
 */

public class ResourceRequestCallbackImpl<Resource> extends AbstractResourceRequestCallback<Resource> {

    private final ResourceView<Resource> resourceView;
    private final EResourceType eResourceType;

    public ResourceRequestCallbackImpl(Context context, ResourceView<Resource> resourceView, EResourceType eResourceType) {
        super();
        this.resourceView = resourceView;
        this.eResourceType = eResourceType;
    }

    @Override
    public void onSuccess(final Resource customer) {
        super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
            @Override
            public void execute() {
                resourceView.onResourceViewResponse(new ResourceResponseEvent<>(customer, null, eResourceType));
            }
        });

    }

    @Override
    public void onError(final BaseException e) {

        super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                                @Override
                                public void execute() {
                                    resourceView.onResourceViewResponse(new ResourceResponseEvent<Resource>(null, new ResourceException(e), eResourceType));
                                }
                            }
        );
    }


}
