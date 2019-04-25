package moshimoshi.cyplay.com.doublenavigation.view;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPaymentStatus;

/**
 * Created by romainlebouc on 09/03/2017.
 */

public interface AdyenPosView extends BaseView {

    void chooseDefaultPos(boolean explanation);

    void onAppNotRegistred();

    void onInvalidTransactionRequest();

    void onAlreadyProcessingTransaction();

    void onCustomerPaymentTransactionCreationFailed();

    void onTransactionStart();

    void onTransactionStop();

    void onTransactionError();

    void setPosStatus(String message, int color);

    void setTransactionStatus(String message);

    void stopPaymentProcessus(EPaymentStatus ePaymentStatus);


}
