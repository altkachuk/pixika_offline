package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sku;
import moshimoshi.cyplay.com.doublenavigation.model.business.WishlistItem;
import moshimoshi.cyplay.com.doublenavigation.model.events.ProductAddedToWishlistEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ProductRemovedFromWishlistEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ProductSelectedSkuChangeEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.WishlistPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.abs.AbstractProductActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.utils.SnackBarHelper;
import moshimoshi.cyplay.com.doublenavigation.view.OpinionView;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;
import ninja.cyplay.com.apilibrary.models.business.ECustomerOpinion;

/**
 * Created by romainlebouc on 11/01/2017.
 */

public class ProductSkuHeaderFragment extends BaseFragment implements OpinionView {

    @Inject
    CustomerContext customerContext;

    @Inject
    WishlistPresenter wishlistPresenter;

    @Inject
    protected EventBus bus;

    @BindView(R.id.product_sku_header_container)
    View container;


    @Nullable
    @BindView(R.id.product_assortment)
    TextView productAssortment;

    @Nullable
    @BindView(R.id.product_brand)
    TextView productBrand;

    @Nullable
    @BindView(R.id.product_info_name)
    TextView productName;

    @Nullable
    @BindView(R.id.product_sku_name)
    TextView skuName;

    @Nullable
    @BindView(R.id.product_sku_id)
    TextView referenceId;

    @Nullable
    @BindView(R.id.favorite_product_container)
    View favoriteProductContainer;

    @Nullable
    @BindView(R.id.favorite_product_icon)
    ImageView favorite;

    @Nullable
    @BindView(R.id.favorite_product_text)
    TextView favoriteText;

    private Product product;
    private Sku sku;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.custom_product_sku_header, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        wishlistPresenter.setView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bus != null) {
            bus.register(this);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (bus != null) {
            bus.unregister(this);
        }
    }

    public void setProduct(Product product) {
        this.product = product;
        if (product != null) {
            container.setBackgroundColor(configHelper.getBrandColor(this.getContext(), product));
            container.requestLayout();
            if (productBrand != null) {
                productBrand.setText(product.getBrand());
            }

            if (productAssortment != null) {
                productAssortment.setText(product.getAssortment());
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
            if (referenceId != null && product != null) {
                referenceId.setText(sku.getRef(this.getContext(), product));
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

    @Subscribe
    public void onProductSelectedSkuChangeEvent(ProductSelectedSkuChangeEvent productSelectedSkuChangeEvent) {
        Sku selectedSku = ((AbstractProductActivity) getActivity()).getSelectedSku();
        this.setSku(selectedSku);
    }


    @Subscribe
    public void onResourceResponseEvent(ResourceResponseEvent<Product> resourceResponseEvent) {
        if (EResourceType.PRODUCT == resourceResponseEvent.getEResourceType()) {
            this.setProduct(resourceResponseEvent.getResource());
        }
    }

    @Override
    public void onAddToWishlistSuccess(WishlistItem wishlistItem) {
        customerContext.addProductToWishlist(wishlistItem);
        if (!this.getActivity().isDestroyed()) {
            SnackBarHelper.buildSnackbar(this.getActivity().findViewById(android.R.id.content), this.getContext().getString(R.string.product_added_to_wishlist_success),
                    Snackbar.LENGTH_SHORT, this.getContext().getString(android.R.string.ok)).show();
            favorite.clearAnimation();
        }


    }

    @Override
    public void onDeleteFromWishlistSuccess() {
        if (!this.getActivity().isDestroyed()) {
            SnackBarHelper.buildSnackbar(this.getActivity().findViewById(android.R.id.content), this.getContext().getString(R.string.product_removed_to_wishlist_success),
                    Snackbar.LENGTH_SHORT, this.getContext().getString(android.R.string.ok)).show();
            favorite.clearAnimation();
        }

    }

    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {
        if (!this.getActivity().isDestroyed()) {
            SnackBarHelper.buildSnackbar(this.getActivity().findViewById(android.R.id.content), this.getContext().getString(R.string.product_wishlist_failure),
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
            favoriteProductContainer.setVisibility(customerContext != null && customerContext.isCustomerIdentified() ? View.VISIBLE : View.GONE);
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
