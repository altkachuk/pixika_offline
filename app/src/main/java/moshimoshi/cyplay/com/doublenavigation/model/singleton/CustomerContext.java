package moshimoshi.cyplay.com.doublenavigation.model.singleton;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.Basket;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketOffer;
import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerOfferState;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.Ticket;
import moshimoshi.cyplay.com.doublenavigation.model.business.WishlistItem;
import moshimoshi.cyplay.com.doublenavigation.model.events.ProductAddedToWishlistEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ProductRemovedFromWishlistEvent;
import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;

/**
 * Created by damien on 13/05/15.
 */
public class CustomerContext {

    EventBus bus;

    private String customerId;
    private boolean aliveCustomerActivityUp = false;

    public CustomerContext(Context context, EventBus bus) {
        ((PlayRetailApp) context.getApplicationContext()).inject(this);
        this.bus = bus;
    }

    // Customer
    private Customer customer;

    // Customer sales
    private List<Ticket> sales;

    /**
     * Check if a request to id customer info should be triggered
     *
     * @return
     */
    public String hasToRequestForCustomer() {
        String result = null;
        if (customerId != null && customer == null) {
            result = customerId;
        }
        return result;
    }

    public String hasToRequestForCustomerSales() {
        String result = null;
        if (customerId != null && sales == null) {
            result = customerId;
        }
        return result;
    }


    public boolean isCustomerIdentified() {
        return customer != null;
    }

    public void clearContext() {
        customer = null;
        customerId = null;
        sales = null;
    }




    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setBasket(Basket basket) {
        if (customer != null) {
            customer.setBasket(basket);
        }

    }

    public List<WishlistItem> getWishlist() {
        return customer != null ? customer.getWishlist() : null;
    }

    public Basket getBasket() {
        return customer != null ? customer.getBasket() : null;
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    public WishlistItem getWishlistItem(Product product) {
        WishlistItem result = null;
        if (product != null && customer != null && customer.getWishlist() != null) {
            for (WishlistItem wishlistItem : customer.getWishlist()) {
                if (product.equals(wishlistItem.getProduct())) {
                    return wishlistItem;
                }
            }
        }

        return result;
    }

    public WishlistItem getWishlistItem(WishlistItem wishlistItemToCheck) {
        WishlistItem result = null;
        if (wishlistItemToCheck != null && wishlistItemToCheck.getProduct() != null && customer != null && customer.getWishlist() != null) {
            for (WishlistItem wishlistItem : customer.getWishlist()) {
                if (wishlistItemToCheck.getProduct().equals(wishlistItem.getProduct())) {
                    return wishlistItem;
                }
            }
        }

        return result;
    }

    public Boolean isOfferInBasket(CustomerOfferState customerOfferState) {
        Boolean response = false;
        if (customerOfferState != null
                && customerOfferState.getOffer_id() != null
                && getBasket() != null
                && getBasket().getBasket_offers() != null) {
            for (BasketOffer basketOffer : getBasket().getBasket_offers()) {
                if (customerOfferState.getOffer_id().equals(basketOffer.getOffer_id())) {
                    response = true;
                    break;
                }
            }
        }
        return response;
    }

    public void addProductToWishlist(WishlistItem wishlistItem) {
        if (wishlistItem != null) {
            if (getWishlistItem(wishlistItem) == null) {
                this.getCustomer().getWishlist().add(wishlistItem);
            }
        }

        bus.post(new ProductAddedToWishlistEvent(wishlistItem.getProduct()));
    }

    public void deleteProductFromWishlist(Product product) {
        List<WishlistItem> wishlist = getWishlist();
        if (product != null && wishlist != null) {

            for (int i = 0; i < wishlist.size(); i++) {
                if (wishlist.get(i).getProduct() != null && wishlist.get(i).getProduct().equals(product)) {
                    wishlist.remove(wishlist.get(i));
                    break;
                }
            }
        }
        bus.post(new ProductRemovedFromWishlistEvent(product));

    }

    // -------------------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------------------

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.clearContext();
        this.customerId = customerId;
    }

    public List<Ticket> getSales() {
        return sales;
    }

    public void setSales(List<Ticket> sales) {
        this.sales = sales;
    }

    public boolean isAliveCustomerActivityUp() {
        return aliveCustomerActivityUp;
    }

    public void setAliveCustomerActivityUp(boolean aliveCustomerActivityUp) {
        this.aliveCustomerActivityUp = aliveCustomerActivityUp;
    }

    public String getPersonalEmail() {
        String result = null;
        if (getCustomer() != null
                && getCustomer().getDetails() != null
                && getCustomer().getDetails().getEmail() != null) {
            result = getCustomer().getDetails().getEmail().getPersonal();
        }
        return result;
    }

    public String getProfessionalEmail() {
        String result = null;
        if (getCustomer() != null
                && getCustomer().getDetails() != null
                && getCustomer().getDetails().getEmail() != null) {
            result = getCustomer().getDetails().getEmail().getProfessional();
        }
        return result;
    }

}