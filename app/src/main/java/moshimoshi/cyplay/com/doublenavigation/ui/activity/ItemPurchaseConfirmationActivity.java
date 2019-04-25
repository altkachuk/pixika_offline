package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import org.parceler.Parcels;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Basket;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.HomeDelivery;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.PurchaseCollection;
import moshimoshi.cyplay.com.doublenavigation.model.business.ShopDelivery;
import moshimoshi.cyplay.com.doublenavigation.model.business.ShopNow;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sku;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.ProductStockChoiceConfig;
import moshimoshi.cyplay.com.doublenavigation.model.business.utils.PurchaseCollectionModeStock;
import moshimoshi.cyplay.com.doublenavigation.model.business.utils.ThreeSixtyStocks;
import moshimoshi.cyplay.com.doublenavigation.presenter.BasketPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.animation.AnimationUtils;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.BasketItemCore;

/**
 * Created by romainlebouc on 20/01/2017.
 */

public class ItemPurchaseConfirmationActivity extends BaseActivity {

    @Inject
    BasketPresenter basketPresenter;

    @BindView(R.id.add_to_basket_status_title)
    TextView addToBasketStatusTitle;

    @BindView(R.id.action_cancel_purchase_confirmation)
    TextView cancel;

    @BindView(R.id.basket_item_core)
    BasketItemCore basketItemCore;

    @BindView(R.id.pop_up_container)
    View popUpContainer;

    @BindView(R.id.background)
    View background;

    @BindView(R.id.action_go_to_basket)
    TextView actionGoToBasket;

    @BindView(R.id.add_product_message_container)
    View addProductMessageContainer;

    private Sku sku;
    private Product product;
    private ThreeSixtyStocks threeSixtyStocks;

    private boolean isInBasket;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.connectedActivity = false;
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        setContentView(R.layout.activity_purchase_confirmation);
        product = Parcels.unwrap(this.getIntent().getParcelableExtra(IntentConstants.EXTRA_PRODUCT));
        sku = Parcels.unwrap(this.getIntent().getParcelableExtra(IntentConstants.EXTRA_SKU));
        threeSixtyStocks = Parcels.unwrap(this.getIntent().getParcelableExtra(IntentConstants.EXTRA_PURCHASE_COLLECTION_360_STOCKS));


        BasketItem basketItem = getInitialBasketItem();
        basketItemCore.setBasketItemWithOutOffers(basketItem);
        basketItemCore.fillStocks(threeSixtyStocks, false, basketItem);
        basketItemCore.setBasketItemChangeListener(new BasketItemCore.BasketItemChangeListener() {
            @Override
            public void onChangeBasketItem(int quantity, PurchaseCollection purchaseCollection, final BasketItemCore.BasketItemChangeConfirmationListener listener) {
                actionGoToBasket.setEnabled(quantity > 0);

                basketItemCore.getBasketItem().setQty(quantity);
                basketItemCore.getBasketItem().setPurchaseCollection(purchaseCollection);
                basketItemCore.setQuantity(quantity);
                listener.onConfirm(true);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        AnimationUtils.animDimBackground(background, this.getResources().getInteger(R.integer.transition_speed));

        ViewTreeObserver vto = basketItemCore.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                popUpContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Animation animation = new TranslateAnimation(0, 0, popUpContainer.getHeight(), 0);
                animation.setFillAfter(true);
                animation.setDuration(ItemPurchaseConfirmationActivity.this.getResources().getInteger(R.integer.transition_speed));
                popUpContainer.startAnimation(animation);
            }
        });

    }

    private BasketItem getInitialBasketItem() {
        Basket basket = basketPresenter.getCachedBasket();
        BasketItem result = null;
        if (basket != null) {
            BasketItem basketItem = basket.getBasketItem(product,
                    sku);
            if (basketItem != null) {
                isInBasket = true;
                try {
                    //clone the object, if not, when qty is changed, the qty in the basket will be changed
                    result = (BasketItem) basketItem.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                if (result == null) {
                    result = basketItem;
                }
                result.setQty(1);
                addProductMessageContainer.setVisibility(View.VISIBLE);
            }
        }

        if (result == null) {
            isInBasket = false;
            //set default selection
            for (PurchaseCollectionModeStock purchaseCollectionModeStock : threeSixtyStocks.getPurchaseCollectionModeStocks()) {
                ProductStockChoiceConfig productStockChoiceConfig = configHelper.getProductConfig().getStock().getProductStockChoiceConfig(purchaseCollectionModeStock.getePurchaseCollectionMode());
                if (productStockChoiceConfig.getMin_real_stock() != null &&
                        purchaseCollectionModeStock.getStock() >= productStockChoiceConfig.getMin_real_stock()) {
                    PurchaseCollection mode = null;
                    switch (purchaseCollectionModeStock.getePurchaseCollectionMode()) {
                        case SHOP_DELIVERY:
                            mode = new ShopDelivery(configHelper.getCurrentShop());
                            break;
                        case HOME_DELIVERY:
                            mode = new HomeDelivery(configHelper.getCurrentShop());
                            break;
                        default:
                        case SHOP_NOW:
                            mode = new ShopNow(configHelper.getCurrentShop());
                            break;
                    }

                    result = new BasketItem(product,
                            sku,
                            configHelper.getCurrentShop(),
                            mode);
                    break;
                }
            }
            //if all stocks < min_real_stock, set default value
            if (result == null) {
                result = new BasketItem(product,
                        sku,
                        configHelper.getCurrentShop(),
                        new ShopNow(configHelper.getCurrentShop()));
            }

            result.setQty(1);
        }

        return result;

    }


    public void finishActivity() {

        Animation animation = new TranslateAnimation(0, 0, 0, popUpContainer.getHeight());
        animation.setFillAfter(true);
        animation.setDuration(ItemPurchaseConfirmationActivity.this.getResources().getInteger(R.integer.transition_speed));
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ItemPurchaseConfirmationActivity.this.finishActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        popUpContainer.startAnimation(animation);

        supportFinishAfterTransition();

    }

    @Override
    public void onBackPressed() {
        this.finishActivity();
    }

    @OnClick(R.id.action_cancel_purchase_confirmation)
    public void onCancelConfirmation() {
        this.finishActivity();
    }

    @OnClick(R.id.action_go_to_basket)
    public void onGoToBasket() {
        Intent intent = new Intent(this, BasketActivity.class);
        intent.putExtra(IntentConstants.EXTRA_BASKET_ITEM, Parcels.wrap(getItemAddToBasket()));
        this.startActivity(intent);
        this.finishActivity();
    }

    @OnClick(R.id.action_continue_purchases)
    public void onContinuePurchases() {
        if (basketItemCore.getBasketItem().getQty() > 0)
            basketPresenter.addUpdateOrDeleteBasketItem(getItemAddToBasket());

        this.finishActivity();
    }

    @OnClick(R.id.background)
    public void onDismissClick() {
        this.finishActivity();
    }


    @OnClick(R.id.pop_up_container)
    public void onDismissPopupClick() {
    }

    private BasketItem getItemAddToBasket() {
        BasketItem result = null;
        result = basketItemCore.getBasketItem();
        if (isInBasket) {
            Basket basket = basketPresenter.getCachedBasket();
            result.setQty(result.getQty() + basket.getBasketItem(product, sku).getQty());
        }

        return result;
    }

}
