package moshimoshi.cyplay.com.doublenavigation.model.singleton;


import android.content.Context;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import moshimoshi.cyplay.com.doublenavigation.model.business.EventSource;
import moshimoshi.cyplay.com.doublenavigation.model.business.Event;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.CalendarGroup;
import moshimoshi.cyplay.com.doublenavigation.model.custom.CalendarDayEvent;
import moshimoshi.cyplay.com.doublenavigation.model.custom.DateRange;
import moshimoshi.cyplay.com.doublenavigation.model.custom.TimeDirection;
import moshimoshi.cyplay.com.doublenavigation.model.events.EventsListRefresh;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractPaginatedResourceRequestCallbackImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.SafeResourceRequestCallbackExecutor;
import ninja.cyplay.com.apilibrary.domain.interactor.EventInteractor;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AEvent;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;


/**
 * Created by romainlebouc on 26/04/16.
 */
public abstract class EventList extends CalendarDayEventList {

    private String currentEventGroup = null;

    @Override
    protected boolean shouldGroupAllEvents() {
        return false;
    }

    // Dagger dependencies
    public final EventBus bus;
    public SellerContext sellerContext;
    private EventInteractor eventInteractor;

    // Flag which is true when events are loading
    protected boolean gettingMoreEvent = false;

    public DateRange getLoadedRange() {
        return loadedRange;
    }

    private final Context context;


    public EventList(EventBus bus, EventInteractor eventInteractor, SellerContext sellerContext, APIValue apiValue) {
        super(apiValue);
        this.bus = bus;
        this.sellerContext = sellerContext;
        this.eventInteractor = eventInteractor;
        this.apiValue = apiValue;
        super.setCalendarDayEventListLoadedListener(new CalendarDayEventListLoadedListener() {
            @Override
            public void onCalendarDayEventListLoaded(EventsListRefresh eventsListRefresh) {
                EventList.this.bus.post(eventsListRefresh);
                gettingMoreEvent = false;
            }
        });
        this.context = apiValue.getContext();
    }

    public String getCurrentEventGroup() {
        return currentEventGroup;
    }

    public void setCurrentEventGroup(String currentEventGroup) {
        this.currentEventGroup = currentEventGroup;
    }

    /**
     * Clear event list events
     */
    public void clear() {
        rawEvents.clear();
        dayEventsByEventType.clear();

        CalendarGroup allCalendarGroup = null ;//PlayRetailApp.get(context).getConfigHelper().getAllCalendarGroup();
        if (allCalendarGroup != null) {
            dayEventsByEventType.put(allCalendarGroup.getCode(), new ArrayList<CalendarDayEvent>());
        }


    }

    /**
     * Get all the events in the loaded range for an event Type Group
     *
     * @param eventTypeGroup
     * @return
     */
    public List<CalendarDayEvent> getCalendarDayEvents(String eventTypeGroup) {

        List<CalendarDayEvent> result = dayEventsByEventType.get(eventTypeGroup);
        if (result == null) {
            result = new ArrayList<>();
            dayEventsByEventType.put(eventTypeGroup, result);
        }
        return result;
    }

    /**
     * Get a list of CalendarDay for one type and one source
     *
     * @param eventTypeGroup
     * @param eventSource
     * @return
     */
    public List<CalendarDay> getCalendarDays(String eventTypeGroup, EventSource eventSource) {


        List<CalendarDayEvent> calendarDayEvents = this.getCalendarDayEvents(eventTypeGroup);
        List<CalendarDay> calendarDays = new ArrayList<>();
        for (CalendarDayEvent calendarDayEvent : calendarDayEvents) {
            switch (eventSource) {
                case ALL:
                    calendarDays.add(calendarDayEvent.getCalendarDay());
                    break;
                case REAL:
                    if (calendarDayEvent.getEvent().eventSource == EventSource.REAL.ordinal()) {
                        calendarDays.add(calendarDayEvent.getCalendarDay());
                    }
                    break;
                case META:
                    if (calendarDayEvent.getEvent().eventSource == EventSource.META.ordinal()) {
                        calendarDays.add(calendarDayEvent.getCalendarDay());
                    }
                    break;
            }
        }
        return calendarDays;
    }

    public abstract void loadEvents(final TimeDirection timeDirection, Date after, Date before);

    public void loadEvents(final TimeDirection timeDirection) {
        this.loadEvents(timeDirection, null, null);
    }

    public void getEvents(final Date after, final Date before) {

        eventInteractor.getEvents(sellerContext.getSeller().getId(),

                after,
                before,
                new AbstractPaginatedResourceRequestCallbackImpl<PR_AEvent>() {
                    @Override
                    public void onSuccess(final List<PR_AEvent> resource, Integer count, String previous, String next) {
                        super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                            @Override
                            public void execute() {
                                EventList.this.rawEvents.addAll((List<Event>) (List<?>) resource);
                                EventList.this.loadEvents(TimeDirection.PRESENT, after, before);
                            }
                        });

                    }

                    @Override
                    public void onError(final BaseException e) {
                        super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                            @Override
                            public void execute() {
                                bus.post(new EventsListRefresh(e.getType(), e.getResponse().getStatus(), e.getResponse().getDetail()));
                            }
                        });

                    }
                });
    }

}
