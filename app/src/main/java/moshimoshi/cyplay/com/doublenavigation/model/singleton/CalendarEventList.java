package moshimoshi.cyplay.com.doublenavigation.model.singleton;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.Event;
import moshimoshi.cyplay.com.doublenavigation.model.business.EventSource;
import moshimoshi.cyplay.com.doublenavigation.model.custom.DateRange;
import moshimoshi.cyplay.com.doublenavigation.model.custom.TimeDirection;
import moshimoshi.cyplay.com.doublenavigation.utils.DateUtils;
import ninja.cyplay.com.apilibrary.domain.interactor.EventInteractor;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;

/**
 * Created by romainlebouc on 03/05/16.
 */
public class CalendarEventList extends EventList {

    private final static int MORE_EVENTS_YEAR_COUNT = 2;
    private final static int INITIAL_YEARS_RANGE = 1;

    public CalendarEventList(EventBus bus, EventInteractor eventInteractor, SellerContext sellerContext, APIValue apiValue) {
        super(bus, eventInteractor, sellerContext, apiValue);
    }

    @Override
    protected DateRange getDateRange(TimeDirection timeDirection, DateRange currentDateRange) {
        DateRange extension = null;
        switch (timeDirection) {
            case PRESENT:
                currentDateRange.setOneDateRange(DateUtils.getFirstDayOfCurrentMonth());
                extension = currentDateRange.extendsRange(INITIAL_YEARS_RANGE);
                break;
            case PAST:
                extension = currentDateRange.extendsRangeInThePast(MORE_EVENTS_YEAR_COUNT);
                break;
            case FUTURE:
                extension = currentDateRange.extendsRangeInTheFuture(MORE_EVENTS_YEAR_COUNT);
                break;
        }
        return extension;
    }

    @Override
    public boolean shouldAddMetaEvents() {
        return true;
    }

    @Override
    public void loadEvents(final TimeDirection timeDirection, Date after, Date before) {
        if (!gettingMoreEvent) {
            gettingMoreEvent = true;
            new EventLoaderAsyncTask(timeDirection).execute(timeDirection);
        }
    }

    /**
     * Build and id a list of fake events for each month in the loaded range
     *
     * @return
     */
    protected List<Event> getMetaEvents(DateRange range) {
        List<Event> result = new ArrayList<>();

        Calendar minCalendar = Calendar.getInstance();
        minCalendar.setTime(range.getMinDate());

        Calendar maxCalendar = Calendar.getInstance();
        maxCalendar.setTime(range.getMaxDate());

        Calendar c = Calendar.getInstance();
        c.setTime(range.getMinDate());

        while (c.before(maxCalendar)) {
            if (c.equals(minCalendar) || c.after(minCalendar)) {
                Event e = new Event();
                e.start_date = Event.IN_FORMAT.format(c.getTime());
                e.eventSource = EventSource.META.ordinal();
                e.summary = new SimpleDateFormat("MMMM yyyy").format(c.getTime());
                result.add(e);
            }
            c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
        }

        return result;
    }
}
