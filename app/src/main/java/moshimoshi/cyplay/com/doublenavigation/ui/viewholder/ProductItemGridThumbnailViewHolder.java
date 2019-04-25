package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.parceler.Parcels;

import java.text.DateFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductOffer;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EMediaSize;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EOfferDiscountType;
import moshimoshi.cyplay.com.doublenavigation.model.business.filters.ProductFilter;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.ProductWithSelectorsActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.SelectableItemAdapter;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.ImageUtils;
import moshimoshi.cyplay.com.doublenavigation.utils.StringUtils;

/**
 * Created by damien on 10/06/16.
 */
public class ProductItemGridThumbnailViewHolder extends IEmptyOrItemViewHolder<ProductItem> {

    public static int LAYOUT = R.layout.cell_product_grid_thumbnail;

    @Inject
    CustomerContext customerContext;

    @Inject
    ConfigHelper configHelper;

    @Nullable
    @BindView(R.id.dimable_cell_content)
    FrameLayout dimableCellContent;

    @Nullable
    @BindView(R.id.product_item_cell_content)
    View cellContent;

    @Nullable
    @BindView(R.id.product_item_picture)
    public ImageView imageView;

    @Nullable
    @BindView(R.id.product_item_picture_loading)
    public View loadingView;

    @Nullable
    @BindView(R.id.product_item_name)
    public TextView title;

    @Nullable
    @BindView(R.id.product_item_price_before_discount)
    public TextView crossedPrice;

    @Nullable
    @BindView(R.id.product_item_price_after_discount)
    public TextView price;

    @Nullable
    @BindView(R.id.product_item_price_container)
    public View priceContainer;

    @Nullable
    @BindView(R.id.product_item_date)
    public TextView itemDate;

    @Nullable
    @BindView(R.id.product_item_brand)
    public TextView brand;

    @Nullable
    @BindView(R.id.product_item_assortment)
    public TextView assortment;

    @Nullable
    @BindView(R.id.product_favorite_icon)
    ImageView favoriteIcon;

    @Nullable
    @BindView(R.id.product_item_checked)
    View productItemChecked;

    @BindView(R.id.discount_indicator)
    TextView discountIndicator;

    @BindView(R.id.new_product_indicator)
    TextView newProductIndicator;

    private Context context;
    private boolean checked = false;

    private ProductItem productItem;
    private SelectableItemAdapter selectableItemAdapter;
    private List<ProductFilter> activeFilters;
    public DateFormat dateFormat;

