package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductShare;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.CustomerWishlistSharingMode;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EChannel;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EResourceAttribute;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.DisplayActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.ProductSharePresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.CustomerActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.WishlistShareActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.DisplayEventFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceBaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.ProductItemThumbnailAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.WishListShareDialog;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.GridSpacingItemDecoration;
import moshimoshi.cyplay.com.doublenavigation.utils.ActionEventHelper;
import moshimoshi.cyplay.com.doublenavigation.view.CreateResourceView;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;
import ninja.cyplay.com.apilibrary.models.business.enums.EActionStatus;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;

/**
 * Created by damien on 13/04/16.
 */
public class CustomerWishlistFragment extends ResourceBaseFragment<Customer> implements DisplayEventFragment {

    @Inject
    ProductSharePresenter productSharePresenter;

    @Inject
    CustomerContext customerContext;

    @Inject
    SellerContext sellerContext;

    @Inject
    ActionEventHelper actionEventHelper;

    @BindView(R.id.customer_wishlist_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.wishlist_product_count)
    TextView wishlistProductCount;

    @BindView(R.id.wishlist_share_icon)
    ImageView wishlistShareIcon;

    @BindView(R.id.wishlist_share_container)
    View wishlistShareContainer;

    @BindView(R.id.wishlist_share_text)
    TextView wishlistShareText;

    @BindView(R.id.noresult_view)
    View noResultView;

    private ProductItemThumbnailAdapter adapter;

    private CustomerWishlistSharingMode wishlistSharingMode;

    private WishListShareDialog wishListShareDialog = null;
    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        wishlistSharingMode = configHelper.getWishListSharingModeSelection(EChannel.EMAIL);
        adapter = new ProductItemThumbnailAdapter(this.getContext(), this.getResources().getInteger(R.integer.product_search_columns_count));
        productSharePresenter.setView(new CreateResourceView<ProductShare>() {
            @Override
            public void onError(ExceptionType exceptionType, String status, String message) {
                if (wishListShareDialog != null) {
                    wishListShareDialog.setFailure();
                }

            }

            @Override
            public void onResourceViewCreateResponse(ResourceResponseEvent<ProductShare> resourceResponseEvent) {
                if (wishListShareDialog != null) {
                    wishListShareDialog.setSuccess();
                }
            }
        });

        wishlistShareContainer.setVisibility(wishlistSharingMode == null ? View.GONE : View.VISIBLE);
        initRecycler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_customer_wishlist, container, false);
    }


    @OnClick({R.id.wishlist_share_icon, R.id.wishlist_share_text})
    public void onShareWishList() {
        if (wishlistSharingMode != null) {
            if (wishlistSharingMode.getSelection()) {
                Intent shareWishListIntent = new Intent(getContext(), WishlistShareActivity.class);
                shareWishListIntent.putExtra(IntentConstants.EXTRA_RESOURCES_WISHLIST_PRODUCTS, Parcels.wrap(adapter.getItems()));
                this.startActivity(shareWishListIntent);
                getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.no_anim);
            } else {
                wishListShareDialog = new WishListShareDialog(getContext(), customerContext, new WishListShareDialog.OnConfirmWishListSharingListener() {

                    @Override
                    public void onConfirmWishListSharing(String email) {
                        WishListShareDialog.shareWishlist(
                                CustomerWishlistFragment.this.getContext(),
                                email,
                                customerContext,
                                sellerContext,
                                adapter.getItems(),
                                productSharePresenter,
                                configHelper
                        );
                    }

                    @Override
                    public void onWishListSharingFinished(boolean success) {

                    }
                });
                wishListShareDialog.show();
            }
        }


    }


    @Override
    protected void setResource(Customer customer) {
        if (customer != null) {
            List<ProductItem> productItems = (List<ProductItem>) (List<?>) customer.getWishlist();

            // We remove wishlist item if product is not filled
            for (Iterator<ProductItem> iterator = productItems.iterator(); iterator.hasNext(); ) {
                ProductItem productItem = iterator.next();
                if (productItem.getProduct() == null) {
                    iterator.remove();
                }
            }

            if (!productItems.isEmpty()) {
                adapter.setItems(productItems);
                wishlistProductCount.setText(this.getContext().getResources().getQuantityString(R.plurals.products_count,
                        productItems.size(),
                        productItems.size()));
                wishlistShareContainer.setVisibility(wishlistSharingMode == null ? View.GONE : View.VISIBLE);
                noResultView.setVisibility(View.GONE);
            } else {
                adapter.setItems(new ArrayList<ProductItem>());
                wishlistProductCount.setText(this.getContext().getResources().getQuantityString(R.plurals.products_count, 0, 0));
                wishlistShareContainer.setVisibility(wishlistSharingMode == null ? View.GONE : View.INVISIBLE);
                noResultView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    // -------------------------------------------------------------------------------------------
    //                                      Event(s)
    // -------------------------------------------------------------------------------------------

    @Override
    @Subscribe
    public void onResourceResponseEvent(ResourceResponseEvent<Customer> resourceResponseEvent) {
        if (EResourceType.CUSTOMER == resourceResponseEvent.getEResourceType()) {
            super.onResourceResponse(resourceResponseEvent);
        }
    }

    @Override
    @Subscribe
    public void onResourceRequestEvent(ResourceRequestEvent<Customer> resourceRequestEvent) {
        if (EResourceType.CUSTOMER == resourceRequestEvent.getEResourceType()) {
            super.onResourceRequest(resourceRequestEvent);
        }
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    @Override
    public Customer getCachedResource() {
        return ((ResourceActivity<Customer>) this.getActivity()).getResource();
    }

    @Override
    public void loadResource() {
        ((CustomerActivity) this.getActivity()).loadResource();
    }

    private void initRecycler() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), getResources().getInteger(R.integer.wishlist_columns_count));
        // optimization
        recyclerView.setHasFixedSize(true);
        // set layout
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        // Add 8dp spacing between items
        int spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(getResources().getInteger(R.integer.wishlist_columns_count), spacing, true));
    }

    @Override
    public void logEvent() {
        actionEventHelper.log(new DisplayActionEventData()
                .setResource(EResource.CUSTOMER)
                .setAttribute(EResourceAttribute.WISHLIST.getCode())
                .setValue(customerContext !=null ? customerContext.getCustomerId() : null)
                .setStatus(EActionStatus.SUCCESS));
    }
}