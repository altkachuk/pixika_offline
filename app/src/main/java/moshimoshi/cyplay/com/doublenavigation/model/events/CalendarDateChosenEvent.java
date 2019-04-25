package moshimoshi.cyplay.com.doublenavigation.model.events;

import com.prolificinteractive.materialcalendarview.CalendarDay;

/**
 * Created by romainlebouc on 19/04/16.
 */
public class CalendarDateChosenEvent {

    private final CalendarDay day;

    public CalendarDateChosenEvent(CalendarDay day) {
        this.day = day;
    }

    public CalendarDay getDay() {
        return day;
    }

}
