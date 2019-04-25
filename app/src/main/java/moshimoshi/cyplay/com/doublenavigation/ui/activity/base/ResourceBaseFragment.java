package moshimoshi.cyplay.com.doublenavigation.ui.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.LoadingView;

/**
 * Created by romainlebouc on 11/08/16.
 */
public abstract class ResourceBaseFragment<Resource> extends BaseFragment {

    @Inject
    protected EventBus bus;

    @Nullable
    @BindView(R.id.resource_loading_view)
    public LoadingView loadingView;

    @Nullable
    @BindView(R.id.reload_button)
    View reloadButton;

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (loadingView != null) {
            loadingView.setLoadingState(LoadingView.LoadingState.LOADING);
        }
        if (reloadButton != null) {
            reloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ResourceBaseFragment.this.loadResource();
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bus != null) {
            bus.register(this);
        }
        Resource resource = this.getCachedResource();
        this.setResource(resource);
        if (loadingView !=null && resource != null && resource instanceof Collection && ((Collection) resource).isEmpty()) {
            loadingView.setLoadingState(LoadingView.LoadingState.NO_RESULT);
        } else if (resource != null && loadingView != null) {
            loadingView.setLoadingState(LoadingView.LoadingState.LOADED);
        } else if (loadingView != null) {
            loadingView.setLoadingState(LoadingView.LoadingState.LOADING);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (bus != null) {
            bus.unregister(this);
        }
    }

    public abstract void onResourceResponseEvent(final ResourceResponseEvent<Resource> resourceResponseEvent);

    public abstract void onResourceRequestEvent(final ResourceRequestEvent<Resource> resourceRequestEvent);

    protected abstract void setResource(Resource resource);

    public abstract Resource getCachedResource();

    public abstract void loadResource();

    public void onResourceResponse(final ResourceResponseEvent<Resource> resourceResponseEvent) {
        if (this.getActivity() != null && isAdded()) {
            this.setResource(resourceResponseEvent.getResource());
            if (resourceResponseEvent.getResource() != null && loadingView != null) {
                if (resourceResponseEvent.getResource() instanceof List && ((List) resourceResponseEvent.getResource()).isEmpty()) {
                    loadingView.setLoadingState(LoadingView.LoadingState.NO_RESULT);
                } else {
                    loadingView.setLoadingState(LoadingView.LoadingState.LOADED);
                }
            } else if (resourceResponseEvent.getResource() == null && resourceResponseEvent.getResourceException() == null) {
                loadingView.setLoadingState(LoadingView.LoadingState.NO_RESULT);
            } else if (resourceResponseEvent.getResourceException() != null && loadingView != null) {
                loadingView.setLoadingState(LoadingView.LoadingState.FAILED);
            }
        }
    }

    public void onResourceRequest(final ResourceRequestEvent<Resource> resourceRequestEvent) {
        if (this.getActivity() != null && isAdded()) {
            if (resourceRequestEvent != null && loadingView != null) {
                loadingView.setLoadingState(LoadingView.LoadingState.LOADING);
            }
        }

    }
}
