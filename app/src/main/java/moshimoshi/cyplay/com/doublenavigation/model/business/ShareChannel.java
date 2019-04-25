package moshimoshi.cyplay.com.doublenavigation.model.business;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPaymentShareMode;

/**
 * Created by wentongwang on 29/03/2017.
 */

public class ShareChannel {
    private String payment_share_mode;
    private String payment_share_reference;

    public ShareChannel(){

    }

    public EPaymentShareMode getPaymentShareMode() {
        return EPaymentShareMode.valueOfEPaymentShareMode(payment_share_mode);
    }

    public String getPaymentShareReference() {
        return payment_share_reference;
    }

    public void setPaymentShareMode(String payment_share_mode) {
        this.payment_share_mode = payment_share_mode;
    }

    public void setPaymentShareReference(String payment_share_reference) {
        this.payment_share_reference = payment_share_reference;
    }
}
