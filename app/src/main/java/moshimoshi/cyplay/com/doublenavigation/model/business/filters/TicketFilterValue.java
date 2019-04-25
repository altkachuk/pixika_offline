package moshimoshi.cyplay.com.doublenavigation.model.business.filters;

import org.parceler.Parcel;

import ninja.cyplay.com.apilibrary.models.filters.ResourceFilterValue;

/**
 * Created by romainlebouc on 18/09/16.
 */
@Parcel
public class TicketFilterValue extends ResourceFilterValue {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TicketFilterValue that = (TicketFilterValue) o;

        if (label != null ? !label.equals(that.label) : that.label != null) return false;
        return key != null ? key.equals(that.key) : that.key == null;

    }


}
