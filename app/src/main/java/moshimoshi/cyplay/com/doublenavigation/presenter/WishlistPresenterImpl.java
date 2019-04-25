package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;
import android.util.Log;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.Shop;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sku;
import moshimoshi.cyplay.com.doublenavigation.model.business.WishlistItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.AddToWishlistActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.RemoveFromWishlistActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractResourceRequestCallback;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.SafeResourceRequestCallbackExecutor;
import moshimoshi.cyplay.com.doublenavigation.utils.ActionEventHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;
import moshimoshi.cyplay.com.doublenavigation.view.OpinionView;
import ninja.cyplay.com.apilibrary.domain.interactor.WishlistInteractor;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AWishlistItem;
import ninja.cyplay.com.apilibrary.models.business.ECustomerOpinion;
import ninja.cyplay.com.apilibrary.models.business.enums.EActionStatus;
import ninja.cyplay.com.apilibrary.utils.LogUtils;

/**
 * Created by damien on 15/05/15.
 */
public class WishlistPresenterImpl extends BasePresenter implements WishlistPresenter {

    @Inject
    ConfigHelper configHelper;

    @Inject
    CustomerContext customerContext;

    @Inject
    ActionEventHelper actionEventHelper;

    private OpinionView opinionView;
    private WishlistInteractor wishlistInteractor;

    public WishlistPresenterImpl(Context context, WishlistInteractor wishlistInteractor) {
        super(context);
        this.wishlistInteractor = wishlistInteractor;
    }

    @Override
    public void setView(OpinionView view) {
        this.opinionView = view;
    }

    @Override
    public void toggleWishlistItem(ECustomerOpinion opinion, Product product, Sku sku, Shop shop) {
        if (customerContext != null && customerContext.isCustomerIdentified()) {
            switch (opinion) {
                case O_WISHLIST:
                    wishlist(product, sku, shop);
                    break;
            }
        } else // This should never happen
            Log.d(LogUtils.generateTag(this), "Trying to perform an opinion Action outside customer context.");
    }


    public void wishlist(Product product, Sku sku, Shop shop) {
        if (customerContext != null && customerContext.isCustomerIdentified()) {
            WishlistItem wishlistItem = customerContext.getWishlistItem(product);
            if (wishlistItem != null) {
                deleteFromWishlist(wishlistItem);
            } else {
                addToWishlist(new WishlistItem(product, sku, shop));
            }
        }
    }

    public void addToWishlist(final WishlistItem wishlistItem) {
        if (wishlistItem.getProduct() != null && customerContext.getCustomer() != null) {
            wishlistInteractor.addProductWishlist(
                    customerContext.getCustomerId(),
                    wishlistItem,
                    configHelper.getCurrentShop().getId(),
                    new AbstractResourceRequestCallback<PR_AWishlistItem>() {

                        @Override
                        public void onSuccess(final PR_AWishlistItem pr_aWishlistItem) {
                            super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                                @Override
                                public void execute() {
                                    wishlistItem.setId(pr_aWishlistItem.getId());
                                    if (customerContext != null) {
                                        customerContext.addProductToWishlist(wishlistItem);
                                    }
                                    opinionView.onAddToWishlistSuccess(wishlistItem);
                                    actionEventHelper.log(new AddToWishlistActionEventData(wishlistItem.getSkuId())
                                            .addObjectParams(wishlistItem.getProduct())
                                            .setValue(wishlistItem.getId())
                                            .setStatus(EActionStatus.SUCCESS));
                                }
                            });

                        }

                        @Override
                        public void onError(final BaseException e) {
                            super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                                @Override
                                public void execute() {
                                    Log.e(LogUtils.generateTag(this), "Error on OpinionPresenterImpl");
                                    opinionView.onError(e.getType(), e.getResponse().getStatus(), e.getResponse().getDetail());
                                    actionEventHelper.log(new AddToWishlistActionEventData(wishlistItem.getSkuId())
                                            .addObjectParams(wishlistItem.getProduct())
                                            .setStatus(EActionStatus.FAILURE));
                                }
                            });
                        }
                    });
        }
    }

    public void deleteFromWishlist(final WishlistItem wishlistItem) {
        if (wishlistItem.getProduct() != null && customerContext.getCustomer() != null) {
            wishlistInteractor.deleteProductWishlist(customerContext.getCustomer().getId(),
                    wishlistItem.getId(),
                    new AbstractResourceRequestCallback<PR_AWishlistItem>() {

                        @Override
                        public void onSuccess(PR_AWishlistItem pr_aWishlistItem) {
                            if (customerContext != null)
                                customerContext.deleteProductFromWishlist(wishlistItem.getProduct());
                            opinionView.onDeleteFromWishlistSuccess();
                            actionEventHelper.log(new RemoveFromWishlistActionEventData(wishlistItem.getSkuId())
                                    .setValue(wishlistItem.getId())
                                    .addObjectParams(wishlistItem.getProduct())
                                    .setStatus(EActionStatus.SUCCESS)
                            );
                        }

                        @Override
                        public void onError(BaseException e) {
                            Log.e(LogUtils.generateTag(this), "Error on OpinionPresenterImpl");
                            opinionView.onError(e.getType(), e.getResponse().getStatus(), e.getResponse().getDetail());
                            actionEventHelper.log(new RemoveFromWishlistActionEventData(wishlistItem.getSkuId())
                                    .setValue(wishlistItem.getId())
                                    .addObjectParams(wishlistItem.getProduct())
                                    .setStatus(EActionStatus.FAILURE));
                        }

                    });
        }
    }

}