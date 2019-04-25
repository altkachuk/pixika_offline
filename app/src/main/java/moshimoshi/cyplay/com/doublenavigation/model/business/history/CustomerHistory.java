package moshimoshi.cyplay.com.doublenavigation.model.business.history;

import org.parceler.Parcel;

import java.util.Date;

import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;

/**
 * Created by wentongwang on 17/05/2017.
 */
@Parcel
public class CustomerHistory implements Comparable<CustomerHistory> {

    String sellerId;
    Customer customer;
    Date date;
    String customerId;

    public CustomerHistory() {

    }

    public CustomerHistory(String sellerId, Customer customer, Date date) {
        this.sellerId = sellerId;
        this.customer = customer;
        this.date = date;
        this.customerId = customer.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomerHistory that = (CustomerHistory) o;

        return customerId != null ? customerId.equals(that.customerId) : that.customerId == null;

    }

    @Override
    public int hashCode() {
        return customerId != null ? customerId.hashCode() : 0;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int compareTo(CustomerHistory another) {
        return - this.getDate().compareTo(another.getDate());
    }
}
