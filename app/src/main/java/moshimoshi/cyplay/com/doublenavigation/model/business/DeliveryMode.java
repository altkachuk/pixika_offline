package moshimoshi.cyplay.com.doublenavigation.model.business;

import android.content.Context;

import org.parceler.Parcel;

import java.util.Arrays;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.R;
import ninja.cyplay.com.apilibrary.models.ModelResource;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ADeliveryMode;

/**
 * Created by wentongwang on 24/03/2017.
 */
@Parcel
@ModelResource
public class DeliveryMode extends PR_ADeliveryMode {

    public transient final static List<String> DELIVERYMODE_PROJECTION = Arrays.asList("id",
            "name",
            "description",
            "target_delivery_types",
            "shipping_time"
    );

    String name;
    String description;
    List<String> target_delivery_types;
    ShippingTime shipping_time;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getTarget_delivery_types() {
        return target_delivery_types;
    }

    public ShippingTime getShipping_time() {
        return shipping_time != null ? shipping_time : new ShippingTime();
    }

    public String getShippingFormatTime(Context context) {
        StringBuilder sb = new StringBuilder();
        if (getShipping_time().getDays() > 0) {
            sb.append(context.getString(R.string.delivery_shipping_time_day, getShipping_time().getDays()));
        }

        if (getShipping_time().getHours() > 0) {
            sb.append(context.getString(R.string.delivery_shipping_time_hour, getShipping_time().getHours()));
        }

        if (getShipping_time().getMinutes() > 0) {
            sb.append(context.getString(R.string.delivery_shipping_time_min, getShipping_time().getMinutes()));
        }

        return sb.toString();

    }
}
