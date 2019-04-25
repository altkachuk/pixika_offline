package moshimoshi.cyplay.com.doublenavigation.view;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.custom.CalendarDayEvent;

/**
 * Created by romainlebouc on 02/05/16.
 */
public interface SellerEventsView extends BaseView {

    void onEventsSuccess(List<CalendarDayEvent> events);

}
