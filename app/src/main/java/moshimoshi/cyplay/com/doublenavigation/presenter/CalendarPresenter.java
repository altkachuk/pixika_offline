package moshimoshi.cyplay.com.doublenavigation.presenter;


import java.util.Date;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.Event;
import moshimoshi.cyplay.com.doublenavigation.view.CalendarView;


/**
 * Created by romainlebouc on 26/04/16.
 */
public interface CalendarPresenter extends Presenter<CalendarView> {

    void getEvents();

    void getEvents(Date before, Date after);

    void onEvents(List<Event> events);

}

