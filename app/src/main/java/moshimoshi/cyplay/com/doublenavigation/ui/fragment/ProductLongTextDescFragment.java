package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.WishlistItem;
import moshimoshi.cyplay.com.doublenavigation.model.events.CustomerOpinionFinishedEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ProductLoadedEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ProductShareClickEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.WishlistPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.view.OpinionView;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;
import ninja.cyplay.com.apilibrary.models.business.ECustomerOpinion;

/**
 * Created by damien on 17/04/16.
 */
public class ProductLongTextDescFragment extends BaseFragment  implements OpinionView {

    @BindView(R.id.product_long_description)
    TextView description;


    @BindView(R.id.wishlist_icon)
    ImageView wishlistButton;

    @Inject
    EventBus bus;

    @Inject
    WishlistPresenter wishlistPresenter;

    @Inject
    CustomerContext customerContext;

    private Product product;
    private String productId;

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_long_text_desc, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        product = IntentUtils.getExtraFromIntent(getActivity().getIntent(), IntentConstants.EXTRA_PRODUCT);
//        if (product == null){
//            product = IntentUtils.getExtraFromIntent(getActivity().getIntent(), IntentConstants.EXTRA_PRODUCT_PREVIEW);
//        }
        wishlistPresenter.setView(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fillInfo();
        // Cannot wishlist if a customer isn't identified
        if (!customerContext.isCustomerIdentified())
            wishlistButton.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bus != null)
            bus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (bus != null)
            bus.unregister(this);
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    void fillInfo() {
        if (product != null) {
            description.setText(product.getLong_desc());
        }
    }

    @Subscribe
    public void onProductLoaded(final ProductLoadedEvent productLoadedEvent) {
        // We ic_filter_check that the event correspond to the right product
        this.product = productLoadedEvent.getProduct();
        this.fillInfo();
        // update wishlist status
        if (customerContext.getWishlistItem(new WishlistItem(product, null, null)) !=null)
            wishlistButton.setImageResource(R.drawable.ic_favorite_red_24dp);
    }

    @OnClick(R.id.dislike_icon)
    public void onDislikeClick() {
    }

    @OnClick(R.id.like_icon)
    public void onLikeClick() {
    }

    @OnClick(R.id.share_icon)
    public void onShareClick() {
        bus.post(new ProductShareClickEvent());
    }

    @OnClick(R.id.wishlist_icon)
    public void onWishlistClick() {
        if (product != null)
            wishlistPresenter.toggleWishlistItem(ECustomerOpinion.O_WISHLIST, product, null, configHelper.getCurrentShop());
    }

    // -------------------------------------------------------------------------------------------
    //                                      View Impl
    // -------------------------------------------------------------------------------------------

    @Override
    public void onAddToWishlistSuccess(WishlistItem wishlistItem) {
        //stateLoadingView.setLoadingState(LoadingView.LoadingState.LOADED);
        bus.post(new CustomerOpinionFinishedEvent(ECustomerOpinion.O_WISHLIST, true));
        // updateIcon
        wishlistButton.setImageResource(R.drawable.ic_favorite_red_24dp);
        // Update locally
        customerContext.addProductToWishlist(wishlistItem);
    }

    @Override
    public void onDeleteFromWishlistSuccess() {
        //stateLoadingView.setLoadingState(LoadingView.LoadingState.LOADED);
        bus.post(new CustomerOpinionFinishedEvent(ECustomerOpinion.O_WISHLIST, true));
        // updateIcon
        wishlistButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        // Update locally
        customerContext.deleteProductFromWishlist(product);
    }

    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {
    }

}
