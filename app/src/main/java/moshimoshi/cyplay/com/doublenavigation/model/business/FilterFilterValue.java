package moshimoshi.cyplay.com.doublenavigation.model.business;

import moshimoshi.cyplay.com.doublenavigation.model.business.filters.ProductFilter;
import ninja.cyplay.com.apilibrary.models.filters.ResourceFilter;
import ninja.cyplay.com.apilibrary.models.filters.ResourceFilterValue;

/**
 * Created by romainlebouc on 31/08/16.
 */
public class FilterFilterValue<TResourceFilterValue extends ResourceFilterValue> {

    ResourceFilter filter;
    TResourceFilterValue resourceFilterValue;

    public FilterFilterValue(ResourceFilter filter, TResourceFilterValue resourceFilterValue) {
        this.filter = filter;
        this.resourceFilterValue = resourceFilterValue;
    }

    public ResourceFilter getFilter() {
        return filter;
    }

    public void setFilter(ProductFilter filter) {
        this.filter = filter;
    }

    public TResourceFilterValue getResourceFilterValue() {
        return resourceFilterValue;
    }

    public void setResourceFilterValue(TResourceFilterValue productFilterValue) {
        this.resourceFilterValue = productFilterValue;
    }

}
