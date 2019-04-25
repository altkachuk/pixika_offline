package moshimoshi.cyplay.com.doublenavigation.presenter;

import java.util.Date;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.Event;
import moshimoshi.cyplay.com.doublenavigation.view.SellerEventsView;

/**
 * Created by romainlebouc on 03/05/16.
 */
public interface SellerEventsPresenter extends Presenter<SellerEventsView> {

    void getEvents();

    void getEvents(Date before, Date after);

    void onEvents(List<Event> events);
}
