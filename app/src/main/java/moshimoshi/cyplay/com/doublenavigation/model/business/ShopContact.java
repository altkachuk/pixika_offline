package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

@Parcel
public class ShopContact {
    String fax_number;
    String phone_number;
    String email;

    public String getFax_number() {
        return fax_number;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getEmail() {
        return email;
    }
}
