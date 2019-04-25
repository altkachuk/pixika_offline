package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.view.View;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerPaymentItem;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.BasketItemCore;

/**
 * Created by wentongwang on 15/03/2017.
 */

public class DeliveryItemViewHolder extends ItemViewHolder<CustomerPaymentItem>  {

    @BindView(R.id.paiement_item_core)
    BasketItemCore basketItemCore;


    public DeliveryItemViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setItem(CustomerPaymentItem customerPaymentItem) {
        BasketItem item = new BasketItem(customerPaymentItem.getProduct(), null, null, null);
        item.setQty(customerPaymentItem.getQty());
        item.setUnit_price(customerPaymentItem.getUnit_price());
        item.setTotal(customerPaymentItem.getTotal());
        item.setTotal_discount(customerPaymentItem.getTotal_discount());
        item.setItem_total_amount(customerPaymentItem.getItem_total_amount());
        item.setSku_id(customerPaymentItem.getSku_id());
        item.setProduct_id(customerPaymentItem.getProduct_id());
        basketItemCore.setBasketItem(item);
    }
}
