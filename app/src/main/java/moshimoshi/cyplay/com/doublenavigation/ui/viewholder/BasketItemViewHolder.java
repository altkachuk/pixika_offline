package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.PurchaseCollection;
import moshimoshi.cyplay.com.doublenavigation.model.business.utils.BasketItemWithStock;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.BasketPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.BasketItemCore;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;

/**
 * Created by damien on 18/04/16.
 */
public class BasketItemViewHolder extends ItemViewHolder<BasketItemWithStock> {

    @Inject
    ConfigHelper configHelper;

    @Inject
    CustomerContext customerContext;

    @BindView(R.id.basket_item_core)
    BasketItemCore basketItemCore;

    @BindView(R.id.increase_sku_quantity)
    ImageView increaseSkuQuantity;

    final BasketPresenter basketPresenter;

    public BasketItemViewHolder(View view, final BasketPresenter basketPresenter) {
        super(view);
        this.basketPresenter = basketPresenter;
        basketItemCore.setBasketItemChangeListener(new BasketItemCore.BasketItemChangeListener() {
            @Override
            public void onChangeBasketItem(final int quantity, final PurchaseCollection purchaseCollection, final BasketItemCore.BasketItemChangeConfirmationListener listener) {

                if (quantity == 0) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(BasketItemViewHolder.this.context);
                    builder.setTitle(R.string.warning);
                    builder.setMessage(R.string.delete_confirmation);
                    builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listener.onConfirm(false);
                        }
                    });
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listener.onConfirm(true);
                            updateBasket(quantity, purchaseCollection);
                        }
                    });
                    builder.create().show();
                } else {
                    updateBasket(quantity, purchaseCollection);
                }

            }
        });

    }

    private void updateBasket(int quantity, PurchaseCollection purchaseCollection) {
        BasketItem basketItem = basketItemCore.getBasketItem();
        basketItem.setQty(quantity);
        basketItem.setPurchaseCollection(purchaseCollection);
        basketPresenter.addUpdateOrDeleteBasketItem(basketItem);
    }


    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------


    public void setItem(BasketItemWithStock basketItemWithStock, BasketItem itemOnModification) {
        BasketItem basketItem = basketItemWithStock.getBasketItem();
        basketItemCore.setBasketItem(basketItem);
        if (configHelper.shouldGetStockAsynchronously() && basketItem.getProduct() != null) {
            if (basketItemWithStock.getThreeSixtyStocks() != null) {
                basketItemCore.fillStocks(basketItemWithStock.getThreeSixtyStocks(), true, itemOnModification);
            }
        }
    }

    @Override
    public void setItem(BasketItemWithStock basketItemWithStock) {
        setItem(basketItemWithStock, null);
    }

    public void onSelectStateChange(boolean select){
        basketItemCore.onSelectStateChange(select);
    }

    public void setOnSelectListener(View.OnClickListener listener) {
        basketItemCore.setOnSelectListener(listener);
    }
}
