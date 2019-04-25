package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sku;
import moshimoshi.cyplay.com.doublenavigation.model.business.sections.SkuSpecificationSection;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.SkuSpecificationSectionViewHolder;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;

/**
 * Created by romainlebouc on 09/12/2016.
 */

public class SkuSpecificationsAdapter extends SectionedRecyclerViewAdapter<SkuSpecificationSectionViewHolder> {

    private Sku sku;
    private List<SkuSpecificationSection> skuSpecificationSections;
    private final ConfigHelper configHelper;

    public SkuSpecificationsAdapter(ConfigHelper configHelper) {
        this.configHelper = configHelper;
    }

    public void setProductSku(Product product, Sku sku) {
        this.sku = sku;
        this.skuSpecificationSections = SkuSpecificationSection.getProductSpecificationSections(product,sku);

        if ( configHelper.getProductConfig() !=null
                && configHelper.getProductConfig().getSpecifications() !=null
                &&  configHelper.getProductConfig().getSpecifications().getDisplay() !=null){
            configHelper.getProductConfig().getSpecifications().getDisplay().sortSpecificationSections(skuSpecificationSections);
        }

        this.notifyDataSetChanged();
    }

    @Override
    public int getSectionCount() {
        return skuSpecificationSections != null ? skuSpecificationSections.size() : 0;
    }

    @Override
    public int getItemCount(int section) {
        if (skuSpecificationSections != null) {
            SkuSpecificationSection skuSpecificationSection = skuSpecificationSections.get(section);
            return skuSpecificationSection.getProductSpecifications() == null ? 0 : skuSpecificationSection.getProductSpecifications().size();
        } else {
            return 0;
        }

    }

    @Override
    public void onBindHeaderViewHolder(SkuSpecificationSectionViewHolder holder, int section) {
        SkuSpecificationSection skuSpecificationSection = skuSpecificationSections.get(section);
        holder.setItem(skuSpecificationSection, -1);
    }

    @Override
    public void onBindViewHolder(SkuSpecificationSectionViewHolder holder, int section, int relativePosition, int absolutePosition) {
        SkuSpecificationSection skuSpecificationSection = skuSpecificationSections.get(section);
        holder.setItem(skuSpecificationSection, relativePosition);

    }

    @Override
    public SkuSpecificationSectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout;
        SkuSpecificationSectionViewHolder holder;

        View v;
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                layout = R.layout.cell_sku_specification_header;
                v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
                holder = new SkuSpecificationSectionViewHolder(v);
                break;
            case VIEW_TYPE_ITEM:
                layout = R.layout.cell_sku_specification;
                v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
                holder = new SkuSpecificationSectionViewHolder(v);
                break;
            default:
                layout = R.layout.cell_empty;
                v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
                holder = new SkuSpecificationSectionViewHolder(v);
                break;
        }

        return holder;
    }

}


