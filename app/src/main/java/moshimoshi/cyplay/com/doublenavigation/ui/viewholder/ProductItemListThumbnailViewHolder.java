package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EMediaSize;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.ProductWithSelectorsActivity;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.ImageUtils;

/**
 * Created by damien on 10/06/16.
 */
public class ProductItemListThumbnailViewHolder extends IEmptyOrItemViewHolder<ProductItem> {

    @Inject
    ConfigHelper configHelper;

    @Inject
    CustomerContext customerContext;

    @Nullable
    @BindView(R.id.product_item_picture_loading)
    View loadingView;

    @BindView(R.id.product_item_picture)
    public ImageView imageView;

    @Nullable
    @BindView(R.id.product_brand)
    public TextView brand;

    @Nullable
    @BindView(R.id.product_name)
    public TextView productName;

    private Context context;

    private ProductItem productItem;

    public ProductItemListThumbnailViewHolder(View view, Context context) {
        super(view);
        this.context = context;
        ButterKnife.bind(this, view);
        PlayRetailApp.get(this.context).inject(this);
    }

    @Override
    public void setItem(ProductItem productItem) {
        this.productItem = productItem;
        if (productItem != null && productItem.getProduct() != null) {

            ImageUtils.loadProductPicture(context, productItem.getProduct(), imageView, loadingView, EMediaSize.PREVIEW, configHelper);

            if (brand != null) {
                brand.setText(productItem.getProduct().getBrand());
            }

            if (productName != null) {
                productName.setText(productItem.getProduct().getName());
            }

        }
    }

    @Nullable
    @OnClick(R.id.product_item_cell_content)
    public void onProductClick() {
        if (productItem != null && productItem.getProduct() != null) {
            Intent intent = new Intent(context, ProductWithSelectorsActivity.class);
            intent.putExtra(IntentConstants.EXTRA_PRODUCT_NAME, productItem.getProduct().getName());
            intent.putExtra(IntentConstants.EXTRA_PRODUCT_ID, productItem.getProduct().getId());
            intent.putExtra(IntentConstants.EXTRA_SKU_ID, productItem.getSkuId());
            context.startActivity(intent);
        }
    }

}
