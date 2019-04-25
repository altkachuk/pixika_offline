package moshimoshi.cyplay.com.doublenavigation.model.events;

import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;

/**
 * Created by romainlebouc on 14/08/16.
 */
public class ResourceRequestEvent<Resource> {


    private static int REQUEST_ID_COUNTER = 0;

    private final moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType EResourceType;
    private final int requestId;
    private final String resourceId;
    private final boolean extra;

    public ResourceRequestEvent( EResourceType EResourceType , String resourceId, boolean extra) {
        this.EResourceType = EResourceType;
        this.requestId = generateRequestId();
        this.resourceId = resourceId;
        this.extra = extra;
    }

    public ResourceRequestEvent( EResourceType EResourceType , String resourceId) {
        this(EResourceType, resourceId, false);
    }

    public ResourceRequestEvent( EResourceType EResourceType ) {
        this(EResourceType, null);
    }


    private static synchronized int generateRequestId()
    {
       return REQUEST_ID_COUNTER++;
    }

    public moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType getEResourceType() {
        return EResourceType;
    }

    public int getRequestId() {
        return requestId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public boolean isExtra() {
        return extra;
    }
}
