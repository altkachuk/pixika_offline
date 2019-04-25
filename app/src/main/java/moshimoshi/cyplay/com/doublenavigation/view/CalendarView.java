package moshimoshi.cyplay.com.doublenavigation.view;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.custom.CalendarDayEvent;

/**
 * Created by romainlebouc on 26/04/16.
 */
public interface CalendarView extends BaseView {

    void onEventsSuccess(List<CalendarDayEvent> events);

}
