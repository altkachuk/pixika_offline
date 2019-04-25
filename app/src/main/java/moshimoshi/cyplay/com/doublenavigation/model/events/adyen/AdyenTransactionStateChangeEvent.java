package moshimoshi.cyplay.com.doublenavigation.model.events.adyen;

import android.content.Context;

import com.adyen.services.posregister.TenderStates;

import java.util.Map;

import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by romainlebouc on 08/03/2017.
 */

public class AdyenTransactionStateChangeEvent {

    final TenderStates tenderStates;
    final String message;
    final Map<String, String> map;

    public AdyenTransactionStateChangeEvent(TenderStates tenderStates, String message, Map<String, String> map) {
        this.tenderStates = tenderStates;
        this.message = message;
        this.map = map;
    }

    public String getMessage() {
        return message;
    }

    public TenderStates getTenderStates() {
        return tenderStates;
    }

    public String getTenderStatesLabel(Context context) {
        switch (this.tenderStates) {
            case INITIAL:
                return context.getString(R.string.transaction_state_initial);
            case TENDER_CREATED:
                return context.getString(R.string.transaction_state_initial_tender_created);
            case CARD_INSERTED:
                return context.getString(R.string.transaction_state_card_inserted);
            case CARD_SWIPED:
                return context.getString(R.string.transaction_state_card_swiped);
            case WAIT_FOR_APP_SELECTION:
                return context.getString(R.string.transaction_state_wait_for_app_selection);
            case APPLICATION_SELECTED:
                return context.getString(R.string.transaction_state_application_selected);
            case WAIT_FOR_AMOUNT_ADJUSTMENT:
                return context.getString(R.string.transaction_state_wait_for_amount_adjustment);
            case ASK_GRATUITY:
                return context.getString(R.string.transaction_state_ask_gratuity);
            case GRATUITY_ENTERED:
                return context.getString(R.string.transaction_state_gratuity_entered);
            case ASK_DCC:
                return context.getString(R.string.transaction_state_ask_dcc);
            case DCC_ACCEPTED:
                return context.getString(R.string.transaction_state_dcc_accepted);
            case DCC_REJECTED:
                return context.getString(R.string.transaction_state_dcc_rejected);
            case PROCESSING_TENDER:
                return TenderStates.PROCESSING_TENDER.name();
            case WAIT_FOR_PIN:
                return context.getString(R.string.transaction_state_wait_for_pin);
            case PIN_DIGIT_ENTERED:
                return context.getString(R.string.transaction_state_pin_digit_entered);
            case PIN_ENTERED:
                return context.getString(R.string.transaction_state_pin_entered);
            case PROVIDE_CARD_DETAILS:
                return context.getString(R.string.transaction_state_provide_card_details);
            case CARD_DETAILS_PROVIDED:
                return context.getString(R.string.transaction_state_card_details_provided);
            case PRINT_RECEIPT:
                return context.getString(R.string.transaction_state_print_receipt);
            case RECEIPT_PRINTED:
                return context.getString(R.string.transaction_state_receipt_printed);
            case REFERRAL:
                return context.getString(R.string.transaction_state_referral);
            case REFERRAL_CHECKED:
                return context.getString(R.string.transaction_state_referral_checked);
            case CHECK_SIGNATURE:
                return context.getString(R.string.transaction_state_check_signature);
            case SIGNATURE_CHECKED:
                return context.getString(R.string.transaction_state_signature_checked);
            case ADDITIONAL_DATA_AVAILABLE:
                return context.getString(R.string.transaction_state_additional_data_available);
            case ERROR:
                return context.getString(R.string.transaction_state_error);
            case APPROVED:
                return context.getString(R.string.transaction_state_approved);
            case DECLINED:
                return context.getString(R.string.transaction_state_declined);
            case CANCELLED:
                return context.getString(R.string.transaction_state_cancelled);
            case ACKNOWLEDGED:
                return context.getString(R.string.transaction_state_acknowledge);
            case BEEP_ALERT:
                return TenderStates.BEEP_ALERT.name();
            case BEEP_OK:
                return TenderStates.BEEP_OK.name();
            case CONTACTLES_CARD_UNSUPPORTED:
                return TenderStates.CONTACTLES_CARD_UNSUPPORTED.name();
            case SWIPE_CARD:
                return TenderStates.SWIPE_CARD.name();
            default:
                return "";
        }
    }


}
