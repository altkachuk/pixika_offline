package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.content.Intent;

import org.parceler.Parcels;

import moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.abs.AbstractResourcesFiltersFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.abs.FilterFragmentHandler;

/**
 * Created by romainlebouc on 27/12/2016.
 */

public class ResourcesFilterSingleChoiceFragment extends AbstractResourcesFiltersFragment implements FilterFragmentHandler{

    @Override
    public void resetActiveFilter() {
        activeFilters.clear();
    }

    @Override
    public void onFilterSelected() {
        Intent intent = new Intent();
        intent.putExtra(IntentConstants.EXTRA_RESOURCES_ACTIVE_FILTERS, Parcels.wrap(activeFilters));
        this.getActivity().setResult(IntentConstants.RESULT_ITEMS_FILTERS, intent);
        this.getActivity().supportFinishAfterTransition();
    }

}