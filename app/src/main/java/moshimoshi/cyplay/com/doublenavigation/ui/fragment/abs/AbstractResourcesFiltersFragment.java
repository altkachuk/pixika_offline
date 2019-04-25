package moshimoshi.cyplay.com.doublenavigation.ui.fragment.abs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.flexbox.FlexboxLayout;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourcesFilteringComponent;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.ResourceFilterExpandableItemAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.ActiveFilterValue;
import ninja.cyplay.com.apilibrary.models.filters.ResourceFilter;
import ninja.cyplay.com.apilibrary.models.filters.ResourceFilterValue;

/**
 * Created by romainlebouc on 28/12/2016.
 */

public abstract class AbstractResourcesFiltersFragment extends BaseFragment implements ResourcesFilteringComponent {

    protected List<ResourceFilter<ResourceFilter, ResourceFilterValue>> activeFilters = new ArrayList<>();
    protected List<ResourceFilter> filters = new ArrayList<>();
    protected ResourceFilterExpandableItemAdapter resourceFilterExpandableItemAdapter;

    @BindView(R.id.product_filters_recycler_view)
    RecyclerView availableFiltersRecyclerView;

    @BindView(R.id.selected_filter_values)
    protected FlexboxLayout selectedFilterValues;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_resources_filters, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectedFilterValues.setVisibility(View.GONE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initFilters();
    }

    protected void initFilters() {

        activeFilters = getInitialActiveFilters();
        if (activeFilters != null) {
            fillActiveFilters();
        }

        filters = getFilters();
        initFiltersRecyclerView();

    }
    protected List<ResourceFilter> getFilters() {
        List<ResourceFilter> result = null;
        Intent intent = this.getActivity().getIntent();
        if (intent != null && intent.hasExtra(IntentConstants.EXTRA_RESOURCES_FILTERS)) {
            result = Parcels.unwrap(intent.getParcelableExtra(IntentConstants.EXTRA_RESOURCES_FILTERS));
        }
        return result;
    }


    public List<ResourceFilter<ResourceFilter, ResourceFilterValue>> getInitialActiveFilters() {
        List<ResourceFilter<ResourceFilter, ResourceFilterValue>> result = null;
        Intent intent = this.getActivity().getIntent();
        if (intent != null && intent.hasExtra(IntentConstants.EXTRA_RESOURCES_ACTIVE_FILTERS)) {
            result = Parcels.unwrap(intent.getParcelableExtra(IntentConstants.EXTRA_RESOURCES_ACTIVE_FILTERS));
        }
        return result;
    }

    protected void initFiltersRecyclerView() {

        // Setup expandable feature and RecyclerView
        RecyclerViewExpandableItemManager expMgr = new RecyclerViewExpandableItemManager(null);

        availableFiltersRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        resourceFilterExpandableItemAdapter = new ResourceFilterExpandableItemAdapter(this, filters, activeFilters);
        availableFiltersRecyclerView.setAdapter(expMgr.createWrappedAdapter(resourceFilterExpandableItemAdapter));

        // NOTE: need to disable change animations to ripple effect work properly
        ((SimpleItemAnimator) availableFiltersRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        expMgr.attachRecyclerView(availableFiltersRecyclerView);
    }

    protected void fillActiveFilters() {
        selectedFilterValues.removeAllViews();
        for (final ResourceFilter<ResourceFilter, ResourceFilterValue> selectedFilter : activeFilters) {
            for (final ResourceFilterValue productFilterValue : selectedFilter.getValues()) {
                ActiveFilterValue activeFilterValue = new ActiveFilterValue(this.getContext());
                activeFilterValue.setLabel(productFilterValue.getValue());

                activeFilterValue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggleFilterValue(selectedFilter, productFilterValue);
                    }
                });
                selectedFilterValues.addView(activeFilterValue);
                selectedFilterValues.setVisibility(View.VISIBLE);
            }
        }
        selectedFilterValues.setVisibility(activeFilters == null || activeFilters.isEmpty() ? View.GONE: View.VISIBLE);
    }

    public void toggleFilterValue(ResourceFilter filter, ResourceFilterValue productFilterValue) {
        int filterPosition = activeFilters.indexOf(filter);
        // Filter was not selected => We add it
        if (filterPosition < 0) {
            activeFilters.add(filter.copyWithValue(productFilterValue));
        } else {
            ResourceFilter selectedFilter = activeFilters.get(filterPosition);
            int filterValuePosition = selectedFilter.getValues().indexOf(productFilterValue);
            // Filter value was not selected => We add it
            if (filterValuePosition < 0) {
                selectedFilter.getValues().add(productFilterValue);
            }
            // Filter value was selected => We remove it
            else {
                selectedFilter.getValues().remove(productFilterValue);
                // If there is no more values for the filter, we remove it
                if (selectedFilter.getValues().isEmpty()) {
                    activeFilters.remove(selectedFilter);
                }
            }
        }
        onFilterSelected();
    }

    protected abstract void onFilterSelected();

    public List<ResourceFilter<ResourceFilter, ResourceFilterValue>> getActiveFilters() {
        return activeFilters;
    }
}
