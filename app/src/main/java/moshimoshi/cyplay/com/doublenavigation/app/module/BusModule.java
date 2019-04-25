package moshimoshi.cyplay.com.doublenavigation.app.module;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import dagger.Module;
import dagger.Provides;

/**
 * Created by romainlebouc on 05/08/16.
 */
@Module
public class BusModule {
    private final EventBus bus;

    public BusModule(Context context) {
        this.bus = new EventBus();
    }

    @Provides
    public EventBus provideBus() {
        return bus;
    }
}
