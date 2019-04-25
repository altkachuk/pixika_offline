package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductSpecification;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.ProductSpecificationsConfig;
import moshimoshi.cyplay.com.doublenavigation.model.business.sections.SkuSpecificationSection;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;

/**
 * Created by romainlebouc on 09/12/2016.
 */

public class SkuSpecificationSectionViewHolder extends SectionedItemViewHolder<SkuSpecificationSection> {

    @Inject
    ConfigHelper configHelper;

    @Nullable
    @BindView(R.id.specification_section_name)
    TextView specificationSectionName;

    @Nullable
    @BindView(R.id.sku_specification_icon)
    ImageView specificationIcon;

    @Nullable
    @BindView(R.id.sku_specification)
    TextView specificationName;

    @Nullable
    @BindView(R.id.sku_specification_value)
    TextView specificationValue;

    private Context context;

    public SkuSpecificationSectionViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = itemView.getContext();
    }

    @Override
    public void setItem(SkuSpecificationSection skuSpecificationSection, int relativePosition) {

        ProductSpecificationsConfig productSpecificationsConfig = configHelper.getProductConfig().getSpecifications();
        if (specificationSectionName != null) {
            String sectionTitle = skuSpecificationSection.getId();

            if (productSpecificationsConfig != null) {
                sectionTitle = productSpecificationsConfig.getSectionTitle(skuSpecificationSection.getId());
            }
            specificationSectionName.setText(sectionTitle);
        }
        if (relativePosition >= 0 && relativePosition < skuSpecificationSection.getProductSpecifications().size()) {
            ProductSpecification skuSpecification = skuSpecificationSection.getProductSpecifications().get(relativePosition);
            if (skuSpecification != null) {
                // TODO : manage icon
                String iconURL = null;
                if (productSpecificationsConfig != null) {
                    iconURL = productSpecificationsConfig.getSpecIconURL(skuSpecification.getId());
                }

                if (specificationIcon != null && iconURL != null) {

                    Picasso.get().load(iconURL).into(specificationIcon, new Callback() {
                        @Override
                        public void onSuccess() {
                            specificationIcon.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {
                            specificationIcon.setVisibility(View.GONE);
                        }
                    });
                } else if (specificationIcon != null) {
                    specificationIcon.setVisibility(View.GONE);
                }
                if (specificationName != null) {
                    specificationName.setText(skuSpecification.getSpec());
                }
                if (specificationValue != null) {
                    specificationValue.setText(skuSpecification.getValueUnit());
                }
            }
        }


    }
}
