package moshimoshi.cyplay.com.doublenavigation.model.events;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPaymentStatus;

/**
 * Created by romainlebouc on 13/12/2016.
 */

public class PaymentStatusUpdatedEvent {
    private final EPaymentStatus ePaymentStatus;
    private final String id;

    public PaymentStatusUpdatedEvent(String id, EPaymentStatus ePaymentStatus) {
        this.id = id;
        this.ePaymentStatus = ePaymentStatus;
    }

    public EPaymentStatus getePaymentStatus() {
        return ePaymentStatus;
    }
}
