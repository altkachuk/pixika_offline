package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import ninja.cyplay.com.apilibrary.models.ModelResource;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_APayment;


/**
 * Created by romainlebouc on 05/12/2016.
 */
@Parcel
@ModelResource
public class CustomerPaymentStatus extends PR_APayment {
    String status;

    public CustomerPaymentStatus() {

    }

    public CustomerPaymentStatus(Payment payment) {
        this.status = payment.getStatus();
    }
}
