package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import java.util.List;

import ninja.cyplay.com.apilibrary.models.ModelResource;
import ninja.cyplay.com.apilibrary.models.ReadOnlyModelField;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AProductShare;

/**
 * Created by romainlebouc on 24/08/16.
 */
@Parcel
@ModelResource
public class ProductShare extends PR_AProductShare{
    public final static transient String EMAIL_CHANNEL = "email";

    @ReadOnlyModelField
    String id;

    String channel;
    String channel_ref;
    String customer_id;
    List<String> product_ids;
    String seller_id;
    String shop_id;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getChannel_ref() {
        return channel_ref;
    }

    public void setChannel_ref(String channel_ref) {
        this.channel_ref = channel_ref;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public List<String> getProduct_ids() {
        return product_ids;
    }

    public void setProduct_ids(List<String> product_ids) {
        this.product_ids = product_ids;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }
}
