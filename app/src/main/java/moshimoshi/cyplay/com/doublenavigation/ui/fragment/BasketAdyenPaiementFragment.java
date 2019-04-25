package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adyen.library.TransactionListener;
import com.adyen.library.exceptions.NotYetRegisteredException;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPaymentStatus;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPaymentStep;
import moshimoshi.cyplay.com.doublenavigation.model.events.PaymentStatusUpdatedEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.PaymentStepEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.adyen.AdyenTransactionCompletionEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.adyen.AdyenTransactionProgressEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.adyen.AdyenTransactionStateChangeEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.PaymentContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.AdyenPosPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.AdyenTerminalConfigurationActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.PaymentSummaryLayout;
import moshimoshi.cyplay.com.doublenavigation.utils.CompatUtils;
import moshimoshi.cyplay.com.doublenavigation.view.AdyenPosView;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;

import static moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants.EXTRA_CHOOSE_MODE;
import static moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants.REQUEST_POS_SELECTION;

/**
 * Created by romainlebouc on 24/11/2016.
 */

public class BasketAdyenPaiementFragment extends BaseFragment implements IViewPagerFragment, AdyenPosView {

    @Inject
    PaymentContext paymentContext;

    @Inject
    AdyenPosPresenter adyenPosPresenter;

    @BindView(R.id.basket_payment_pos_status)
    TextView transactionStatus;

    @BindView(R.id.payment_summary)
    PaymentSummaryLayout paymentSummary;

    @BindView(R.id.pos_status_container)
    View POSStatusContainer;

    @BindView(R.id.pos_status)
    ImageView POSstatus;

    @BindView(R.id.pos_name)
    TextView POSName;

    @BindView(R.id.loadfailed_view)
    View loadedFailed;

