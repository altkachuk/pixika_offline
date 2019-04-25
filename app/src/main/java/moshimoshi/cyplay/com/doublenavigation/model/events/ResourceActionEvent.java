package moshimoshi.cyplay.com.doublenavigation.model.events;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EResourceAction;

/**
 * Created by romainlebouc on 29/12/2016.
 */

public class ResourceActionEvent {

    private  final EResourceAction resourceAction;

    public ResourceActionEvent(EResourceAction resourceAction) {
        this.resourceAction = resourceAction;
    }

    public EResourceAction getResourceAction() {
        return resourceAction;
    }
}
