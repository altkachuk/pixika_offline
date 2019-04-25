package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.PurchaseCollection;
import moshimoshi.cyplay.com.doublenavigation.model.business.Address;
import moshimoshi.cyplay.com.doublenavigation.model.business.Ska;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sku;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPurchaseCollectionMode;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;

/**
 * Created by romainlebouc on 28/11/2016.
 */

public class BasketItemDelivery extends LinearLayout {

    @BindView(R.id.delivery_address)
    TextView deliveryAddress;

    @BindView(R.id.delivery_mode)
    TextView deliveryMode;

    private BasketItem basketItem;
    private PurchaseCollection purchaseCollection;

    public BasketItemDelivery(Context context) {
        this(context, null);
    }

    public BasketItemDelivery(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BasketItemDelivery(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.cell_basket_item_delivery, this, true);
        // Bind xml
        ButterKnife.bind(this);
    }

    public void setDeliveryDestination(BasketItem basketItem, PurchaseCollection purchaseCollection, CustomerContext customerContext) {
        this.purchaseCollection = purchaseCollection;
        this.basketItem = basketItem;

        EPurchaseCollectionMode ePurchaseCollectionMode = purchaseCollection.getEPurchaseCollectionMode();
        if (ePurchaseCollectionMode != null) {
            switch (ePurchaseCollectionMode) {
                case HOME_DELIVERY:
                    // If shipping address => We use this one
                    //
                    if (purchaseCollection.getShipping_address_id() != null
                            && customerContext.getCustomer() != null
                            ) {
                        Address shippingAddress = customerContext.getCustomer().getAddress(purchaseCollection.getShipping_address_id());
                        if (shippingAddress != null && shippingAddress.getMail() != null) {
                            deliveryAddress.setText(shippingAddress.getMail().getOneLineAddress());
                        } else {
                            //TODO ??
                        }
                        // If no shipping address, we use the default address
                    } else if (customerContext.getCustomer() != null
                            && customerContext.getCustomer().getDetails() != null
                            && customerContext.getCustomer().getDetails().getMail() != null) {
                        deliveryAddress.setText(customerContext.getCustomer().getDetails().getMail().getOneLineAddress());
                    } else {
                        //TODO ??
                    }
                    break;
                case SHOP_DELIVERY:
                    Sku sku = basketItem.getProduct() != null ? basketItem.getProduct().getSku(basketItem.getSku_id()) : null;
                    Ska ska = sku != null ? sku.getSkaForShop(purchaseCollection.getShop_id()) : null;
                    if (ska != null
                            && ska.getShop() != null
                            && ska.getShop().getMail() != null) {
                        deliveryAddress.setText(ska.getShop().getName(true));
                    } else {
                        deliveryAddress.setText(null);
                    }

                    break;
            }
        }


        deliveryMode.setText(purchaseCollection.getEPurchaseCollectionMode().getLabelId());


        // Get the sku of the basket item
        Sku sku = basketItem.getProduct().getSku(basketItem.getSku_id());
        if (sku != null) {
            switch (purchaseCollection.getEPurchaseCollectionMode()) {
                // From the web stock
                case HOME_DELIVERY:
                    Ska webSka = sku.getWeb_shop_availability();
                    break;
                // From the stock of a shop
                case SHOP_DELIVERY:
                    Ska shopSka = sku.getSkaForShop(purchaseCollection.getShop_id());
                    break;
            }
        } else {
            //TODO
        }

        basketItem.getPurchaseCollection().getEPurchaseCollectionMode();

    }

}
