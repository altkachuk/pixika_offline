package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Payment;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerPaymentStatus;
import moshimoshi.cyplay.com.doublenavigation.model.business.DeliveryMode;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPaymentStep;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EDeliveryModeStatus;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPaymentStatus;
import moshimoshi.cyplay.com.doublenavigation.model.business.utils.Step;
import moshimoshi.cyplay.com.doublenavigation.model.events.PaymentStepEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.PaymentStatusUpdatedEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.PaymentContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.BasketPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetDeliveryModesPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.PaymentPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.PaymentTransactionPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.ViewPagerAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.BasketViewPager;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.StepsProgressLayout;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.IViewPagerFragment;
import moshimoshi.cyplay.com.doublenavigation.utils.ActivityHelper;
import moshimoshi.cyplay.com.doublenavigation.view.PaymentView;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;

/**
 * Created by romainlebouc on 02/03/2017.
 */

public class PaymentDeliveryStepsActivity extends BaseActivity {

    @Inject
    PaymentContext paymentContext;

    @Inject
    CustomerContext customerContext;

    @Inject
    SellerContext sellerContext;

    @Inject
    BasketPresenter basketPresenter;

    @Inject
    PaymentPresenter paymentPresenter;

    @Inject
    PaymentTransactionPresenter paymentTransactionPresenter;

    @Inject
    GetDeliveryModesPresenter getDeliveryModesPresenter;

    @BindView(R.id.tunnel_payment_creation)
    View tunnelPayementCreationWaiting;

    @BindView(R.id.basket_steps_view_pager)
    BasketViewPager basketStepsViewPager;

    @BindView(R.id.payment_steps_progress)
    StepsProgressLayout paymentStepsProgress;

    @BindView(R.id.default_loadfailed_view)
    View loadedFailed;

