package moshimoshi.cyplay.com.doublenavigation.model.events;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPaymentStep;

/**
 * Created by romainlebouc on 25/11/2016.
 */

public class PaymentStepEvent {

    private final EPaymentStep step;
    private final boolean leaveCustomerContext;

    public PaymentStepEvent(EPaymentStep step) {
        this.step = step;
        this.leaveCustomerContext= false;
    }

    public PaymentStepEvent(EPaymentStep step, boolean leaveCustomerContext) {
        this.step = step;
        this.leaveCustomerContext= leaveCustomerContext;
    }

    public EPaymentStep getStep() {
        return step;
    }

    public boolean isLeaveCustomerContext() {
        return leaveCustomerContext;
    }
}
