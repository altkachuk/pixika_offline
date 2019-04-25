package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourcesFilteringComponent;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.ResourceFilterValueViewHolder;
import ninja.cyplay.com.apilibrary.models.filters.ResourceFilter;
import ninja.cyplay.com.apilibrary.models.filters.ResourceFilterValue;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.ProductFilterViewHolder;

/**
 * Created by romainlebouc on 05/09/16.
**/
public class ResourceFilterExpandableItemAdapter<TResourceFilterValue extends ResourceFilterValue> extends AbstractExpandableItemAdapter<ProductFilterViewHolder, ResourceFilterValueViewHolder> {
    List<ResourceFilter<ResourceFilter,TResourceFilterValue>> mItems;

    private ResourcesFilteringComponent resourcesFilteringComponent;
    List<ResourceFilter<ResourceFilter,TResourceFilterValue>> selectedFilters;

    public void setItems(List<ResourceFilter<ResourceFilter, TResourceFilterValue>> filters,
                         List<ResourceFilter<ResourceFilter,TResourceFilterValue>> selectedFilters ) {
        this.mItems = filters;
        this.selectedFilters = selectedFilters;
        this.notifyDataSetChanged();
    }

    public ResourceFilterExpandableItemAdapter(ResourcesFilteringComponent resourcesFilteringComponent,
                                               List<ResourceFilter<ResourceFilter,TResourceFilterValue>> filters,
                                               List<ResourceFilter<ResourceFilter,TResourceFilterValue>> selectedFilters ) {
        setHasStableIds(true); // this is required for expandable feature.
        mItems = filters;
        this.resourcesFilteringComponent = resourcesFilteringComponent;
        this.selectedFilters = selectedFilters;
    }

    @Override
    public int getGroupCount() {
        return mItems !=null ? mItems.size():0;
    }

    @Override
    public int getChildCount(int groupPosition) {
        return mItems !=null ? mItems.get(groupPosition).getValues().size() : 0;
    }

    @Override
    public long getGroupId(int groupPosition) {
        // This method need to return unique value within all group items.
        return mItems.get(groupPosition).hashCode();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // This method need to return unique value within the group.
        return mItems.get(groupPosition).getValues().get(childPosition).hashCode();
    }

    @Override
    public ProductFilterViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_filter, parent, false);
        return new ProductFilterViewHolder(v);
    }

    @Override
    public ResourceFilterValueViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_filter_value, parent, false);
        return new ResourceFilterValueViewHolder(v, resourcesFilteringComponent);
    }

    @Override
    public void onBindGroupViewHolder(ProductFilterViewHolder holder, int groupPosition, int viewType) {
        ResourceFilter group = mItems.get(groupPosition);
        holder.setSelectedFilters(selectedFilters);
        holder.setItem(group);
    }

    @Override
    public void onBindChildViewHolder(ResourceFilterValueViewHolder holder, int groupPosition, int childPosition, int viewType) {
        ResourceFilterValue child = mItems.get(groupPosition).getValues().get(childPosition);
        ResourceFilter productFilter = mItems.get(groupPosition);
        int selectedFilterPosition = selectedFilters.indexOf(productFilter);
        if (  selectedFilterPosition >= 0){
            holder.setProductFilters(productFilter, selectedFilters.get(selectedFilterPosition));
        }else{
            holder.setProductFilters(productFilter, null);
        }
        holder.setItem(child);
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(ProductFilterViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        return true;
    }
}