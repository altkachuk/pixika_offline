package moshimoshi.cyplay.com.doublenavigation.view;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.custom.CalendarDayEvent;

/**
 * Created by romainlebouc on 16/05/16.
 */
public interface GetEventsView extends BaseView{

    void showLoading();

    void onEventsSuccess(List<CalendarDayEvent> calendarDayEvents);

}
