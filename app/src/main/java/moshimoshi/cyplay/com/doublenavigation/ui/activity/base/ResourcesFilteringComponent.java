package moshimoshi.cyplay.com.doublenavigation.ui.activity.base;

import ninja.cyplay.com.apilibrary.models.filters.ResourceFilter;
import ninja.cyplay.com.apilibrary.models.filters.ResourceFilterValue;

/**
 * Created by romainlebouc on 31/08/16.
 */
public interface ResourcesFilteringComponent {
    void toggleFilterValue(ResourceFilter filter, ResourceFilterValue productFilterValue);
}
