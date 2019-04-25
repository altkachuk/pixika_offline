package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by romainlebouc on 09/05/16.
 */
@Parcel
public class Calendar {

    List<CalendarGroup> groups;

    public List<CalendarGroup> getGroups() {
        return groups;
    }
}