    boolean noTerminalChosen = false;

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!noTerminalChosen) {
            adyenPosPresenter.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        adyenPosPresenter.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adyenPosPresenter.getAdyenLibraryInterface() != null) {
            adyenPosPresenter.getAdyenLibraryInterface().unregisterTerminalConnectionListener();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_basket_adyen_paiement, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadedFailed.setVisibility(View.GONE);
        adyenPosPresenter.setView(this);
        try {
            adyenPosPresenter.init();
        } catch (NotYetRegisteredException e) {
            Log.e(BasketAdyenPaiementFragment.class.getName(), e.getMessage(), e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_POS_SELECTION) {
            // Make sure the request was successful
            if (resultCode != IntentConstants.RESULT_POS_SELECTION_OK) {
                noTerminalChosen = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
                builder.setTitle(R.string.error);
                builder.setMessage(R.string.no_default_pos_chosen_warning);
                builder.setCancelable(false);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        BasketAdyenPaiementFragment.this.getActivity().supportFinishAfterTransition();
                    }
                });
                builder.create().show();
            } else {
                if (!noTerminalChosen) {
                    adyenPosPresenter.startPaiementProcess();
                }
            }
        }
    }


    @Override
    public void onTransactionStart() {

    }

    @Override
    public void onTransactionStop() {

    }

    @Override
    public void onFragmentSelected(boolean displayDeliveryFee) {
        paymentSummary.setItem(paymentContext.getPayment(), true);
        if (!noTerminalChosen) {
            adyenPosPresenter.startPaiementProcess();
        }
    }

    /***
     * Action to perform when a default POS is not associated to the Android device
     */
    @Override
    public void chooseDefaultPos(boolean explanation) {
        if (explanation) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
            builder.setTitle(R.string.no_default_pos_warning);
            builder.setCancelable(false);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startChoosePOSActivity();
                }
            });
            builder.create().show();
        } else {
            startChoosePOSActivity();
        }
    }


    @Override
    public void onAppNotRegistred() {
        //TODO ??
    }

    @Override
    public void onInvalidTransactionRequest() {
        //TODO
    }

    @Override
    public void onAlreadyProcessingTransaction() {
        //TODO
    }

    @Override
    public void onCustomerPaymentTransactionCreationFailed() {
        loadedFailed.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTransactionError() {
        //TODO
    }

    @Override
    public void setPosStatus(final String message, final int colorId) {
        try {
            this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    POSStatusContainer.setVisibility(View.VISIBLE);
                    CompatUtils.setDrawableTint(POSstatus.getDrawable(), ContextCompat.getColor(BasketAdyenPaiementFragment.this.getActivity(), colorId));
                    POSName.setText(message);
                }
            });
        } catch (Exception e) {
            Log.e(BasketAdyenPaiementFragment.class.getName(), e.getMessage() != null ? e.getMessage() : e.toString(), e);
        }
    }


    @Override
    protected boolean isBusActivated() {
        return true;
    }

    @Override
    public void setTransactionStatus(String message) {
        transactionStatus.setText(message);
    }

    @Override
    public void stopPaymentProcessus(EPaymentStatus ePaymentStatus) {
        if (paymentContext.getPayment() != null) {
            bus.post(new PaymentStatusUpdatedEvent(paymentContext.getPayment().getId(),
                    ePaymentStatus));
        }
    }

    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {
        //TODO
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void startChoosePOSActivity() {
        Intent intent = new Intent(BasketAdyenPaiementFragment.this.getContext(), AdyenTerminalConfigurationActivity.class);
        intent.putExtra(EXTRA_CHOOSE_MODE, true);
        BasketAdyenPaiementFragment.this.startActivityForResult(intent, REQUEST_POS_SELECTION);
        BasketAdyenPaiementFragment.this.getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.no_anim);
    }

    private void onAdyenTransactionNotApproved(TransactionListener.CompletedStatus completedStatus) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BasketAdyenPaiementFragment.this.getActivity());
        switch (completedStatus) {
            case DECLINED:
                builder.setTitle(R.string.transaction_declined);
                break;
            case CANCELLED:
                builder.setTitle(R.string.transaction_cancelled);
                break;
            case ERROR:
                builder.setTitle(R.string.transaction_error);
                break;
            default:
                return;
        }
        builder.setCancelable(false);
        builder.setNegativeButton(R.string.retry, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                transactionStatus.setText(R.string.starting_transaction);
                adyenPosPresenter.pay(paymentContext.getPayment(), paymentContext.getCurrentPaymentTransaction());
            }
        });
        builder.setPositiveButton(R.string.leave, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adyenPosPresenter.onTransactionCancelled();
            }
        });
        builder.create().show();
    }


    // -------------------------------------------------------------------------------------------
    //                                      Events(s)
    // -------------------------------------------------------------------------------------------

    @Subscribe
    public void onAdyenTransactionProgressEvent(final AdyenTransactionProgressEvent event) {
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                transactionStatus.setText(event.getMessage());
            }
        });
    }


    @Subscribe
    public void onAdyenTransactionStateChangeEvent(final AdyenTransactionStateChangeEvent event) {
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                transactionStatus.setText(event.getTenderStatesLabel(BasketAdyenPaiementFragment.this.getContext()));
            }
        });

    }

    @Subscribe
    public void onAdyenTransactionCompletionEvent(AdyenTransactionCompletionEvent event) {
        switch (event.getCompletedStatus()) {
            case APPROVED:
                BasketAdyenPaiementFragment.this.bus.post(new PaymentStepEvent(EPaymentStep.BASKET_PAYMENT));
                break;
            case DECLINED:
            case CANCELLED:
            case ERROR:
                onAdyenTransactionNotApproved(event.getCompletedStatus());
                break;
            default:

        }
    }

    // -------------------------------------------------------------------------------------------
    //                                      Clicks
    // -------------------------------------------------------------------------------------------


    @OnClick(R.id.pos_link)
    public void onChosePOS() {
        adyenPosPresenter.choosePOS();
    }


    @OnClick(R.id.reload_button)
    public void onReloadTransaction() {
        adyenPosPresenter.initTransaction();
    }


}
