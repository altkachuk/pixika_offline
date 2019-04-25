package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;
import moshimoshi.cyplay.com.doublenavigation.model.business.Media;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductSpecSelector;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductSpecification;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sku;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EMediaSize;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EMediaType;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.ImageUtils;

/**
 * Created by romainlebouc on 02/02/2017.
 */

public class ProductItemCore extends LinearLayout {

    @Inject
    protected ConfigHelper configHelper;

    @BindView(R.id.remove_item_container)
    protected View removeItemContainer;

    @Nullable
    @BindView(R.id.sku_picture)
    protected ImageView skuPicture;

    @Nullable
    @BindView(R.id.sku_item_picture_loading)
    protected ProgressBar skuPictureLoading;

    @Nullable
    @BindView(R.id.item_assortment)
    protected TextView assortment;

    @Nullable
    @BindView(R.id.item_brand)
    protected TextView brand;

    @Nullable
    @BindView(R.id.item_name)
    protected TextView name;

    @Nullable
    @BindView(R.id.item_ref)
    protected TextView ref;

    @Nullable
    @BindView(R.id.item_unit_price)
    protected TextView itemPrice;

    @BindView(R.id.product_info_container)
    LinearLayout productInfoContainer;

    @BindView(R.id.sku_spec_container)
    LinearLayout skuSpecContainer;

    boolean isSelected = false;

    public ProductItemCore(Context context) {
        this(context, null);
    }

    public ProductItemCore(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProductItemCore(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        PlayRetailApp.get(context).inject(this);
        initView(context);
    }

    public interface RemoveProductItemListener {
        void onRemoveProductItem();
    }

    private RemoveProductItemListener removeProductItemListener;

    public void setRemoveProductItemListener(RemoveProductItemListener removeProductItemListener) {
        this.removeProductItemListener = removeProductItemListener;
//        removeItemContainer.setVisibility(removeProductItemListener != null ? VISIBLE : GONE);
    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_product_item_core, this, true);
        ButterKnife.bind(this);
    }

    public void fillProductItem(Product product, String skuId) {
        Sku sku = product != null ? product.getSku(skuId) : null;
        if (name != null) {
            name.setText(product != null ? product.getName() : null);
        }
        if (ref != null) {
            ref.setText(product != null ? Sku.getRef(this.getContext(), product.getId(), skuId) : null);
        }
        if (assortment != null) {
            assortment.setText(product != null ? product.getAssortment() : null);
        }
        if (brand != null) {
            brand.setText(product != null ? product.getBrand() : null);
        }

        skuSpecContainer.removeAllViews();
        if (product != null) {
            List<ProductSpecSelector> productSpecSelectors = product.getSelectors();
            if (productSpecSelectors != null && sku != null) {
                for (ProductSpecSelector productSpecSelector : productSpecSelectors) {
                    ProductSpecification productSpecification = sku.getSpecificationForId(productSpecSelector.getSpec_id());
                    if (productSpecification != null) {
                        BasketSkuSpecification basketSkuSpecification = new BasketSkuSpecification(this.getContext());
                        basketSkuSpecification.setProductSpecification(productSpecification);
                        basketSkuSpecification.setSelected(skuSpecContainer.isSelected());
                        skuSpecContainer.addView(basketSkuSpecification);
                    }
                }
            }
        }

        // Picture
        Media firstMedia = null;
        if (sku != null) {
            firstMedia = Media.getFirstMediaOfType(sku.getMedias(), EMediaType.PICTURE);
            if (firstMedia != null) {
                ImageUtils.loadProductMediaPicture(this.getContext(), firstMedia, skuPicture, skuPictureLoading, EMediaSize.PREVIEW, configHelper);
            }
        }
        if (product != null && firstMedia == null) {
            firstMedia = Media.getFirstMediaOfType(product.getMedias(), EMediaType.PICTURE);
            ImageUtils.loadProductMediaPicture(this.getContext(), firstMedia, skuPicture, skuPictureLoading, EMediaSize.PREVIEW, configHelper);
        } else {
            ImageUtils.loadProductMediaPicture(this.getContext(), firstMedia, skuPicture, skuPictureLoading, EMediaSize.PREVIEW, configHelper);
        }
    }

    public void setItemPrice(String price) {
        itemPrice.setText(price);
    }

    @OnClick(R.id.remove_item_container)
    public void onRemoveItem() {
        if (removeProductItemListener != null) {
            removeProductItemListener.onRemoveProductItem();
        }
    }

    public void onSelectStateChange(boolean select) {
        isSelected = select;
        productInfoContainer.setSelected(select);
    }

}
