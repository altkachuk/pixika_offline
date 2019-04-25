package moshimoshi.cyplay.com.doublenavigation.ui.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;
import moshimoshi.cyplay.com.doublenavigation.model.events.FakeEvent;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;


/**
 * Created by damien on 13/04/16.
 */
public class BaseFragment extends Fragment {

    @Inject
    protected ConfigHelper configHelper;

    @Inject
    protected EventBus bus;

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        injectViews(view);
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    protected void injectDependencies() {
        PlayRetailApp.get(getContext()).inject(this);
    }

    private void injectViews(View view) {
        ButterKnife.bind(this, view);
    }

    //TODO : remove that
    @Subscribe
    public void onFakeEvent(FakeEvent fakeEvent){

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isBusActivated() && bus != null) {
            bus.register(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isBusActivated() && bus != null) {
            bus.unregister(this);
        }
    }


    protected boolean isBusActivated(){
        return false;
    }


}