    public ProductItemGridThumbnailViewHolder(View view,
                                              final Context context,
                                              SelectableItemAdapter selectableItemAdapter,
                                              List<ProductFilter> activeFilters) {
        super(view);
        this.context = context;
        this.selectableItemAdapter = selectableItemAdapter;
        ButterKnife.bind(this, view);
        PlayRetailApp.get(this.context).inject(this);
        this.activeFilters = activeFilters;
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, view.getContext().getResources().getConfiguration().locale);

    }

    @Override
    public void setItem(final ProductItem productItem) {
        this.productItem = productItem;
        checked = selectableItemAdapter.getSelectedItems().contains(productItem);
        if (productItem != null && productItem.getProduct() != null) {
            title.setText(productItem.getProduct().getName());
            if (brand != null) {
                brand.setText(productItem.getProduct().getBrand());
            }
            if (assortment != null) {
                assortment.setText(productItem.getProduct().getAssortment());
            }

            if (configHelper.getConfig().getFeature().getProductConfig().getThumbnail().getDisplay_price()) {
                if (priceContainer != null)
                    priceContainer.setVisibility(View.VISIBLE);

                if (productItem.getProduct().getCrossedPrice() != null) {
                    if (crossedPrice != null) {
                        crossedPrice.setVisibility(View.VISIBLE);
                        crossedPrice.setText(productItem.getProduct().getCrossedPrice() != null ?
                                configHelper.formatPrice(productItem.getProduct().getCrossedPrice()) : "");
                        crossedPrice.setPaintFlags(crossedPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }

                    if (price != null) {
                        price.setText(configHelper.formatPrice(productItem.getProduct().getPrice()));
                    }

                } else {
                    if (crossedPrice != null) {
                        crossedPrice.setVisibility(View.GONE);
                    }

                    if (price != null) {
                        price.setText(productItem.getProduct().getPrice() != null ? configHelper.formatPrice(productItem.getProduct().getPrice()) : "");
                    }
                }

            } else {
                if (priceContainer != null)
                    priceContainer.setVisibility(View.GONE);
            }

            //show discount indicator
            displayDiscount(productItem.getProduct());

            ImageUtils.loadProductPicture(context, productItem.getProduct(), imageView, loadingView, EMediaSize.PREVIEW, configHelper);
            favoriteIcon.setVisibility(customerContext != null
                    && customerContext.getWishlistItem(productItem.getProduct()) != null ? View.VISIBLE : View.GONE);
            setSelectable(selectableItemAdapter.isSelectionMode());
            String itemDateString = productItem.getDate() != null ? dateFormat.format(productItem.getDate()) : null;
            this.itemDate.setText(itemDateString);
            this.itemDate.setVisibility(itemDateString != null ? View.VISIBLE : View.GONE);
        }
        if (!selectableItemAdapter.isSelectionMode()) {
            this.cellContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (productItem != null && productItem.getProduct() != null) {
                        Intent intent = new Intent(ProductItemGridThumbnailViewHolder.this.context, ProductWithSelectorsActivity.class);
                        if (activeFilters != null) {
                            intent.putExtra(IntentConstants.EXTRA_RESOURCES_ACTIVE_FILTERS, Parcels.wrap(activeFilters));
                        }
                        intent.putExtra(IntentConstants.EXTRA_PRODUCT_NAME, productItem.getProduct().getName());
                        intent.putExtra(IntentConstants.EXTRA_PRODUCT_ID, productItem.getProduct().getId());
                        //intent.putExtra(IntentConstants.EXTRA_SKU_ID, productItem.getSkuId());
                        ProductItemGridThumbnailViewHolder.this.context.startActivity(intent);
                    }
                }
            });
        } else {
            this.cellContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!checked) {
                        dimableCellContent.setForeground(new ColorDrawable(ContextCompat.getColor(ProductItemGridThumbnailViewHolder.this.context,
                                R.color.product_item_light_dim)));
                        productItemChecked.setVisibility(View.VISIBLE);
                        selectableItemAdapter.getSelectedItems().add(productItem);
                        selectableItemAdapter.notifySelectedItemsChange();

                    } else {
                        dimableCellContent.setForeground(new ColorDrawable(ContextCompat.getColor(ProductItemGridThumbnailViewHolder.this.context,
                                R.color.product_item_dim)));
                        productItemChecked.setVisibility(View.GONE);
                        selectableItemAdapter.getSelectedItems().remove(productItem);
                        selectableItemAdapter.notifySelectedItemsChange();
                    }
                    checked = !checked;
                }
            });
        }
    }

    private void setSelectable(boolean selectable) {
        if (selectable) {
            dimableCellContent.setForeground(new ColorDrawable(ContextCompat.getColor(ProductItemGridThumbnailViewHolder.this.context,
                    android.R.color.transparent)));
        } else {
            dimableCellContent.setForeground(new ColorDrawable(ContextCompat.getColor(ProductItemGridThumbnailViewHolder.this.context,
                    android.R.color.transparent)));
        }
        productItemChecked.setVisibility(View.GONE);

    }


    private void displayDiscount(Product product) {
        if (product == null) {
            discountIndicator.setVisibility(View.GONE);
            newProductIndicator.setVisibility(View.GONE);
            return;
        }
        //show new product indicator
        if (product.isNewProduct())
            newProductIndicator.setVisibility(View.VISIBLE);
        else
            newProductIndicator.setVisibility(View.GONE);

        //show percent offer indicator
        ProductOffer percentOffer = product.getOfferByDiscountType(EOfferDiscountType.PERCENT);
        if (percentOffer != null) {
            discountIndicator.setVisibility(View.VISIBLE);
            discountIndicator.setText(context.getString(R.string.product_discount_percent, StringUtils.getPrecentStr(percentOffer.getDiscount_value())));
        } else {
            //if do not have percent offer, show amount offer
            ProductOffer amountOffer = product.getOfferByDiscountType(EOfferDiscountType.AMOUNT);
            if (amountOffer != null) {
                Double discount = StringUtils.getDoubleFromStr(amountOffer.getDiscount_value());
                discountIndicator.setText(context.getString(R.string.product_discount_percent, configHelper.formatPrice(discount)));
            } else
                discountIndicator.setVisibility(View.GONE);
        }
    }
}
