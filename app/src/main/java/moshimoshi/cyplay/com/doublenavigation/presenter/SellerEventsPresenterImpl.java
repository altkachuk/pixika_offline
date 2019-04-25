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
import moshimoshi.cyplay.com.doublenavigation.model.singleton.DashboardEventList;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;
import moshimoshi.cyplay.com.doublenavigation.view.SellerEventsView;
import ninja.cyplay.com.apilibrary.domain.interactor.EventInteractor;

/**
 * Created by romainlebouc on 03/05/16.
 */
public class SellerEventsPresenterImpl extends BasePresenter implements SellerEventsPresenter {

    @Inject
    ConfigHelper configHelper;

    private EventInteractor eventInteractor;
    private SellerEventsView sellerEventsView;
    private DashboardEventList dashboardEventList;
    private EventBus bus;


    public SellerEventsPresenterImpl(Context context,
                                     EventInteractor eventInteractor,
                                     DashboardEventList dashboardEventList,
                                     EventBus bus) {
        super(context);
        this.eventInteractor = eventInteractor;
        this.dashboardEventList = dashboardEventList;
        this.bus = bus;
        bus.register(this);
    }

    @Override
    public void getEvents() {

    }

    @Override
    public void getEvents(Date before, Date after) {
        CalendarGroup calendarGroup = /*PlayRetailApp.get(context).getConfigHelper()*/configHelper.getAllCalendarGroup();
        if (calendarGroup != null) {
            dashboardEventList.setCurrentEventGroup(calendarGroup.getCode());
            dashboardEventList.getEvents(before, after);
        }


    }

    @Override
    public void onEvents(List<Event> events) {

    }

    @Override
    public void setView(SellerEventsView view) {
        this.sellerEventsView = view;
    }

    @Subscribe
    public void onEventsListRefresh(EventsListRefresh eventsListRefresh) {
        if (eventsListRefresh.getExceptionType() == null) {
            sellerEventsView.onEventsSuccess(dashboardEventList.getCalendarDayEvents(dashboardEventList.getCurrentEventGroup()));
        } else {
            sellerEventsView.onError(eventsListRefresh.getExceptionType(), eventsListRefresh.getStatus(), eventsListRefresh.getMessage());
        }

    }
}
