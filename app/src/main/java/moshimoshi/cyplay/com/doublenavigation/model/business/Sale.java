package moshimoshi.cyplay.com.doublenavigation.model.business;

import java.util.Arrays;
import java.util.List;

import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_Sale;

/**
 * Created by andre on 28-Feb-19.
 */

public class Sale extends PR_Sale {

    public transient final static List<String> CUSTOMER_PROJECTION = Arrays.asList(
            "id",
            "customer",
            "basket");

    Customer customer;
    Basket basket;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }
}
