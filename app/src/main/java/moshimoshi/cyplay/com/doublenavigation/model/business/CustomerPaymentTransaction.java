package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import java.util.Date;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPaymentType;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.ETransactionStatus;
import ninja.cyplay.com.apilibrary.models.ModelResource;
import ninja.cyplay.com.apilibrary.models.ReadOnlyModelField;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_APaymentTransaction;

/**
 * Created by romainlebouc on 05/12/2016.
 */
@Parcel
@ModelResource
public class CustomerPaymentTransaction extends PR_APaymentTransaction {

    public final static String MERCHANT_ACCOUNT_KEY = "MERCHANT_ACCOUNT";

    @ReadOnlyModelField
    String id;

    String payment_type;
    Double amount;
    Date creation_date;
    Date due_date;
    String status;
    String payment_ref;
    List<AdditionalData> additional_data;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public Double getAmount() {
        return amount;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public Date getDue_date() {
        return due_date;
    }

    public String getStatus() {
        return status;
    }

    public ETransactionStatus getETransactionStatus() {
        return ETransactionStatus.getETransactionStatusFromCode(status);
    }

    public void setETransactionStatus(ETransactionStatus eTransactionStatus) {
        this.status = eTransactionStatus.getCode();
    }

    public EPaymentType getEPaymentType() {
        return EPaymentType.getEPaymentTypeFromCode(this.payment_type);
    }

    public void setEPaymentType(EPaymentType ePaymentType) {
        this.payment_type = ePaymentType.getCode();
    }


    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPayment_ref() {
        return payment_ref;
    }

    public void setPayment_ref(String payment_ref) {
        this.payment_ref = payment_ref;
    }

    public void setAdditional_data(List<AdditionalData> additional_data) {
        this.additional_data = additional_data;
    }
}
