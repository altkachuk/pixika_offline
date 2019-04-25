package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.ETransactionStatus;
import ninja.cyplay.com.apilibrary.models.ModelResource;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_APaymentTransaction;

/**
 * Created by romainlebouc on 05/12/2016.
 */
@Parcel
@ModelResource
public class CustomerPaymentTransactionStatus extends PR_APaymentTransaction {

    String status;
    String payment_ref;

    public CustomerPaymentTransactionStatus() {

    }

    public CustomerPaymentTransactionStatus(CustomerPaymentTransaction customerPaymentTransaction) {
        this.status = customerPaymentTransaction.getStatus();
        this.payment_ref = customerPaymentTransaction.getPayment_ref();
    }

    public String getStatus() {
        return status;
    }

    public void setEPaymentStatus(ETransactionStatus eTransactionStatus) {
        this.status = eTransactionStatus.getCode();
    }

    public String getPayment_ref() {
        return payment_ref;
    }

    public void setPayment_ref(String payment_ref) {
        this.payment_ref = payment_ref;
    }
}
