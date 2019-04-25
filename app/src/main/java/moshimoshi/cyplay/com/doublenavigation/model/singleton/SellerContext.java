package moshimoshi.cyplay.com.doublenavigation.model.singleton;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.Basket;
import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.model.business.Seller;
import ninja.cyplay.com.apilibrary.models.singleton.TweetCacheManager;

/**
 * Created by damien on 29/04/15.
 */
public class SellerContext {

    private Seller seller;

    private List<Customer> customer_history = new ArrayList<>();

    private Basket basket;

    private final Context context;

    public SellerContext(Context context, TweetCacheManager tweetCacheManager) {
        this.context = context;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public boolean isSellerIdentified() {
        return seller != null;
    }


    public String getFormatName() {
        StringBuilder builder = new StringBuilder();
        if (seller != null) {
            builder.append(seller.getFirst_name());
            builder.append(" ");
            builder.append(seller.getLast_name());
        }
        return builder.toString();
    }

    public List<Customer> getCustomer_history() {
        return customer_history;
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    public void setCustomer_history(List<Customer> cl) {
        // transform a customer details to customer preview
        if (customer_history == null)
            customer_history = new ArrayList<>();
        if (!customer_history.isEmpty())
            customer_history.clear();
        this.customer_history = cl;
    }


    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private boolean bringCustomerToTop(Customer customer) {
        if (customer_history != null) {
            for (int i = 0; i < customer_history.size(); i++) {
                if (customer.getEan_card() != null && customer.getEan_card().equals(customer_history.get(i).getEan_card())) {
                    customer_history.remove(i);
                    customer_history.add(0, customer);
                    return true;
                }
            }
        }
        return false;
    }

    public void addCustomerToHistory(Customer customer) {
        if (customer_history == null){
            customer_history = new ArrayList<>();
        }
        if (!bringCustomerToTop(customer)){
            customer_history.add(0, customer);
        }

    }

    public void clearContext() {
        this.seller = null;
        this.customer_history = null;
        this.basket = null;
    }

    public Context getContext() {
        return context;
    }
}
