package moshimoshi.cyplay.com.doublenavigation.model.business.filters;

import android.content.Context;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.utils.XmlUtils;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AProductFilter;
import ninja.cyplay.com.apilibrary.models.filters.ResourceFilter;
import ninja.cyplay.com.apilibrary.models.filters.ResourceFilterValue;

/**
 * Created by anishosni on 10/06/2015.
 */
@Parcel
public class ProductFilter extends ResourceFilter<ProductFilter, ProductFilterValue> implements PR_AProductFilter {

    public Map<String, String> getMissingLabels(Context context) {
        return XmlUtils.getHashMapResource(context, R.xml.additional_filters);
    }

    public List<ProductFilterValue> values;

    @Override
    public ResourceFilter copyWithValue(ResourceFilterValue filterValue) {
        ProductFilter filter = new ProductFilter();
        filter.label = this.label;
        filter.key = this.key;
        filter.level = this.level;
        filter.setValues(new ArrayList<ProductFilterValue>());
        filter.getValues().add((ProductFilterValue) filterValue);
        return filter;
    }

    public List<ProductFilterValue> getValues() {
        return values;
    }

    public void setValues(List<ProductFilterValue> values) {
        this.values = values;
    }

    public String getLabel(Context context) {

        if (label == null && context != null && getMissingLabels(context).containsKey(key)) {

            int labelResourceNameId = context.getResources().getIdentifier(getMissingLabels(context).get(key), "string", context.getPackageName());

            if (labelResourceNameId > 0) {
                return context.getString(labelResourceNameId);
            } else {
                return "";
            }

        }

        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductFilter that = (ProductFilter) o;

        if (label != null ? !label.equals(that.label) : that.label != null) return false;
        return key != null ? key.equals(that.key) : that.key == null && (level != null ? level.equals(that.level) : that.level == null);

    }


}

