package moshimoshi.cyplay.com.doublenavigation.ui.fragment.abs;

import java.util.List;

import ninja.cyplay.com.apilibrary.models.filters.ResourceFilter;
import ninja.cyplay.com.apilibrary.models.filters.ResourceFilterValue;

/**
 * Created by wentongwang on 14/04/2017.
 */

public interface FilterFragmentHandler {
    void resetActiveFilter();
    void onFilterSelected();
    List<ResourceFilter<ResourceFilter, ResourceFilterValue>> getActiveFilters();
}
