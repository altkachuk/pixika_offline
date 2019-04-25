package moshimoshi.cyplay.com.doublenavigation.ui.custom.form;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.model.events.FakeEvent;

/**
 * Created by wentongwang on 01/06/2017.
 */

public abstract class FormEventBusView extends FormSubView {

    @Inject
    EventBus bus;

    public FormEventBusView(Context context) {
        this(context, null);
    }

    public FormEventBusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FormEventBusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        if (bus == null) {
            bus = EventBus.getDefault();
        }
        bus.register(this);


    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        switch (visibility) {
            case VISIBLE:

                if (bus != null) {
                    if (!bus.isRegistered(this)) {
                        bus.register(this);
                    }
                }
                break;
            case GONE:
            case INVISIBLE:

                if (bus != null) {
                    bus.unregister(this);
                }
                break;
        }
        super.onVisibilityChanged(changedView, visibility);

    }

    @Subscribe
    public void onFakeEvent(FakeEvent fakeEvent) {

    }

}
