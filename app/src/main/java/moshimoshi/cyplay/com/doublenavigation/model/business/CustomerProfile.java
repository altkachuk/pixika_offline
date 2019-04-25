package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import ninja.cyplay.com.apilibrary.models.ModelField;
import ninja.cyplay.com.apilibrary.models.ModelResource;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ACustomer;


@Parcel
@ModelResource
public class CustomerProfile extends PR_ACustomer {

    @ModelField(embedded = true, eventLogging = true)
    CustomerDetails details;

    @ModelField(embedded = true, eventLogging = true)
    ProfessionalDetails professional_details;

    @ModelField(eventLogging = true)
    String ean_card;

    String source;

    MeansOfPayment means_of_payment;

    public CustomerProfile() {

    }

    public CustomerProfile(Customer customer) {
        this.id = customer.getId();
        this.details = customer.details;
        this.professional_details = customer.professional_details;
        this.ean_card = customer.ean_card;
        this.means_of_payment = customer.means_of_payment;
        this.source = customer.getSource();
    }

    public CustomerProfile(Customer customer, Address address) {
        this.id = customer.getId();
        this.details = customer.details;
        this.professional_details = customer.professional_details;
        this.ean_card = customer.ean_card;
        this.means_of_payment = customer.means_of_payment;
        this.details.updateFromAddress(address);
        this.source = customer.getSource();
    }

}
