package moshimoshi.cyplay.com.doublenavigation.model.events;

import com.adyen.library.DeviceInfo;

/**
 * Created by romainlebouc on 10/03/2017.
 */

public class AdyenPOSChosenEvent {


    DeviceInfo deviceInfo;

    public AdyenPOSChosenEvent(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
}
