package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.utils.Step;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.PaymentContext;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.BasketAdyenPaiementFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.BasketPaiementTypeChoiceFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.DeliveryAddressFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.DeliveryModesFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.IViewPagerFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.PaymentSummaryFragment;

import static moshimoshi.cyplay.com.doublenavigation.ui.fragment.BasketPaiementTypeChoiceFragment.PAYMENT_TYPE_LIST;

/**
 * Created by romainlebouc on 25/11/2016.
 */

public enum EPaymentStep implements Step {
    BASKET_INIT(-1, false) {
        @Override
        public boolean isActiveStep(PaymentContext paymentContext) {
            return false;
        }

        @Override
        public IViewPagerFragment getIViewPagerFragment(Context context) {
            return null;
        }
    },
    BASKET_DELIVERY_CHOICE(R.string.basket_step_delivery, false) {
        @Override
        public boolean isActiveStep(PaymentContext paymentContext) {
            return paymentContext.getBasketEPurchaseCollectionModes().contains(EPurchaseCollectionMode.HOME_DELIVERY) ||
                    paymentContext.getBasketEPurchaseCollectionModes().contains(EPurchaseCollectionMode.SHOP_DELIVERY);
        }

        @Override
        public IViewPagerFragment getIViewPagerFragment(Context context) {
            return new DeliveryAddressFragment();
        }
    },
    BASKET_DELIVERY_MODES(R.string.basket_step_delivery_mode, true) {
        @Override
        public boolean isActiveStep(PaymentContext paymentContext) {
            return paymentContext.getBasketEPurchaseCollectionModes() != null && paymentContext.getAvailableDeliveryModes().size() > 0;
        }

        @Override
        public IViewPagerFragment getIViewPagerFragment(Context context) {
            return new DeliveryModesFragment();
        }
    },
    BASKET_PAYMENT_TYPE_CHOICE(R.string.basket_step_payment_choice_type, true) {
        @Override
        public boolean isActiveStep(PaymentContext paymentContext) {
            // TODO : its has to be a list coming from the server configuration
            return PAYMENT_TYPE_LIST.size() > 1;
//            return true;
        }


        @Override
        public IViewPagerFragment getIViewPagerFragment(Context context) {
            return new BasketPaiementTypeChoiceFragment();
        }
    },
    BASKET_PAYMENT(R.string.basket_step_payment, true) {
        @Override
        public boolean isActiveStep(PaymentContext paymentContext) {
            return true;
            //return false;
        }

        @Override
        public IViewPagerFragment getIViewPagerFragment(Context context) {
            return new BasketAdyenPaiementFragment();
        }
    },
    BASKET_RECAP(R.string.basket_step_summary, true) {
        @Override
        public boolean isActiveStep(PaymentContext paymentContext) {
            return true;
        }

        @Override
        public IViewPagerFragment getIViewPagerFragment(Context context) {
            return new PaymentSummaryFragment();
        }
    };

    private final int labelId;

    private boolean displayDeliveryFee;

    EPaymentStep(int labelId, boolean displayDeliveryFee) {
        this.labelId = labelId;
        this.displayDeliveryFee = displayDeliveryFee;
    }

    public int getLabelId() {
        return labelId;
    }

    public int getId() {
        return this.ordinal();
    }

    public boolean isDisplayDeliveryFee() {
        return displayDeliveryFee;
    }

    public abstract boolean isActiveStep(PaymentContext paymentContext);

    public abstract IViewPagerFragment getIViewPagerFragment(Context context);

    public EPaymentStep nextStep(PaymentContext paymentContext) {
        EPaymentStep result = null;
        for (int i = this.ordinal() + 1; i < EPaymentStep.values().length; i++) {
            if (EPaymentStep.values()[i].isActiveStep(paymentContext)) {
                result = EPaymentStep.values()[i];
                break;
            }
        }
        return result;
    }

    public static List<EPaymentStep> getAllSteps(PaymentContext paymentContext) {
        List<EPaymentStep> result = new ArrayList<>();
        for (EPaymentStep ePaymentStep : EPaymentStep.values()) {
            if (ePaymentStep.isActiveStep(paymentContext)) {
                result.add(ePaymentStep);
            }
        }
        return result;
    }


}
