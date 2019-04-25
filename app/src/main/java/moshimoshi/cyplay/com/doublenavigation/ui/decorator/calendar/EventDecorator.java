package moshimoshi.cyplay.com.doublenavigation.ui.decorator.calendar;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

/**
 * Decorate several days with a dot
 */
public class EventDecorator implements DayViewDecorator {

    private int color;
    private final HashSet<CalendarDay> dates;

    private final int size;

    public EventDecorator(int color, Collection<CalendarDay> dates, int size) {
        this.color = color;
        this.dates = new HashSet<>(dates);
        this.size = size;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(size, color));
    }

    public HashSet<CalendarDay> getDates() {
        return dates;
    }
}

