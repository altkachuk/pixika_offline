package moshimoshi.cyplay.com.doublenavigation.view;


import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;

/**
 * Created by romainlebouc on 14/08/16.
 */
public interface ResourceView<Resource> extends View {
    void onResourceViewResponse(ResourceResponseEvent<Resource> resourceResponseEvent);
}
