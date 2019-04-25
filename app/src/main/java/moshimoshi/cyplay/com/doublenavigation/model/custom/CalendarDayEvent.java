package moshimoshi.cyplay.com.doublenavigation.model.custom;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.Event;
import moshimoshi.cyplay.com.doublenavigation.model.business.EventSource;

/**
 * Created by romainlebouc on 28/04/16.
 */
public class CalendarDayEvent {


    public final static Comparator<CalendarDayEvent> DEFAULT_CALENDAR_DAY_EVENT_COMPARATOR = new Comparator<CalendarDayEvent>() {
        @Override
        public int compare(CalendarDayEvent calendarDay2, CalendarDayEvent calendarDay1) {
            Date date2 = calendarDay2.getCalendarDay().getDate();
            Date date1 = calendarDay1.getCalendarDay().getDate();
            if (date1.equals(date2)) {
                return calendarDay2.getEvent().eventSource == EventSource.META.ordinal() ? -1 : 0;
            } else {
                return date2.compareTo(date1);
            }
        }
    };

    private CalendarDay calendarDay;
    private Event event;

    public CalendarDay getCalendarDay() {
        return calendarDay;
    }

    public CalendarDayEvent(Event event, CalendarDay calendarDay) {
        this.calendarDay = calendarDay;
        this.event = event;
    }

    public void setCalendarDay(CalendarDay calendarDay) {
        this.calendarDay = calendarDay;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public static CalendarDayEvent getFirstCalendarDayEventAfter(List<CalendarDayEvent> items, CalendarDay calendarDay) {
        CalendarDayEvent result = null;
        for (CalendarDayEvent calendarDayEvent : items) {
            if (calendarDayEvent.getCalendarDay().equals(calendarDay) ||
                    calendarDayEvent.getCalendarDay().isAfter(calendarDay)) {
                result = calendarDayEvent;
                break;
            }
        }
        return result;
    }

}