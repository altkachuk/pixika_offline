package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sku;
import moshimoshi.cyplay.com.doublenavigation.model.business.WishlistItem;
import moshimoshi.cyplay.com.doublenavigation.model.events.ProductAddedToWishlistEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ProductRemovedFromWishlistEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.WishlistPresenter;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.SnackBarHelper;
import moshimoshi.cyplay.com.doublenavigation.view.OpinionView;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;
import ninja.cyplay.com.apilibrary.models.business.ECustomerOpinion;

/**
 * Created by romainlebouc on 07/09/16.
 */
public class ProductSkuheader extends LinearLayout implements OpinionView {

    @Inject
    ConfigHelper configHelper;

    @BindView(R.id.product_sku_header_container)
    View container;

    @Nullable
    @BindView(R.id.product_info_brand)
    TextView productBrand;

    @Nullable
    @BindView(R.id.product_info_name)
    TextView productName;

    @Nullable
    @BindView(R.id.product_sku_name)
    TextView skuName;

    @Nullable
    @BindView(R.id.product_sku_id)
    TextView skuId;

    @Nullable
    @BindView(R.id.favorite_product_container)
    View favoriteProductContainer;

    @Nullable
    @BindView(R.id.favorite_product_icon)
    ImageView favorite;

    @Nullable
    @BindView(R.id.favorite_product_text)
    TextView favoriteText;

    private Activity activity;
    private CustomerContext customerContext;
    private WishlistPresenter wishlistPresenter;

    private Sku sku;
    private Product product;

    public ProductSkuheader(Context context) {
        this(context, null);
    }

    public ProductSkuheader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProductSkuheader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_product_sku_header, this, true);
        // Bind xml
        ButterKnife.bind(this);
    }

    public void registerToBus(EventBus bus) {
        if (bus != null) {
            bus.register(this);
        }
    }

    public void unregisterFromBus(EventBus bus) {
        if (bus != null) {
            bus.unregister(this);
        }
    }

    public void setContext(Activity activity, CustomerContext customerContext, WishlistPresenter wishlistPresenter, ConfigHelper configHelper) {
        this.activity = activity;
        this.customerContext = customerContext;
        this.wishlistPresenter = wishlistPresenter;
        this.wishlistPresenter.setView(this);
        this.configHelper = configHelper;
    }

    public void setProduct(Product product) {
        this.product = product;
        if (product != null) {
            container.setBackgroundColor(configHelper.getBrandColor(this.getContext(), product));
            container.requestLayout();
            if (productBrand != null) {
                if (product.getBrand() != null) {
                    productBrand.setText(product.getBrand());
                } else if (product.getAssortment() != null) {
                    productBrand.setText(product.getAssortment());
                }

            }

            if (productName != null) {
                String name = product.getName();
                productName.setText(name != null ? name.replaceAll("[\n]", " ") : "");
            }

        }
        updateWishlistButtonDesign();
    }

    public void setSku(Sku sku) {
        this.sku = sku;
        if (sku != null) {
            if (skuName != null) {
                skuName.setText(sku.getName());
            }
            if (skuId != null && product != null) {
                skuId.setText(sku.getRef(this.getContext(), product));
            }

        }
    }

    @Optional
    @OnClick({R.id.favorite_product_icon,
            R.id.favorite_product_text})
    public void onWishListHeartClick() {
        if (product != null) {
            Animation myFadeInAnimation = AnimationUtils.loadAnimation(this.getContext(), R.anim.blinking);
            favorite.startAnimation(myFadeInAnimation);
            wishlistPresenter.toggleWishlistItem(ECustomerOpinion.O_WISHLIST, product, sku, configHelper.getCurrentShop());
        }
    }


    @Override
    public void onAddToWishlistSuccess(WishlistItem wishlistItem) {
        customerContext.addProductToWishlist(wishlistItem);
        if (!activity.isDestroyed()) {
            SnackBarHelper.buildSnackbar(activity.findViewById(android.R.id.content), this.getContext().getString(R.string.product_added_to_wishlist_success),
                    Snackbar.LENGTH_SHORT, this.getContext().getString(android.R.string.ok)).show();
            favorite.clearAnimation();
        }


    }

    @Override
    public void onDeleteFromWishlistSuccess() {
        if (!activity.isDestroyed()) {
            SnackBarHelper.buildSnackbar(activity.findViewById(android.R.id.content), this.getContext().getString(R.string.product_removed_to_wishlist_success),
                    Snackbar.LENGTH_SHORT, this.getContext().getString(android.R.string.ok)).show();
            favorite.clearAnimation();
        }

    }

    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {
        if (!activity.isDestroyed()) {
            SnackBarHelper.buildSnackbar(activity.findViewById(android.R.id.content), this.getContext().getString(R.string.product_wishlist_failure),
                    Snackbar.LENGTH_INDEFINITE, this.getContext().getString(android.R.string.ok)).show();
            favorite.clearAnimation();
        }

    }

    @Subscribe
    public void onProductAddedToWishList(ProductAddedToWishlistEvent productAddedToWishlistEvent) {
        updateWishlistButtonDesign();
    }

    @Subscribe
    public void onProductRemovedFromWishlist(ProductRemovedFromWishlistEvent productRemovedFromWishlistEvent) {
        updateWishlistButtonDesign();
    }

    private void updateWishlistButtonDesign() {
        if (favoriteProductContainer != null) {
            favoriteProductContainer.setVisibility(customerContext != null && customerContext.isCustomerIdentified() ? VISIBLE : GONE);
        }

        if (product != null && customerContext != null && customerContext.getWishlistItem(product) != null) {
            if (favorite != null) {
                favorite.setImageResource(R.drawable.ic_favorite_active);
            }
            if (favoriteText != null) {
                favoriteText.setText(R.string.remove_from_favorite);
            }
        } else {
            if (favorite != null) {
                favorite.setImageResource(R.drawable.ic_favorite_inactive);
            }
            if (favoriteText != null) {
                favoriteText.setText(R.string.add_to_favorite);
            }
        }

    }


}
