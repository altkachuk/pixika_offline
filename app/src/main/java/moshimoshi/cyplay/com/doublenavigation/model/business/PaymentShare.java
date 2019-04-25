package moshimoshi.cyplay.com.doublenavigation.model.business;

import java.util.List;

import ninja.cyplay.com.apilibrary.models.ModelResource;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_APaymentShare;

/**
 * Created by wentongwang on 29/03/2017.
 */
@ModelResource
public class PaymentShare extends PR_APaymentShare {

    private String customer_id;
    private String lang;
    private String payment_id;
    private String seller_id;
    private String shop_id;
    private List<ShareChannel> sharing_channels;

    public String getCustomer_id() {
        return customer_id;
    }

    public String getLang() {
        return lang;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public List<ShareChannel> getSharing_channels() {
        return sharing_channels;
    }


    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public void setSharing_channels(List<ShareChannel> sharing_channels) {
        this.sharing_channels = sharing_channels;
    }
}
