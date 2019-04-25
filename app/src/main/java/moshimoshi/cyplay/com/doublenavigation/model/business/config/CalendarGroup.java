package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by romainlebouc on 09/05/16.
 */
@Parcel
public class CalendarGroup {

    String code;
    String label;
    String icon;
    List<String> event_types;

    Boolean all;

    public List<String> getEvent_types() {
        return event_types;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public String getIcon() {
        return icon;
    }

    public Boolean getAll() {
        return all;
    }
}
