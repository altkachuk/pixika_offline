package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ninja.cyplay.com.apilibrary.models.ModelField;
import ninja.cyplay.com.apilibrary.models.ModelResource;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ACustomer;


@Parcel
@ModelResource
public class Customer extends PR_ACustomer {

    public transient final static List<String> CUSTOMER_PROJECTION = Arrays.asList(
            "id",
            "ean_card",
            "details",
            "professional_details",
            "kpi",
            "loyalty",
            "wishlist",
            "addresses",
            "means_of_payment",
            "offers");

    @ModelField(eventLogging = true)
    String ean_card;

    @ModelField(embedded = true, eventLogging = true)
    CustomerDetails details;

    @ModelField(embedded = true, eventLogging = true)
    ProfessionalDetails professional_details;

    @ModelField(embedded = true, eventLogging = true)
    CustomerKpi kpi;

    @ModelField(embedded = true, eventLogging = true)
    CustomerLoyalty loyalty;

    MeansOfPayment means_of_payment;

    List<WishlistItem> wishlist;
    Basket basket;
    CustomerOffers offers;
    List<Address> addresses;
    String source;

    public Customer() {
        this.details = new CustomerDetails();
        this.professional_details = new ProfessionalDetails();
        this.kpi = new CustomerKpi();
        this.wishlist = new ArrayList<>();
        this.basket = new Basket();
        this.offers = new CustomerOffers();
        this.means_of_payment = new MeansOfPayment();
    }



    public String getEan_card() {
        return ean_card;
    }

    public void setEan_card(String ean_card) {
        this.ean_card = ean_card;
    }

    public CustomerDetails getDetails() {
        return details;
    }

    public void setDetails(CustomerDetails details) {
        this.details = details;
    }

    public ProfessionalDetails getProfessionalDetails() {
        return professional_details;
    }

    public void setProfessionalDetails(ProfessionalDetails professionalDetails) {
        this.professional_details = professionalDetails;
    }

    public List<WishlistItem> getWishlist() {
        wishlist = wishlist != null ? wishlist : new ArrayList<WishlistItem>();
        return wishlist;
    }

    public MeansOfPayment getMeans_of_payment() {
        return means_of_payment;
    }

    public CustomerKpi getKpi() {
        return kpi;
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    public CustomerOffers getOffers() {
        return offers;
    }

    public void setOffers(CustomerOffers offers) {
        this.offers = offers;
    }

    public CustomerLoyalty getLoyalty() {
        return loyalty;
    }

    public List<Address> getAddresses() {
        if (addresses == null) {
            addresses = new ArrayList<>();
        }
        return addresses;
    }

    public Address getAddress(String addressId) {
        Address result = null;
        if (addresses != null && addressId != null) {
            for (Address address : addresses) {
                if (addressId.equals(address.getId())) {
                    result = address;
                    break;
                }
            }
        }
        return result;
    }

    public void updateAddress(Address address) {
        int indexOf = addresses.indexOf(address);
        if (indexOf >= 0) {
            addresses.set(indexOf, address);
        }
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
