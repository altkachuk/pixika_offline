package moshimoshi.cyplay.com.doublenavigation.model.events;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import ninja.cyplay.com.apilibrary.models.meta.Pagination;


/**
 * Created by romainlebouc on 12/08/16.
 */
public class FilterResourceResponseEvent<Resource, Filter> extends ResourceResponseEvent<Resource>{

    private final List<Filter> filters;

    public FilterResourceResponseEvent(Resource resource, ResourceException resourceException, moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType EResourceType) {
        super(resource, resourceException, EResourceType);
        this.filters = null;
    }

    public FilterResourceResponseEvent(Resource resource, ResourceException resourceException, moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType EResourceType, String previous, String next, Integer count) {
        super(resource, resourceException, EResourceType, previous, next, count);
        this.filters = null;
    }

    public FilterResourceResponseEvent(Resource resource, ResourceException resourceException, moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType EResourceType, String previous, String next, Integer count, Pagination pagination) {
        super(resource, resourceException, EResourceType, previous, next, count, pagination);
        this.filters = null;
    }

    public FilterResourceResponseEvent(Resource resource, ResourceException resourceException, EResourceType EResourceType, String previous, String next, Integer count, Pagination pagination, List<Filter> filters){
        super(resource, resourceException,EResourceType,previous, next, count, pagination);
        this.filters = filters;
    }

    public List<Filter> getFilters() {
        return filters;
    }
}
