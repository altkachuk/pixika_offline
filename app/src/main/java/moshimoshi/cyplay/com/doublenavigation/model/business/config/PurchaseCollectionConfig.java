package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPurchaseCollectionMode;

/**
 * Created by wentongwang on 16/03/2017.
 */
@Parcel
public class PurchaseCollectionConfig {

    List<String> available_modes;


    public List<EPurchaseCollectionMode> getAvailable_modes() {
        List<EPurchaseCollectionMode> ePurchaseCollectionMode = new ArrayList<>();
        if (available_modes != null) {
            for (String code : available_modes) {
                ePurchaseCollectionMode.add(EPurchaseCollectionMode.getEDeliveryDestinationTypeFromCode(code));
            }
        } else {
            ePurchaseCollectionMode = Arrays.asList(EPurchaseCollectionMode.values());
        }
        return ePurchaseCollectionMode;
    }


}
