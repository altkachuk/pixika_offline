package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;
import moshimoshi.cyplay.com.doublenavigation.model.business.Media;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.Ska;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sku;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EMediaSize;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EMediaType;
import moshimoshi.cyplay.com.doublenavigation.utils.CompatUtils;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.ImageUtils;

/**
 * Created by romainlebouc on 08/08/16.
 */
public class ProductSkuSelectorAdapter extends ArrayAdapter<Sku> {

    @Inject
    ConfigHelper configHelper;

    LayoutInflater mInflater;
    private Product product;

    public ProductSkuSelectorAdapter(Context context, Product product) {
        super(context, 0, product.getSkus());
        PlayRetailApp.get(context).inject(this);
        mInflater = LayoutInflater.from(context);
        this.product = product;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Sku sku = this.getItem(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.spinner_dropdown_product_sku_selector,
                    null);
        }
        this.setSku(sku, convertView, !(product != null && product.getSkus() != null && product.getSkus().size() <= 1));
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Sku sku = this.getItem(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.spinner_dropdown_product_sku_selector,
                    null);
        }
        this.setSku(sku, convertView, false);
        return convertView;
    }

    private void setSku(Sku sku, View convertView, boolean selectorEnabled) {
        if (sku != null) {

            // Sku selector picture
            ImageView skuSelector = (ImageView) convertView.findViewById(R.id.sku_selector_imageview);
            if (skuSelector !=null){
                Media pictureSelector = Media.getFirstMediaOfType(sku.getMedias(), EMediaType.PICTURE_SELECTOR);
                if (pictureSelector != null) {
                    skuSelector.setVisibility(View.VISIBLE);
                    ImageUtils.loadProductMediaPicture(this.getContext(), pictureSelector, skuSelector, EMediaSize.SELECTOR, configHelper);
                } else {
                    skuSelector.setVisibility(View.GONE);
                }
            }

            // Sku name
            TextView skuName = (TextView) convertView.findViewById(R.id.sku_name_selector);
            skuName.setText(sku.getName());

            //Sku price
            TextView skuPrice = (TextView) convertView.findViewById(R.id.sku_price_selector);
            if (skuPrice != null) {
                skuPrice.setTextColor(configHelper.getBrandColor(this.getContext(), product));
                Ska shopSka = sku.getCurrent_shop_availability();
                if (shopSka != null) {
                    skuPrice.setText(configHelper.formatPrice(shopSka.getPrice()));
                }
            }

            ImageView skuSelectorArrow = (ImageView) convertView.findViewById(R.id.sku_selector_arrow);
            if (skuSelectorArrow != null) {
                skuSelectorArrow.setVisibility(selectorEnabled ? View.VISIBLE : View.GONE);
            }

            ImageView skuSelectorChoice = (ImageView) convertView.findViewById(R.id.sku_selector_choice);
            if (skuSelectorChoice != null) {
                int colorId = selectorEnabled ? R.color.colorAccent : R.color.LightGrey;
                CompatUtils.setDrawableTint(skuSelectorChoice.getDrawable(), ContextCompat.getColor(getContext(), colorId));
            }

        }
    }


}
