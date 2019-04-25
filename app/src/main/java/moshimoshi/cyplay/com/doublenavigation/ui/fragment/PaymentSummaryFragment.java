package moshimoshi.cyplay.com.doublenavigation.ui.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Payment;
import moshimoshi.cyplay.com.doublenavigation.model.business.PaymentShare;
import moshimoshi.cyplay.com.doublenavigation.model.business.ShareChannel;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPaymentShareMode;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPaymentStatus;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPaymentStep;
import moshimoshi.cyplay.com.doublenavigation.model.events.PaymentStepEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.PaymentContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.BasketPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.PaymentPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.PaymentSharePresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.DeliveryItemExpandableAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.animation.AnimationUtils;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.LoadingView;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.PaymentSummaryLayout;
import moshimoshi.cyplay.com.doublenavigation.utils.ActivityHelper;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.utils.CompatUtils;

import static moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants.EXTRA_CUSTOMER_PAYMENT;


/**
 * Created by romainlebouc on 02/03/2017.
 */

public class PaymentSummaryFragment extends BaseFragment implements IViewPagerFragment, CompoundButton.OnCheckedChangeListener {

    @Inject
    CustomerContext customerContext;

    @Inject
    SellerContext sellerContext;

    @Inject
    PaymentContext paymentContext;

    @Inject
    BasketPresenter basketPresenter;

    @Inject
    PaymentSharePresenter paymentSharePresenter;

    @Inject
    PaymentPresenter paymentPresenter;

    @BindView(R.id.loading_view)
    LoadingView loadingView;

    @BindView(R.id.payment_summary)
    PaymentSummaryLayout paymentSummary;

    @BindView(R.id.payment_transactions)
    RecyclerView paymentRecyclerView;

    @BindView(R.id.shop_toggle_button)
    SwitchCompat shopToggleBtn;

    @BindView(R.id.customer_email_toggle_button)
    SwitchCompat emailToggleBtn;

    @BindView(R.id.customer_email)
    TextView customerEmail;

    @BindView(R.id.email_input)
    EditText emailInput;

    @BindView(R.id.paiment_summary_ok)
    TextView okBtn;

    @BindView(R.id.payment_summary_share_container)
    View shareContainer;

    @BindView(R.id.payment_status_container)
    View paymentStatusContainer;

    @BindView(R.id.payment_status)
    TextView paymentStatus;

    @BindView(R.id.loading_message)
    TextView loadingMessage;

    @BindView(R.id.payment_summary_loading_view)
    View summaryLoadingView;

    private DeliveryItemExpandableAdapter adapter;

    private Payment payment;

    private AlertDialog errorDialog;
    private AlertDialog contextClientDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_paiement_summary, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initPaymentSharePresenter();
        initPaymentPresenter();

        initRecyclerView();
        initDialogs();
        initBtns();

