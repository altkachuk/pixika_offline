package moshimoshi.cyplay.com.doublenavigation.app.component;

import org.greenrobot.eventbus.EventBus;

import dagger.Component;
import moshimoshi.cyplay.com.doublenavigation.app.module.BusModule;


/**
 * Created by romainlebouc on 05/08/16.
 */
@Component(
        modules = {
                BusModule.class
        }
)
public interface BusComponent {
    EventBus provideBus(); //provision method

}
