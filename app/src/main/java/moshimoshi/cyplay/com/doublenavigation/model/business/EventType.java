package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

/**
 * Created by romainlebouc on 26/04/16.
 */
@Parcel
public class EventType {

    public String id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventType eventType = (EventType) o;

        return id != null ? id.equals(eventType.id) : eventType.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
