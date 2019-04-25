package moshimoshi.cyplay.com.doublenavigation.model.business.v2;

import java.util.List;

import ninja.cyplay.com.apilibrary.models.abstractmodels.v2.Contact;
import ninja.cyplay.com.apilibrary.models.abstractmodels.v2.PR_AData;

/**
 * Created by andre on 22-Mar-19.
 */

public class Data extends PR_AData {
    List<Seller> sellers;
    List<Contact> contacts;
    List<ProductData> products;

    public List<Seller> getSellers() {
        return sellers;
    }

    public void setSellers(List<Seller> sellers) {
        this.sellers = sellers;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public List<ProductData> getProducts() {
        return products;
    }

    public void setProducts(List<ProductData> products) {
        this.products = products;
    }
}
