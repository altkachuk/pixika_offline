package moshimoshi.cyplay.com.doublenavigation.ui.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.LoadingView;
import ninja.cyplay.com.apilibrary.models.meta.Pagination;

/**
 * Created by damien on 18/08/16.
 */
public abstract class PaginatedResourceBaseFragment<Resource> extends BaseFragment {

    @Inject
    protected EventBus bus;

    @Nullable
    @BindView(R.id.resource_loading_view)
    protected LoadingView loadingView;

    @Nullable
    @BindView(R.id.reload_button)
    View reloadButton;

    @Nullable
    @BindView(R.id.default_loading_more_view)
    View loadingMore;

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (loadingView != null) {
            loadingView.setLoadingState(LoadingView.LoadingState.LOADING);
        }
        if (reloadButton != null) {
            reloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadResource(Pagination.getInitialPagingation());
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (bus != null) {
            bus.unregister(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bus != null) {
            bus.register(this);
        }

        List<Resource> resource = this.getCachedResource();
        this.setResource(resource, false);
        if (resource != null && loadingView != null) {
            loadingView.setLoadingState(LoadingView.LoadingState.LOADED);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        loadingMore.setVisibility(View.GONE);
    }

    protected abstract void setResource(List<Resource> resource, boolean extra);

    public abstract List<Resource> getCachedResource();

    public abstract void loadResource(Pagination pagination);

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    public void onResourceResponse(final ResourceResponseEvent<List<Resource>> resourceResponseEvent) {
        this.setResource(resourceResponseEvent.getResource(),
                resourceResponseEvent.getPagination() != null && resourceResponseEvent.getPagination().isExtraData());

        if (resourceResponseEvent.getPagination() != null && resourceResponseEvent.getPagination().isExtraData()) {
            if (loadingMore != null) {
                loadingMore.setVisibility(View.GONE);
            }
            if (loadingView != null) {
                loadingView.setLoadingState(LoadingView.LoadingState.LOADED);
            }
        } else {
            if (resourceResponseEvent.getResource() != null && loadingView != null) {
                if (resourceResponseEvent.getResource() instanceof List && ((List) resourceResponseEvent.getResource()).isEmpty()) {
                    loadingView.setLoadingState(LoadingView.LoadingState.NO_RESULT);
                } else {
                    loadingView.setLoadingState(LoadingView.LoadingState.LOADED);
                }
            } else if (resourceResponseEvent.getResourceException() != null && loadingView != null) {
                loadingView.setLoadingState(LoadingView.LoadingState.FAILED);
                loadingMore.setVisibility(View.GONE);
            }
        }

    }

    public void onResourceRequest(final ResourceRequestEvent<List<Resource>> resourceRequestEvent, boolean extra) {
        if (resourceRequestEvent != null && !extra) {
            loadingView.setLoadingState(LoadingView.LoadingState.LOADING);
        }
        if (loadingMore != null) {
            loadingMore.setVisibility(extra ? View.VISIBLE : View.GONE);
        }
    }

}