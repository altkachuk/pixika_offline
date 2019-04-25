package moshimoshi.cyplay.com.doublenavigation.model.events;

import moshimoshi.cyplay.com.doublenavigation.model.custom.CalendarDayEvent;

/**
 * Created by romainlebouc on 19/04/16.
 */
public class CalendarDateScrolledEvent {

    private final CalendarDayEvent calendarDayEvent;
    private final boolean setEventDateAsSelected;

    public CalendarDateScrolledEvent(CalendarDayEvent calendarDayEvent, boolean setEventDateAsSelected) {
        this.calendarDayEvent = calendarDayEvent;
        this.setEventDateAsSelected = setEventDateAsSelected;
    }

    public CalendarDayEvent getCalendarDayEvent() {
        return calendarDayEvent;
    }

    public boolean isSetEventDateAsSelected() {
        return setEventDateAsSelected;
    }
}
