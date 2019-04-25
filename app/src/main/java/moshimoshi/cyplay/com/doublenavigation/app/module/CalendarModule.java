package moshimoshi.cyplay.com.doublenavigation.app.module;

import org.greenrobot.eventbus.EventBus;

import dagger.Module;
import dagger.Provides;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CalendarEventList;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.DashboardEventList;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import ninja.cyplay.com.apilibrary.domain.interactor.EventInteractor;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;

/**
 * Created by romainlebouc on 03/05/16.
 */
@Module
public class CalendarModule {

    private final DashboardEventList dashboardEventList;
    private final CalendarEventList calendarEventList;


    public CalendarModule(EventBus bus, EventInteractor eventInteractor, SellerContext sellerContext, APIValue apiValue) {
        this.dashboardEventList = new DashboardEventList(bus, eventInteractor, sellerContext, apiValue);
        this.calendarEventList = new CalendarEventList(bus, eventInteractor, sellerContext, apiValue);
    }

    @Provides
    public CalendarEventList calendarEvents() {
        return this.calendarEventList;
    }

    @Provides
    public DashboardEventList dashboardEvents() {
        return this.dashboardEventList;
    }

}
