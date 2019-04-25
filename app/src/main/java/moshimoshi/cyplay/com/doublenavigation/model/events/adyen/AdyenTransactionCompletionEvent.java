package moshimoshi.cyplay.com.doublenavigation.model.events.adyen;

import com.adyen.library.TransactionListener;

import java.util.Map;

/**
 * Created by romainlebouc on 08/03/2017.
 */

public class AdyenTransactionCompletionEvent {
    private TransactionListener.CompletedStatus completedStatus;
    final String message;
    Integer integer;
    Map<String, String> map;

    public TransactionListener.CompletedStatus getCompletedStatus() {
        return completedStatus;
    }

    public AdyenTransactionCompletionEvent(TransactionListener.CompletedStatus completedStatus, String s, Integer integer, Map<String, String> map) {
        this.map = map;
        this.completedStatus = completedStatus;
        this.message = s;
        this.integer = integer;
    }

    public String getMessage() {
        return message;
    }
}
