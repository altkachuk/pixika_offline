package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by romainlebouc on 14/08/16.
 */
@Parcel
public class Home extends PR_AHome {
    List<Shortcut> shortcuts;

    public List<Shortcut> getShortcuts() {
        return shortcuts;
    }

    public HomeEvent events;

    public HomeEvent getEvents() {
        return events;
    }
}
