package moshimoshi.cyplay.com.doublenavigation.model.events;

import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.requests.ResourceRequest;
import ninja.cyplay.com.apilibrary.models.meta.Pagination;


/**
 * Created by romainlebouc on 12/08/16.
 */
public class ResourceResponseEvent<Resource>{

    private final Resource resource;
    private final ResourceException resourceException;
    private final moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType EResourceType;
    private final Integer count;
    private final String previous;
    private final String next;
    private final Pagination pagination;
    private ResourceRequest resourceRequest;


    public ResourceResponseEvent(Resource resource, ResourceException resourceException, EResourceType EResourceType) {
        this(resource, resourceException, EResourceType, null, null, 0);
    }

    public ResourceResponseEvent(Resource resource, ResourceException resourceException, EResourceType EResourceType,ResourceRequest resourceRequest) {
        this(resource, resourceException, EResourceType, null, null, 0);
        this.resourceRequest = resourceRequest;
    }

    public ResourceResponseEvent(Resource resource, ResourceException resourceException, EResourceType EResourceType, String previous, String next, Integer count){
        this(resource,resourceException,EResourceType, previous,next,count, null);
    }

    public ResourceResponseEvent(Resource resource, ResourceException resourceException, EResourceType EResourceType, String previous, String next, Integer count, Pagination pagination) {
        this.resource = resource;
        this.resourceException = resourceException;
        this.EResourceType = EResourceType;
        this.previous = previous;
        this.next = next;
        this.pagination = pagination;
        this.count = count;
    }

    public Resource getResource() {
        return resource;
    }

    public ResourceException getResourceException() {
        return resourceException;
    }

    public EResourceType getEResourceType() {
        return EResourceType;
    }

    public String getPrevious() {
        return previous;
    }

    public String getNext() {
        return next;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public Integer getCount() {
        return count;
    }

    public int getResourceRequestId(){
        return resourceRequest!=null ? resourceRequest.getRequestId() : -1;
    }

}
