package moshimoshi.cyplay.com.doublenavigation.view;

import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;

/**
 * Created by romainlebouc on 24/08/16.
 */
public interface CreateResourceView<Resource> extends BaseView {

    void onResourceViewCreateResponse(ResourceResponseEvent<Resource> resourceResponseEvent);

}
