package moshimoshi.cyplay.com.doublenavigation.model.singleton;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import moshimoshi.cyplay.com.doublenavigation.model.business.Event;
import moshimoshi.cyplay.com.doublenavigation.model.business.EventSource;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.Calendar;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.Config;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EventRRuleFreq;
import moshimoshi.cyplay.com.doublenavigation.model.events.EventsListRefresh;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.CalendarGroup;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.ConfigFeature;
import moshimoshi.cyplay.com.doublenavigation.model.custom.CalendarDayEvent;
import moshimoshi.cyplay.com.doublenavigation.model.custom.DateRange;
import moshimoshi.cyplay.com.doublenavigation.model.custom.TimeDirection;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;

/**
 * Created by romainlebouc on 16/05/16.
 */
public abstract class CalendarDayEventList {

    // Raw events from the server
    protected final List<Event> rawEvents = new ArrayList<>();

    // Date range on which the events are loaded
    protected DateRange loadedRange = new DateRange();

    /**
     * Indicate whether or not meta events should be added (for instance, months, days, etc...)
     *
     * @return
     */
    public abstract boolean shouldAddMetaEvents();

    /**
     * The list of meta events to add
     *
     * @param range
     * @return
     */
    protected abstract List<Event> getMetaEvents(DateRange range);

    /**
     * The date range on which events should be loaded
     *
     * @param timeDirection
     * @param currentDateRange
     * @return
     */
    protected abstract DateRange getDateRange(TimeDirection timeDirection, DateRange currentDateRange);

    /**
     * Indicate if events should be treated in differents groups or only in one group
     *
     * @return
     */
    protected abstract boolean shouldGroupAllEvents();

    /**
     * Get current selected event group
     *
     * @return
     */
    public abstract String getCurrentEventGroup();

    protected Map<String, String> eventTypeToEventGroup = new HashMap<>();

    protected APIValue apiValue;
    private final Context context;

    public CalendarDayEventList(APIValue apiValue) {
        this.apiValue = apiValue;
        this.context = apiValue.getContext();

    }

    // Map containing the list of events for each type
    protected Map<String, List<CalendarDayEvent>> dayEventsByEventType = new HashMap<>();

    private void addCalendarDayEventForGroup(CalendarDayEvent calendarDayEvent,
                                             String group,
                                             Map<String, List<CalendarDayEvent>> calendarDayEventByGroup) {
        if (group != null) {
            List<CalendarDayEvent> calendarDayEventsForType = calendarDayEventByGroup.get(group);
            if (calendarDayEventsForType == null) {
                calendarDayEventsForType = new ArrayList<>();
                calendarDayEventByGroup.put(group, calendarDayEventsForType);
            }
            calendarDayEventsForType.add(calendarDayEvent);
        }

    }

    /**
     * Add a one shot event from the server
     *
     * @param event
     * @param extension
     * @return
     */
    private int addOneShotEvent(Event event,
                                DateRange extension,
                                Map<String, List<CalendarDayEvent>> calendarDayEventByGroup) {

        if (extension.isInRange(event.getStartDate())) {
            CalendarDayEvent calendarDayEvent = new CalendarDayEvent(event, CalendarDay.from(event.getStartDate()));

            if (event.type != null) {


                if (!shouldGroupAllEvents()) {
                    // Group with event of a tyoe
                    this.addCalendarDayEventForGroup(calendarDayEvent,
                            getEventTypeToEventGroup().get(event.type.id),
                            calendarDayEventByGroup);
                }


                // Group with all the event
                CalendarGroup allCalendarGroup = null; //PlayRetailApp.get(this.context).getConfigHelper().getAllCalendarGroup();
                if (allCalendarGroup != null) {
                    this.addCalendarDayEventForGroup(calendarDayEvent,
                            allCalendarGroup.getCode(),
                            calendarDayEventByGroup);
                }
            }
            if (event.eventSource == EventSource.META.ordinal()) {
                for (CalendarGroup calendarGroup : new ArrayList<CalendarGroup>() /*PlayRetailApp.get(this.context).getConfigHelper().getCalendarGroups()*/) {
                    CalendarDayEvent structEvent = new CalendarDayEvent(event, CalendarDay.from(event.getStartDate()));
                    this.addCalendarDayEventForGroup(structEvent,
                            calendarGroup.getCode(),
                            calendarDayEventByGroup);
                }
            }
            return 1;
        } else {
            return 0;
        }

    }

