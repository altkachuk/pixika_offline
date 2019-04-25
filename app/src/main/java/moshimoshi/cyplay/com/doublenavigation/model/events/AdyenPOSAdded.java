package moshimoshi.cyplay.com.doublenavigation.model.events;

import com.adyen.library.DeviceInfo;

/**
 * Created by romainlebouc on 25/11/2016.
 */

public class AdyenPOSAdded {

    final DeviceInfo deviceInfo;

    public AdyenPOSAdded(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
}
