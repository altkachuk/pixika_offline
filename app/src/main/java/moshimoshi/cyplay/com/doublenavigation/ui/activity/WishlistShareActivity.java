package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import org.parceler.Parcels;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductShare;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EShareProductsState;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.ProductSharePresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.ProductItemThumbnailAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.WishListShareDialog;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.GridSpacingItemDecoration;
import moshimoshi.cyplay.com.doublenavigation.view.CreateResourceView;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;

/**
 * Created by romainlebouc on 10/09/16.
 */
public class WishlistShareActivity extends BaseActivity {

    @Inject
    ProductSharePresenter productSharePresenter;

    @Inject
    CustomerContext customerContext;

    @Inject
    SellerContext sellerContext;

    @BindView(R.id.customer_wishlist_share_recycler_view)
    RecyclerView recyclerView;

    private WishListShareDialog wishListShareDialog;
    private EShareProductsState shareWishListState = EShareProductsState.PRODUCT_TO_SELECT;

    private ProductItemThumbnailAdapter adapter;
    private List<ProductItem> productItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_wishlist);

        Intent intent = this.getIntent();
        if (intent != null && intent.hasExtra(IntentConstants.EXTRA_RESOURCES_WISHLIST_PRODUCTS)) {
            productItems = Parcels.unwrap(intent.getParcelableExtra(IntentConstants.EXTRA_RESOURCES_WISHLIST_PRODUCTS));
        }

        productSharePresenter.setView(new CreateResourceView<ProductShare>() {
            @Override
            public void onError(ExceptionType exceptionType, String status, String message) {
                wishListShareDialog.setFailure();
            }

            @Override
            public void onResourceViewCreateResponse(ResourceResponseEvent<ProductShare> resourceResponseEvent) {
                wishListShareDialog.setSuccess();
            }
        });

        adapter = new ProductItemThumbnailAdapter(this, this.getResources().getInteger(R.integer.product_search_columns_count)) {
            @Override
            public void notifySelectedItemsChange() {
                super.notifySelectedItemsChange();
                if (!this.getSelectedItems().isEmpty()) {
                    shareWishListState = EShareProductsState.PRODUCTS_SELECTED;
                } else {
                    shareWishListState = EShareProductsState.PRODUCT_TO_SELECT;
                }
                applySelectedState(this.getSelectedItems());

            }
        };

        if (productItems != null) {
            adapter.setItems(productItems);
        }

        initRecycler();
        applySelectedState(adapter.getSelectedItems());

    }

    private MenuItem sendWishListMenu;


    @Override
    protected boolean isLeftCrossClosable() {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wishlist_share, menu);
        sendWishListMenu = menu.findItem(R.id.action_send_wishlist);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send_wishlist:
                onOptionSendWishListSelected();
                break;
            case android.R.id.home:
                supportFinishAfterTransition();
                overridePendingTransition(R.anim.no_anim, R.anim.slide_out_bottom);
                break;
        }
        return true;
    }

    private void onOptionSendWishListSelected() {
        wishListShareDialog = new WishListShareDialog(WishlistShareActivity.this, customerContext, new WishListShareDialog.OnConfirmWishListSharingListener() {
            @Override
            public void onConfirmWishListSharing(String email) {

                WishListShareDialog.shareWishlist(
                        WishlistShareActivity.this,
                        email,
                        customerContext,
                        sellerContext,
                        adapter.getSelectedItems(),
                        productSharePresenter,
                        configHelper);
            }

            @Override
            public void onWishListSharingFinished(boolean success) {
                supportFinishAfterTransition();
                overridePendingTransition(R.anim.no_anim, R.anim.slide_out_bottom);
            }
        });
        wishListShareDialog.show();
    }

    private void applySelectedState(List<ProductItem> selecteItems) {
        switch (shareWishListState) {
            case PRODUCT_TO_SELECT:
                adapter.setSelectionMode(true);
                adapter.notifyDataSetChanged();
                if (sendWishListMenu != null) {
                    sendWishListMenu.setVisible(false);
                }

                break;
            case PRODUCTS_SELECTED:
                if (sendWishListMenu != null) {
                    sendWishListMenu.setVisible(true);
                }
                break;
        }
    }

    private void initRecycler() {


        GridLayoutManager layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.wishlist_columns_count));
        // optimization
        recyclerView.setHasFixedSize(true);
        // set layout
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        // Add 8dp spacing between items
        int spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(getResources().getInteger(R.integer.wishlist_columns_count), spacing, true));

    }
}
