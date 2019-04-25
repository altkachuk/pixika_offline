package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductSpecification;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EMediaSize;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.ImageUtils;

/**
 * Created by damien on 15/11/16.
 */
public class ProductSpecificationAdapter extends ArrayAdapter<ProductSpecification> {

    @Inject
    ConfigHelper configHelper;

    LayoutInflater mInflater;

    public ProductSpecificationAdapter(Context context) {
        super(context, 0);
        PlayRetailApp.get(context).inject(this);
        mInflater = LayoutInflater.from(context);
    }

    public void setItems(List<ProductSpecification> specifications) {
        // clear items
        clear();
        addAll(specifications);
        notifyDataSetChanged();
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ProductSpecification specification = this.getItem(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_product_specification, null);
        }
        this.setSpec(specification, convertView);
        return convertView;
    }

    private void setSpec(ProductSpecification specification, View convertView) {
        if (specification != null) {

            // Spec
            ImageView specImage = (ImageView) convertView.findViewById(R.id.spec_imageview);
            TextView specValue = (TextView) convertView.findViewById(R.id.spec_value);
            TextView specUnit = (TextView) convertView.findViewById(R.id.spec_unit);


            // Image
            if (specification.getMedias() != null) {
                specImage.setVisibility(View.VISIBLE);
                ImageUtils.loadProductMediaPicture(this.getContext(), specification.getMedias(), specImage, EMediaSize.SELECTOR, configHelper);
            } else {
                specImage.setVisibility(View.GONE);
            }

            // Spec value
            specValue.setText(specification.getValue());
            specUnit.setText(specification.getUnit());
        }
    }


}