        if (isSummaryHistory()) {
            onFragmentSelected(true);
        }
    }

    private void initBtns() {
        shopToggleBtn.setOnCheckedChangeListener(this);
        emailToggleBtn.setOnCheckedChangeListener(this);
        emailToggleBtn.setChecked(true);
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkReceiptSending();
            }
        });
        emailInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    ActivityHelper.hideKeyboard(emailInput, PaymentSummaryFragment.this.getActivity());
                }
                return false;
            }
        });

    }


    private void initRecyclerView() {
        // Setup expandable feature and RecyclerView
        RecyclerViewExpandableItemManager expMgr = new RecyclerViewExpandableItemManager(null);
        paymentRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        expMgr.setDefaultGroupsExpandedState(true);

        adapter = new DeliveryItemExpandableAdapter();
        paymentRecyclerView.setAdapter(expMgr.createWrappedAdapter(adapter));

        // NOTE: need to disable change animations to ripple effect work properly
        ((SimpleItemAnimator) paymentRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        expMgr.attachRecyclerView(paymentRecyclerView);
    }

    private void initDialogs() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_NEUTRAL:
                        errorDialog.dismiss();
                        break;
                    case DialogInterface.BUTTON_POSITIVE:
                        //No button clicked
                        //cancel
                        errorDialog.dismiss();
                        basketPresenter.getBasket();
                        bus.post(new PaymentStepEvent(EPaymentStep.BASKET_RECAP));
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        //Yes button clicked
                        //retry
                        errorDialog.dismiss();
                        createPaymentShare();
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        errorDialog = builder
                .setTitle(R.string.warning)
                .setMessage(R.string.create_payment_share_error)
                .setPositiveButton(android.R.string.ok, dialogClickListener)
                .setNegativeButton(R.string.retry, dialogClickListener)
                .setNeutralButton(R.string.modify_sending_parameters, dialogClickListener)
                .create();
    }


    private void initPaymentSharePresenter() {
        paymentSharePresenter.setView(new ResourceView<PaymentShare>() {
            @Override
            public void onResourceViewResponse(ResourceResponseEvent<PaymentShare> resourceResponseEvent) {
                loadingView.setLoadingState(LoadingView.LoadingState.LOADED);
                if (resourceResponseEvent.getResourceException() == null) {

                    if (isSummaryHistory()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentSummaryFragment.this.getContext());
                        builder.setTitle(R.string.success);
                        builder.setMessage(R.string.create_payment_share_success);
                        builder.setCancelable(false);
                        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                contextClientDialog.dismiss();
                            }
                        });
                        contextClientDialog = builder.create();
                        contextClientDialog.show();
                    } else {
                        showSuccessDialog();
                    }
                } else {
                    errorDialog.show();
                }
            }
        });
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentSummaryFragment.this.getContext());
        builder.setTitle(R.string.success);
        builder.setMessage(R.string.stay_or_leave_customer_context_dialog);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                contextClientDialog.dismiss();
                PaymentSummaryFragment.this.bus.post(new PaymentStepEvent(EPaymentStep.BASKET_RECAP, false));
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PaymentSummaryFragment.this.bus.post(new PaymentStepEvent(EPaymentStep.BASKET_RECAP, true));
            }
        });

        contextClientDialog = builder.create();
        contextClientDialog.show();
    }

    private void initPaymentPresenter() {
        paymentPresenter.setView(new ResourceView<Payment>() {
            @Override
            public void onResourceViewResponse(ResourceResponseEvent<Payment> resourceResponseEvent) {
                if (resourceResponseEvent.getResourceException() == null) {
                    loadingView.setLoadingState(LoadingView.LoadingState.LOADED);
                    payment = resourceResponseEvent.getResource();
                    updateInfos();
                } else {
                    loadingView.setLoadingState(LoadingView.LoadingState.FAILED);
                }
            }
        });
    }


    @OnClick(R.id.paiment_summary_ok)
    public void onClickPaiementOK() {
        createPaymentShare();
    }

    private void createPaymentShare() {
        if (emailInput.isFocused()) {
            ActivityHelper.hideKeyboard(emailInput, PaymentSummaryFragment.this.getActivity());
        }
        PaymentShare paymentShare = new PaymentShare();
        paymentShare.setCustomer_id(customerContext.getCustomerId());
        paymentShare.setPayment_id(payment.getId());
        paymentShare.setSeller_id(sellerContext.getSeller().getId());
        paymentShare.setShop_id(payment.getShop_id());
        paymentShare.setLang(CompatUtils.toLanguageTag(Locale.getDefault()));
        paymentShare.setSharing_channels(getSharingChannel());
        setLoadingState(this.getString(R.string.sending_in_progress), ContextCompat.getColor(this.getActivity(), R.color.window_black_translucent));

        paymentSharePresenter.createPaymentShare(paymentShare);
    }

    private List<ShareChannel> getSharingChannel() {
        List<ShareChannel> shareChannels = new ArrayList<>();
        if (shopToggleBtn.isChecked()) {
            ShareChannel shopChannel = new ShareChannel();
            shopChannel.setPaymentShareMode(EPaymentShareMode.PAYMENT_SHARE_SHOP_EMAIL.getTag());
            shareChannels.add(shopChannel);
        }

        if (emailToggleBtn.isChecked()) {
            ShareChannel emailChannel = new ShareChannel();
            emailChannel.setPaymentShareMode(EPaymentShareMode.PAYMENT_SHARE_CUSTOMER_EMAIL.getTag());
            emailChannel.setPaymentShareReference(emailInput.getText().toString());
            shareChannels.add(emailChannel);
        }

        return shareChannels;
    }

    @Override
    public void onFragmentSelected(boolean displayDeliveryFee) {
        Bundle bundle = getArguments();
        if (isSummaryHistory()) {
            payment = Parcels.unwrap(bundle.getParcelable(EXTRA_CUSTOMER_PAYMENT));
            loadingView.setLoadingState(LoadingView.LoadingState.LOADING);
            setLoadingState("", ContextCompat.getColor(this.getActivity(), android.R.color.transparent));
            paymentPresenter.getCustomerPayment(customerContext.getCustomer().getId(),
                    payment.getId());
        } else {
            payment = paymentContext.getPayment();
            updateInfos();
        }

    }


    private boolean isSummaryHistory() {
        Bundle bundle = getArguments();
        return bundle != null && bundle.containsKey(EXTRA_CUSTOMER_PAYMENT);
    }

    private void updateInfos() {
        emailInput.setText(customerContext.getCustomer().getDetails().getEmail().getFirstEmail());
        checkReceiptSending();
        if (payment != null) {
            adapter.setPaymentDeliveryItems(payment.getDelivery_items());
            paymentSummary.setItem(payment, true);
        }

        if (!isSummaryHistory() || payment.getEPaymentStatus().isReceiptSharable()) {
            shareContainer.setVisibility(View.VISIBLE);
        } else {
            shareContainer.setVisibility(View.GONE);
        }

        if (isSummaryHistory()) {
            paymentStatusContainer.setBackgroundColor(ContextCompat.getColor(this.getContext(), payment.getEPaymentStatus().getColorId()));
            paymentStatus.setText(payment.getEPaymentStatus().getLabelId());
        } else {
            paymentStatusContainer.setBackgroundColor(ContextCompat.getColor(this.getContext(), EPaymentStatus.PAYMENT_PAID.getColorId()));
            paymentStatus.setText(EPaymentStatus.PAYMENT_PAID.getLabelId());
            Animation a = AnimationUtils.buildCollapseAnimation(paymentStatusContainer, 6000, 500);
            paymentStatusContainer.startAnimation(a);
        }
    }

    @OnClick(R.id.customer_email)
    public void editEmail() {
        okBtn.setEnabled(false);
        emailInput.setVisibility(View.VISIBLE);
        emailInput.setFocusable(true);
        emailInput.requestFocus();
        ActivityHelper.showKeyboard(emailInput, getActivity());
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        checkReceiptSending();
    }

    public void checkReceiptSending() {
        okBtn.setEnabled(shopToggleBtn.isChecked() ||
                (emailToggleBtn.isChecked() && Patterns.EMAIL_ADDRESS.matcher(emailInput.getText()).matches()));
    }


    private void setLoadingState(String text, int color) {
        loadingMessage.setText(text);
        summaryLoadingView.setBackgroundColor(color);
        this.loadingView.setLoadingState(LoadingView.LoadingState.LOADING);
    }

}
