package moshimoshi.cyplay.com.doublenavigation.model.events;

import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;

/**
 * Created by wentongwang on 24/04/2017.
 */

public class CustomerLoadedEvent extends LimitedConsumptionEvent {

    private final Customer customer;
    private final boolean linkSellerToCustomer;

    public CustomerLoadedEvent(Customer customer) {
        super(1);
        this.customer = customer;
        linkSellerToCustomer = true;
    }

    public CustomerLoadedEvent(Customer customer, boolean linkSellerToCustomer) {
        super(1);
        this.customer = customer;
        this.linkSellerToCustomer = linkSellerToCustomer;
    }


    public Customer getCustomer() {
        return customer;
    }

    public boolean isLinkSellerToCustomer() {
        return linkSellerToCustomer;
    }
}
