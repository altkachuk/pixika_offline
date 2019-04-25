package moshimoshi.cyplay.com.doublenavigation.model.events;

import java.util.List;
import java.util.Map;

import moshimoshi.cyplay.com.doublenavigation.model.custom.CalendarDayEvent;
import moshimoshi.cyplay.com.doublenavigation.model.custom.TimeDirection;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;

/**
 * Created by romainlebouc on 26/04/16.
 */
public class EventsListRefresh {

    // Success
    private TimeDirection timeDirection;
    private int previousCount;
    private int count;
    private Map<String, List<CalendarDayEvent>> calendarDayEventByGroup;

    // Error
    private ExceptionType exceptionType;
    private String status;
    private String message;


    public EventsListRefresh(TimeDirection timeDirection,
                             int previousCount,
                             int count,
                             Map<String, List<CalendarDayEvent>> calendarDayEventByGroup) {
        this.timeDirection = timeDirection;
        this.previousCount = previousCount;
        this.count = count;
        this.calendarDayEventByGroup = calendarDayEventByGroup;
    }

    public EventsListRefresh(ExceptionType exceptionType, String status, String message) {
        this.exceptionType = exceptionType;
        this.status = status;
        this.message = message;
    }

    public TimeDirection getTimeDirection() {
        return timeDirection;
    }

    public int getCount() {
        return count;
    }

    public int getPreviousCount() {
        return previousCount;
    }

    public ExceptionType getExceptionType() {
        return exceptionType;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, List<CalendarDayEvent>> getCalendarDayEventByGroup() {
        return calendarDayEventByGroup;
    }
}
