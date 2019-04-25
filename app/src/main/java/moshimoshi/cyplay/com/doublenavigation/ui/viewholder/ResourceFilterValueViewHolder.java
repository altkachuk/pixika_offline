package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import ninja.cyplay.com.apilibrary.models.filters.ResourceFilter;
import ninja.cyplay.com.apilibrary.models.filters.ResourceFilterValue;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourcesFilteringComponent;

/**
 * Created by romainlebouc on 30/08/16.
 */
public class ResourceFilterValueViewHolder extends ItemViewHolder<ResourceFilterValue>{

    @Nullable
    @BindView(R.id.criteria_name)
    TextView criteriaName;

    @Nullable
    @BindView(R.id.critera_selector)
    ImageView criteriaSelector;

    private ResourceFilterValue resourceFilterValue;
    private ResourceFilter productFilter;
    private ResourceFilter selectedProductFiler;

    public ResourceFilterValueViewHolder(View itemView, final ResourcesFilteringComponent resourcesFilteringComponent) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resourcesFilteringComponent.toggleFilterValue(productFilter, resourceFilterValue);
            }
        });

    }

    @Override
    public void setItem(ResourceFilterValue productFilterValue) {
        this.resourceFilterValue = productFilterValue;
        criteriaName.setText(productFilterValue.getValue() + " ("+ String.valueOf(productFilterValue.getCount()) + ")");
        if (selectedProductFiler !=null && selectedProductFiler.getValues() != null && selectedProductFiler.getValues().contains(productFilterValue)){
            criteriaSelector.setImageResource(R.drawable.ic_filter_check);
        }else{
            criteriaSelector.setImageResource(R.drawable.ic_filter_add);
        }
    }

    public void setProductFilters(ResourceFilter productFilter, ResourceFilter selectedProductFilter ){
        this.productFilter = productFilter;
        this.selectedProductFiler = selectedProductFilter;
    }

}
