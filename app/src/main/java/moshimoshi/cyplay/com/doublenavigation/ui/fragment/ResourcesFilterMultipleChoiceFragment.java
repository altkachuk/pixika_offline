package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import moshimoshi.cyplay.com.doublenavigation.ui.fragment.abs.AbstractResourcesFiltersFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.abs.FilterFragmentHandler;

/**
 * Created by romainlebouc on 28/12/2016.
 */

public class ResourcesFilterMultipleChoiceFragment extends AbstractResourcesFiltersFragment implements FilterFragmentHandler{

    @Override
    public void resetActiveFilter() {
        activeFilters.clear();
    }

    @Override
    public void onFilterSelected(){
        fillActiveFilters();
        resourceFilterExpandableItemAdapter.notifyDataSetChanged();
    }
}
