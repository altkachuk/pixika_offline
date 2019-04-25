package moshimoshi.cyplay.com.doublenavigation.presenter;

import moshimoshi.cyplay.com.doublenavigation.view.GetEventsView;

/**
 * Created by romainlebouc on 16/05/16.
 */
public interface GetEventsPresenter extends Presenter<GetEventsView>{

    void getCustomerEvents(String attendee__id);
}
