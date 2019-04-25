package moshimoshi.cyplay.com.doublenavigation.model.singleton;

import java.util.Date;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.Event;
import moshimoshi.cyplay.com.doublenavigation.model.custom.DateRange;
import moshimoshi.cyplay.com.doublenavigation.model.custom.TimeDirection;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;

/**
 * Created by romainlebouc on 16/05/16.
 */
public class CustomerEventList extends CalendarDayEventList{

    public CustomerEventList(APIValue apiValue) {
        super(apiValue);
        this.loadedRange.setOneDateRange(new Date()).extendsRange(1);
    }

    @Override
    protected List<Event> getMetaEvents(DateRange range) {
        return null;
    }

    @Override
    protected DateRange getDateRange(TimeDirection timeDirection, DateRange currentDateRange) {
        return currentDateRange;
    }

    @Override
    public boolean shouldAddMetaEvents() {
        return false;
    }

    @Override
    protected boolean shouldGroupAllEvents(){
        return true;
    }

    @Override
    public String getCurrentEventGroup() {
        return "ALL";
    }

    public void getCalendarDayEvents(List<Event> rawEvents){
        this.rawEvents.addAll(rawEvents);
        new EventLoaderAsyncTask(TimeDirection.PRESENT).execute(TimeDirection.PRESENT);

    }
}
