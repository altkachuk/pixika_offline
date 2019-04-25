package moshimoshi.cyplay.com.doublenavigation.model.singleton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import moshimoshi.cyplay.com.doublenavigation.model.business.Payment;
import moshimoshi.cyplay.com.doublenavigation.model.business.Basket;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerPaymentTransaction;
import moshimoshi.cyplay.com.doublenavigation.model.business.DeliveryMode;
import moshimoshi.cyplay.com.doublenavigation.model.business.Address;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPurchaseCollectionMode;

/**
 * Created by romainlebouc on 06/12/2016.
 */

public class PaymentContext {

    Basket basket;
    String customerId;
    Payment payment;
    CustomerPaymentTransaction currentPaymentTransaction;

    // Available delivery modes
    List<DeliveryMode> availableDeliveryModes;

    Address mainShippingAddress;
    Address mainBillingAddress;

    boolean transactionInProgress = false;

    public String getCustomerId() {
        return customerId;
    }

    public Payment getPayment() {
        return payment;
    }

    public Set<EPurchaseCollectionMode> getBasketEPurchaseCollectionModes() {
        if (basket != null) {
            return basket.getBasketEPurchaseCollectionModes();
        } else {
            return new HashSet<>();
        }
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void setCurrentPaymentTransaction(CustomerPaymentTransaction currentTransaction) {
        this.currentPaymentTransaction = currentTransaction;
    }

    public CustomerPaymentTransaction getCurrentPaymentTransaction() {
        return currentPaymentTransaction;
    }

    public void setCurrentTransactionId(String currentTransactionId) {
        if (this.currentPaymentTransaction != null) {
            this.currentPaymentTransaction.setId(currentTransactionId);
        }
    }

    public String getCurrentTransactionId() {
        String result = null;
        if (this.currentPaymentTransaction != null) {
            result = currentPaymentTransaction.getId();
        }
        return result;
    }

    public List<DeliveryMode> getAvailableDeliveryModes() {
        return availableDeliveryModes != null ? availableDeliveryModes : new ArrayList<DeliveryMode>();
    }

    public void setAvailableDeliveryModes(List<DeliveryMode> deliveryModes) {
        this.availableDeliveryModes = deliveryModes;
    }

    public void clear() {
        basket = null;
        customerId = null;
        payment = null;
        currentPaymentTransaction = null;
        availableDeliveryModes = null;
        mainShippingAddress = null;
        mainBillingAddress = null;
        transactionInProgress = false;
    }

    public Address getMainShippingAddress() {
        return mainShippingAddress;
    }

    public void setMainShippingAddress(Address mainShippingAddress) {
        this.mainShippingAddress = mainShippingAddress;
    }

    public Address getMainBillingAddress() {
        return mainBillingAddress;
    }

    public void setMainBillingAddress(Address mainBillingAddress) {
        this.mainBillingAddress = mainBillingAddress;
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public boolean isTransactionInProgress() {
        return transactionInProgress;
    }

    public void setTransactionInProgress(boolean transactionInProgress) {
        this.transactionInProgress = transactionInProgress;
    }
}
