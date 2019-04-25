package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EAddressType;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AAddress;

/**
 * Created by romainlebouc on 07/12/2016.
 */
@Parcel
public class Address extends PR_AAddress {

    String first_name;
    String last_name;
    Mail mail;
    CustomerDetailsPhone phone;
    CustomerDetailsEmail email;

    private transient EAddressType addressType = EAddressType.EXTRA;

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Mail getMail() {
        return mail;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }

    public CustomerDetailsPhone getPhone() {
        return phone;
    }

    public void setPhone(CustomerDetailsPhone phone) {
        this.phone = phone;
    }

    public CustomerDetailsEmail getEmail() {
        return email;
    }

    public void setEmail(CustomerDetailsEmail email) {
        this.email = email;
    }

    public String getFormatName() {
        return (last_name != null && last_name.length() > 0 ? last_name + " " : "") +
                (first_name != null && first_name.length() > 0 ? first_name : "");
    }

    public Address() {
        this.mail = new Mail();
        this.phone = new CustomerDetailsPhone();
        this.email = new CustomerDetailsEmail();
    }

    public Address(Customer customer, EAddressType addressType) {
        this();
        this.addressType = addressType;
        if (customer != null && customer.getDetails() != null) {
            this.setFirst_name(customer.getDetails().getFirst_name());
            this.setLast_name(customer.getDetails().getLast_name());
            this.setMail(customer.getDetails().getMail());
            this.setPhone(customer.getDetails().getPhone());
            this.setEmail(customer.getDetails().getEmail());
        }
    }


    public EAddressType getAddressType() {
        return addressType;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address that = (Address) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }


}
