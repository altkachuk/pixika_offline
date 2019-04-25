package moshimoshi.cyplay.com.doublenavigation.model.events.adyen;

import com.adyen.library.TransactionListener;

/**
 * Created by romainlebouc on 08/03/2017.
 */

public class AdyenTransactionProgressEvent {
    final TransactionListener.ProcessStatus processStatus;
    final String message;

    public AdyenTransactionProgressEvent(TransactionListener.ProcessStatus processStatus, String message) {
        this.processStatus = processStatus;
        this.message = message;
    }

    public TransactionListener.ProcessStatus getProcessStatus() {
        return processStatus;
    }

    public String getMessage() {
        return message;
    }
}