    /**
     * Add a recurent event from the server
     * It will create a CalendarDayEvent for each period
     *
     * @param event
     * @param extension
     * @return
     */
    private int addRecurentEvent(Event event,
                                 DateRange extension,
                                 Map<String, List<CalendarDayEvent>> calendarDayEventByGroup) {
        int eventAddedCount = 0;
        EventRRuleFreq freq = event.rrule.getEventRRuleFreq();
        if (freq != null) {
            switch (freq) {
                // Yearly event
                case YEARLY:
                    // We id all the events in the calendar loaded range
                    List<Date> recurentDates = extension.getYearlyDateInRange(event.getStartDate());
                    for (Date recurentDate : recurentDates) {
                        CalendarDayEvent calendarDayEvent = new CalendarDayEvent(event, CalendarDay.from(recurentDate));

                        eventAddedCount++;

                        if (!shouldGroupAllEvents()) {
                            // Group with event of a tyoe
                            if (event.type != null) {
                                this.addCalendarDayEventForGroup(calendarDayEvent,
                                        getEventTypeToEventGroup().get(event.type.id),
                                        calendarDayEventByGroup);
                            }
                        }


                        // Group with all the event
                        CalendarGroup allCalendarGroup = null;//PlayRetailApp.get(this.context).getConfigHelper().getAllCalendarGroup();
                        if (allCalendarGroup != null) {
                            this.addCalendarDayEventForGroup(calendarDayEvent,
                                    allCalendarGroup.getCode(),
                                    calendarDayEventByGroup);
                        }

                    }
                    break;
                default:
                    Log.e(EventList.class.getName(), "Only yearly frequence is managed ");
            }
        }
        return eventAddedCount;
    }

    /***
     * AsynTask to build CalendarDayEvent for a period of time
     */
    protected class EventLoaderAsyncTask extends AsyncTask<TimeDirection, Void, EventsListRefresh> {

        private final TimeDirection timeDirection;

        public EventLoaderAsyncTask(TimeDirection timeDirection) {
            this.timeDirection = timeDirection;
        }

        @Override
        protected EventsListRefresh doInBackground(TimeDirection... params) {
            try {
                TimeDirection timeDirection = params[0];

                int moreEventCount = 0;
                Map<String, List<CalendarDayEvent>> calendarDayEventByGroup = new HashMap<>();
                DateRange extension = getDateRange(timeDirection, CalendarDayEventList.this.loadedRange);

                List<CalendarDayEvent> calendarDayEventsForGroup = dayEventsByEventType.get(CalendarDayEventList.this.getCurrentEventGroup());
                int allDayEventsSize = calendarDayEventsForGroup == null ? 0 : calendarDayEventsForGroup.size();


                // We id all the fake month events if needed
                if (CalendarDayEventList.this.shouldAddMetaEvents()) {
                    CalendarDayEventList.this.rawEvents.addAll(getMetaEvents(extension));
                }

                // We load all the events from the server
                for (Event event : CalendarDayEventList.this.rawEvents) {
                    // One shot event
                    if (event.rrule == null) {
                        moreEventCount += addOneShotEvent(event,
                                extension,
                                calendarDayEventByGroup);
                        // Recurent event
                    } else {
                        moreEventCount += addRecurentEvent(event,
                                extension,
                                calendarDayEventByGroup);
                    }
                }

                return new EventsListRefresh(timeDirection,
                        timeDirection.equals(TimeDirection.PRESENT) ? 0 : allDayEventsSize,
                        moreEventCount,
                        calendarDayEventByGroup);

            } catch (Exception e) {
                return new EventsListRefresh(ExceptionType.TECHNICAL, "", e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(EventsListRefresh s) {
            super.onPostExecute(s);

            // Add all the events
            for (Map.Entry<String, List<CalendarDayEvent>> entry : s.getCalendarDayEventByGroup().entrySet()) {
                List<CalendarDayEvent> calendarDayEvents = CalendarDayEventList.this.dayEventsByEventType.get(entry.getKey());
                if (calendarDayEvents == null) {
                    CalendarDayEventList.this.dayEventsByEventType.put(entry.getKey(), entry.getValue());
                } else {
                    calendarDayEvents.addAll(entry.getValue());
                }
            }

            // Sort the events
            for (List<CalendarDayEvent> calendarDayEvents : CalendarDayEventList.this.dayEventsByEventType.values()) {
                Collections.sort(calendarDayEvents, CalendarDayEvent.DEFAULT_CALENDAR_DAY_EVENT_COMPARATOR);
            }


            if (calendarDayEventListLoadedListener != null) {
                calendarDayEventListLoadedListener.onCalendarDayEventListLoaded(s);
            }


        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    public Map<String, String> getEventTypeToEventGroup() {
        if (eventTypeToEventGroup.isEmpty()) {
            Config config = null ;// PlayRetailApp.get(this.context).getConfigHelper().getConfig();
            if (config != null) {
                ConfigFeature feature = config.getFeature();
                if (feature != null) {
                    Calendar calendar = feature.getCalendar();
                    if (calendar != null) {
                        List<CalendarGroup> calendarGroups = calendar.getGroups();
                        for (CalendarGroup calendarGroup : calendarGroups) {
                            List<String> eventTypes = calendarGroup.getEvent_types();
                            if (eventTypes != null) {
                                for (String eventType : calendarGroup.getEvent_types()) {
                                    eventTypeToEventGroup.put(eventType, calendarGroup.getCode());
                                }
                            }
                        }
                    }
                }
            }
        }
        return eventTypeToEventGroup;
    }

    private CalendarDayEventListLoadedListener calendarDayEventListLoadedListener;

    public void setCalendarDayEventListLoadedListener(CalendarDayEventListLoadedListener calendarDayEventListLoadedListener) {
        this.calendarDayEventListLoadedListener = calendarDayEventListLoadedListener;
    }

    public interface CalendarDayEventListLoadedListener {
        void onCalendarDayEventListLoaded(EventsListRefresh eventsListRefresh);
    }


}
