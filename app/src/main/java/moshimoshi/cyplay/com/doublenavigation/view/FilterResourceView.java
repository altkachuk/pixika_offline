package moshimoshi.cyplay.com.doublenavigation.view;


import moshimoshi.cyplay.com.doublenavigation.model.events.FilterResourceResponseEvent;

/**
 * Created by romainlebouc on 14/08/16.
 */
public interface FilterResourceView<Resource, Filter> extends View {
    void onResourceViewResponse(FilterResourceResponseEvent<Resource, Filter> resourceResponseEvent);
}
