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

/**
 * Created by romainlebouc on 14/08/16.
 */
public abstract class ResourceBaseActivity <Resource> extends MenuBaseActivity {
    @Inject
    protected EventBus bus;

    @Nullable
    @BindView(R.id.resource_loading_view)
    LoadingView loadingView;

    @Nullable
    @BindView(R.id.reload_button)
    View reloadButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ( loadingView !=null){
            loadingView.setLoadingState(LoadingView.LoadingState.LOADING);
        }
        if ( reloadButton!=null){
            reloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ResourceBaseActivity.this.loadResource();
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Resource resource = this.getCachedResource();
        this.setResource(resource);
        if (resource!=null && loadingView!=null ){
            loadingView.setLoadingState(LoadingView.LoadingState.LOADED);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    protected abstract void setResource(Resource resource);
    public abstract Resource getCachedResource();
    public abstract void loadResource();

    public void onResourceResponse(final ResourceResponseEvent<Resource> resourceResponseEvent) {
        this.setResource(resourceResponseEvent.getResource());
        if (resourceResponseEvent.getResource()!=null && loadingView!=null) {
            if ( resourceResponseEvent.getResource() instanceof List && ((List)resourceResponseEvent.getResource()).isEmpty()){
                loadingView.setLoadingState(LoadingView.LoadingState.NO_RESULT);
            }else{
                loadingView.setLoadingState(LoadingView.LoadingState.LOADED);
            }
        }else if(resourceResponseEvent.getResourceException() !=null && loadingView!=null){
            loadingView.setLoadingState(LoadingView.LoadingState.FAILED);
        }
    }

    public void onResourceRequest(final ResourceRequestEvent<Resource> resourceRequestEvent){
        if (resourceRequestEvent !=null){
            loadingView.setLoadingState(LoadingView.LoadingState.LOADING);
        }
    }
}
