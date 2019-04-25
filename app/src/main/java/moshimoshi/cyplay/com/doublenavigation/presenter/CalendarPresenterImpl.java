package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.model.business.Event;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.CalendarGroup;
import moshimoshi.cyplay.com.doublenavigation.model.events.EventsListRefresh;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CalendarEventList;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;
import moshimoshi.cyplay.com.doublenavigation.view.CalendarView;
import ninja.cyplay.com.apilibrary.domain.interactor.EventInteractor;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;

/**
 * Created by romainlebouc on 26/04/16.
 */
public class CalendarPresenterImpl extends BasePresenter implements CalendarPresenter {

    @Inject
    ConfigHelper configHelper;

    @Inject
    APIValue apiValue;

    @Inject
    SellerContext sellerContext;

    private CalendarView calendarView;
    private EventInteractor eventInteractor;
    private CalendarEventList calendarEventList;
    private EventBus bus;

    public CalendarPresenterImpl(Context context,
                                 EventInteractor eventInteractor,
                                 CalendarEventList calendarEventList,
                                 EventBus bus) {
        super(context);
        this.eventInteractor = eventInteractor;
        this.calendarEventList = calendarEventList;
        this.bus = bus;
        bus.register(this);
    }


    @Override
    public void getEvents() {
        this.getEvents(null, null);
    }

    @Override
    public void getEvents(Date after, Date before) {
        CalendarGroup calendarGroup = configHelper.getAllCalendarGroup();
        if (calendarGroup != null) {
            calendarEventList.setCurrentEventGroup(calendarGroup.getCode());
            calendarEventList.getEvents(before, after);
        }

    }

    @Override
    public void onEvents(List<Event> events) {
        //calendarView.onEventsSuccess(events);
    }

    @Override
    public void setView(CalendarView view) {
        this.calendarView = view;
    }

    @Subscribe
    public void onEventsListRefresh(EventsListRefresh eventsListRefresh) {
        calendarView.onEventsSuccess(calendarEventList.getCalendarDayEvents(calendarEventList.getCurrentEventGroup()));
    }

}
