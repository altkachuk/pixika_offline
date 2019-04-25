package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPurchaseCollectionMode;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EShopType;
import ninja.cyplay.com.apilibrary.models.ModelResource;
import ninja.cyplay.com.apilibrary.models.ReadOnlyModelField;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ADelivery;


/**
 * Created by romainlebouc on 28/11/2016.
 */
@Parcel
@ModelResource
public class PurchaseCollection extends PR_ADelivery {

    String type;
    String shop_id;
    String shipping_address_id;
    String billing_address_id;
    String delivery_mode_id;

    @ReadOnlyModelField
    DeliveryMode delivery_mode ;

    String name;

    @ReadOnlyModelField
    Mail address;

    public void setType(EPurchaseCollectionMode ePurchaseCollectionMode) {
        this.type = ePurchaseCollectionMode != null ? ePurchaseCollectionMode.getCode() : null;
    }

    public String getType() {
        return type;
    }

    public Mail getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getShop_id() {
        return shop_id;
    }

    public String getShipping_address_id() {
        return shipping_address_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public void setShipping_address_id(String shipping_address_id) {
        this.shipping_address_id = shipping_address_id;
    }

    public void setDeliveryMode(DeliveryMode deliveryMode) {
        this.delivery_mode = deliveryMode;
        if (deliveryMode != null) {
            this.delivery_mode_id = deliveryMode.getId();
        }
    }

    public String getBilling_address_id() {
        return billing_address_id;
    }

    public DeliveryMode getDelivery_mode() {
        return delivery_mode;
    }

    public void setBilling_address_id(String billing_address_id) {
        this.billing_address_id = billing_address_id;
    }

    public EPurchaseCollectionMode getEPurchaseCollectionMode() {
        return EPurchaseCollectionMode.getEDeliveryDestinationTypeFromCode(this.type);
    }

    public static List<PurchaseCollection> getAvailableDeliveries(EPurchaseCollectionMode ePurchaseCollectionMode,
                                                                  Customer customer,
                                                                  Sku sku,
                                                                  Shop currentShop) {
        switch (ePurchaseCollectionMode) {
            case HOME_DELIVERY:
                return getAvailableHomeDeliveries(customer, sku);
            case SHOP_DELIVERY:
                return getAvailableShopDeliveries(sku, currentShop);
            case SHOP_NOW:
                return getAvailableShopCollectDeliveries(sku, currentShop);
            default:
                return null;
        }
    }

    private static List<PurchaseCollection> getAvailableHomeDeliveries(Customer customer, Sku sku) {

        Shop webShop = Ska.getWebShop(sku.getAvailabilities(), EShopType.WEB);

        List<PurchaseCollection> result = new ArrayList<>();

        // Add Home address
        if (customer != null
                && customer.getDetails() != null) {
            PurchaseCollection homeDestination = new HomeDelivery(webShop/*,
                    customer.getDetails().getMail(),
                    null,
                    customer.getDetails().getFormatName()*/);
            result.add(homeDestination);
        }

        // Add Shipping addresses
        if (customer != null) {
            List<Address> shippingAddresses = customer.getAddresses();
            if (shippingAddresses != null) {
                for (Address shippingAddress : shippingAddresses) {
                    PurchaseCollection homeDestination = new HomeDelivery(webShop/*,
                        shippingAddress.getMail(),
                        shippingAddress.getCustomerId(),
                        shippingAddress.getFormatName()*/);
                    result.add(homeDestination);
                }
            }
        }


        // Add Shipping addresses
        return result;
    }

    private static List<PurchaseCollection> getAvailableShopCollectDeliveries(Sku sku, Shop currentShop) {
        List<PurchaseCollection> result = new ArrayList<>();
        PurchaseCollection shopDestination = new ShopNow(currentShop);
        result.add(shopDestination);
        return result;
    }

    private static List<PurchaseCollection> getAvailableShopDeliveries(Sku sku, Shop currentShop) {
        List<PurchaseCollection> result = new ArrayList<>();
        PurchaseCollection shopDestination = new ShopDelivery(currentShop);
        result.add(shopDestination);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PurchaseCollection that = (PurchaseCollection) o;

        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        return shop_id != null ? shop_id.equals(that.shop_id) : that.shop_id == null;

    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (shop_id != null ? shop_id.hashCode() : 0);
        return result;
    }

}
