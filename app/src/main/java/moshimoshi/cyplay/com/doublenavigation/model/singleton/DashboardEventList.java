package moshimoshi.cyplay.com.doublenavigation.model.singleton;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.Event;
import moshimoshi.cyplay.com.doublenavigation.model.custom.DateRange;
import moshimoshi.cyplay.com.doublenavigation.model.custom.TimeDirection;
import ninja.cyplay.com.apilibrary.domain.interactor.EventInteractor;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;

/**
 * Created by romainlebouc on 03/05/16.
 */
public class DashboardEventList extends EventList {

    public DashboardEventList(EventBus bus, EventInteractor eventInteractor, SellerContext sellerContext, APIValue apiValue) {
        super(bus, eventInteractor, sellerContext, apiValue);
    }


    @Override
    public boolean shouldAddMetaEvents() {
        return false;
    }


    @Override
    protected DateRange getDateRange(TimeDirection timeDirection, DateRange currentDateRange) {
        return getLoadedRange();
    }

    @Override
    public void loadEvents(final TimeDirection timeDirection, Date after, Date before) {

        if (!gettingMoreEvent) {
            this.getLoadedRange().setMinDate(after);
            this.getLoadedRange().setMaxDate(before);
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
        return new ArrayList<>();
    }

}
