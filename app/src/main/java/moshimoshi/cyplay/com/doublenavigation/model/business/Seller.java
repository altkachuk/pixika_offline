package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import ninja.cyplay.com.apilibrary.models.ModelResource;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ASeller;

@Parcel
@ModelResource
public class Seller extends PR_ASeller implements PicturedResource {

    String id;
    String username;
    String first_name;
    String last_name;
    Boolean has_temp_password;
    Basket basket;
    String password;
    String shop_id;

    public String getFormatName() {
        return (first_name != null && first_name.length() > 0 ? first_name + " " : "") +
                (last_name != null && last_name.length() > 0 ? last_name : "");
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    @Override
    public String getImage() {
        return null;
    }

    @Override
    public String getInitiales() {
        StringBuilder builder = new StringBuilder();
        if (first_name != null && first_name.length() > 0)
            builder.append(first_name.charAt(0));
        if (last_name != null && last_name.length() > 0)
            builder.append(last_name.charAt(0));
        return builder.toString();
    }

    public Basket getBasket() {
        return basket;
    }

    public Boolean getHas_temp_password() {
        return has_temp_password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.first_name = firstName;
    }

    public void setLastName(String lastName) {
        this.last_name = lastName;
    }

    public void setHasTempPassword(Boolean hasTempPassword) {
        this.has_temp_password = hasTempPassword;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getShopId() {
        return shop_id;
    }

    public void setShopId(String shopId) {
        this.shop_id = shopId;
    }
}