    private List<EPaymentStep> ePaymentSteps;
    private EPaymentStep ePaymentStep = EPaymentStep.BASKET_PAYMENT_TYPE_CHOICE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_delivery_steps);
        loadedFailed.setVisibility(View.GONE);
        initPresenters();

        // Screen stay on if we are making a payment
        if (configHelper.getPaymentConfig().getDisplay().getKeep_screen_on()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

    }

    protected boolean isLeftCrossClosable() {
        return true;
    }

    private void initPresenters() {
        paymentPresenter.setView(new PaymentView() {
            @Override
            public void onResourceViewResponse(ResourceResponseEvent<Payment> resourceResponseEvent) {
                // Failure or success of the update : we finish the activity
                EPaymentStatus ePaymentStatus = paymentContext.getPayment().getEPaymentStatus();
                switch (ePaymentStatus) {
                    case PAYMENT_CANCELED:
                    case PAYMENT_FAILED:
                        finishPayment(false);
                        break;
                }
            }
        });
        paymentContext.setBasket(customerContext.getBasket());
        paymentContext.setCustomerId(customerContext.getCustomerId());
        tunnelPayementCreationWaiting.setVisibility(View.VISIBLE);
        getDeliveryModesPresenter.setView(new ResourceView<List<DeliveryMode>>() {
            @Override
            public void onResourceViewResponse(ResourceResponseEvent<List<DeliveryMode>> resourceResponseEvent) {
                tunnelPayementCreationWaiting.setVisibility(View.GONE);
                if (resourceResponseEvent.getResourceException() == null) {
                    paymentContext.setAvailableDeliveryModes(resourceResponseEvent.getResource());
                    ePaymentSteps = EPaymentStep.getAllSteps(paymentContext);
                    paymentStepsProgress.setSteps((List<Step>) (List<?>) ePaymentSteps);
                    paymentStepsProgress.bringToFront();
                    initViewPager();
                } else {
                    loadedFailed.setVisibility(View.VISIBLE);
                }
            }
        });
        getDeliveryModesPresenter.getDeliveryModes(EDeliveryModeStatus.ACTIVE.getStatus());
    }

    private void initViewPager() {
        // Create adapter
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        for (EPaymentStep ePaymentStep : ePaymentSteps) {
            adapter.addFragment((Fragment) ePaymentStep.getIViewPagerFragment(this), this.getString(ePaymentStep.getLabelId()));
        }
        // fillStocks viewPager
        basketStepsViewPager.setAdapter(adapter);
        basketStepsViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            boolean[] scrolled = new boolean[ePaymentSteps.size()];

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (!scrolled[position] && adapter.getItem(position) instanceof IViewPagerFragment) {
                    ((IViewPagerFragment) adapter.getItem(position)).onFragmentSelected(ePaymentSteps.get(position).isDisplayDeliveryFee());
                    paymentStepsProgress.scroll(position, positionOffset);
                    scrolled[position] = true;
                }

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        //Disable swipe
        basketStepsViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        PaymentDeliveryStepsActivity.this.bus.post(new PaymentStepEvent(EPaymentStep.BASKET_INIT));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                stopPayement();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        stopPayement();
    }

    private void stopPayement(){
        // If a transaction is in progress, we disable back button
        if (!paymentContext.isTransactionInProgress()) {
            warnPaymentStop();
        } else {
            warnTransactionStop();
        }
    }
    private void warnPaymentStop() {
        WarnPaymentListener warnPaymentListener = new WarnPaymentListener() {
            @Override
            public void onExecute() {
                finishPayment(false);
            }
        };
        warnPaymentCancellation(warnPaymentListener);
    }

    private void warnTransactionStop() {
        WarnPaymentListener warnPaymentListener = new WarnPaymentListener() {
            @Override
            public void onExecute() {
                finishPayment(false);
            }
        };
        warnTransactionCancellation(warnPaymentListener);
    }

    public interface WarnPaymentListener {
        void onExecute();
    }

    private void warnPaymentCancellation(final WarnPaymentListener warnPaymentListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.cancel_payment_warning);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (paymentContext.getPayment() != null) {
                    bus.post(new PaymentStatusUpdatedEvent(paymentContext.getPayment().getId(), EPaymentStatus.PAYMENT_CANCELED));
                }
                warnPaymentListener.onExecute();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();

    }


    private void warnTransactionCancellation(final WarnPaymentListener warnPaymentListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.warning);
        builder.setMessage(R.string.cancel_transaction_warning);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (paymentContext.getPayment() != null) {
                    bus.post(new PaymentStatusUpdatedEvent(paymentContext.getPayment().getId(), EPaymentStatus.PAYMENT_CANCELED));
                }
                warnPaymentListener.onExecute();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    private void finishPayment(boolean clearCustomerContext) {
        if (clearCustomerContext) {
            customerContext.clearContext();
            paymentContext.clear();
            ActivityHelper.goToSellerDashboard();
        } else {
            paymentContext.clear();
            basketPresenter.getBasket();
        }

        supportFinishAfterTransition();
        overridePendingTransition(R.anim.no_anim, R.anim.slide_out_bottom);
    }

    @Subscribe
    public void onPaymentStatusUpdateEvent(PaymentStatusUpdatedEvent paymentStatusUpdatedEvent) {
        switch (paymentStatusUpdatedEvent.getePaymentStatus()) {
            case PAYMENT_CANCELED:
                if (paymentContext.getPayment() != null) {
                    paymentContext.getPayment().setEPaymentStatus(EPaymentStatus.PAYMENT_CANCELED);
                    paymentPresenter.updateCustomerPayment(paymentContext.getCustomerId(),
                            paymentContext.getPayment().getId(),
                            new CustomerPaymentStatus(paymentContext.getPayment()));
                    break;
                }
        }
    }

    @Subscribe
    public void onBasketStepEvent(PaymentStepEvent paymentStepEvent) {
        this.ePaymentStep = paymentStepEvent.getStep().nextStep(paymentContext);
        if (this.ePaymentStep == null) {
            finishPayment(paymentStepEvent.isLeaveCustomerContext());
        } else {
            basketStepsViewPager.setCurrentItem(ePaymentSteps.indexOf(this.ePaymentStep), true);
            paymentStepsProgress.setCurrentStep(this.ePaymentStep);
        }

    }

}
