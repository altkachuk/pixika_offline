package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductShare;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.ESendingSate;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.ProductSharePresenter;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;

/**
 * Created by romainlebouc on 23/08/16.
 */
public class WishListShareDialog extends AlertDialog {

    @Inject
    ConfigHelper configHelper;

    public interface OnConfirmWishListSharingListener {
        void onConfirmWishListSharing(String email);

        void onWishListSharingFinished(boolean success);
    }

    private OnConfirmWishListSharingListener onConfirmWishListSharingListener;
    private List<RadioButton> radioButtons = new ArrayList<>();
    private RadioButton personalEmailButton;
    private RadioButton professionalEmailButton;
    private RadioButton freeEmailButton;
    private EditText freeEmail;
    private TextView emailError;
    private CustomerContext customerContext;

    private ProgressBar wishlistSendingInProgress;

    private View contentView;
    private TextView sendingStatus;
    private View toSendView;

    private final View.OnClickListener SEND_EMAIL_LISTENER = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            String email = getChosenEmail();
            boolean isEmailAddress = Patterns.EMAIL_ADDRESS.matcher(email).matches();
            if (isEmailAddress) {
                applyState(ESendingSate.SENDING);
                onConfirmWishListSharingListener.onConfirmWishListSharing(email);
                emailError.setVisibility(View.GONE);

            } else {
                emailError.setVisibility(View.VISIBLE);
            }
        }
    };

    private final OnClickListener EMPTY_ON_CLICK_LISTENER = new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            onConfirmWishListSharingListener.onWishListSharingFinished(false);
        }
    };

    public WishListShareDialog(Context context, CustomerContext customerContext, OnConfirmWishListSharingListener onConfirmWishListSharingListener) {
        super(context, R.style.AppCompatAlertDialogStyle);
        ((PlayRetailApp) context.getApplicationContext()).inject(this);
        this.onConfirmWishListSharingListener = onConfirmWishListSharingListener;
        init(customerContext);
    }

    public void setSuccess() {
        applyState(ESendingSate.SENT_SUCCESS);
    }

    public void setFailure() {
        applyState(ESendingSate.SENT_FAILURE);

    }

    private void init(CustomerContext customerContext) {
        this.customerContext = customerContext;
        this.setCancelable(false);

        contentView = this.getLayoutInflater().inflate(R.layout.dialog_wishlist_email, null);
        emailError = (TextView) contentView.findViewById(R.id.email_error);
        freeEmail = (EditText) contentView.findViewById(R.id.free_email);
        wishlistSendingInProgress = (ProgressBar) contentView.findViewById(R.id.wishlist_sending_in_progress);
        sendingStatus = (TextView) contentView.findViewById(R.id.sending_status);
        toSendView = contentView.findViewById(R.id.to_send_container);

        initRadioButtons(contentView);
        initViews();
        initListeners();
        applyState(ESendingSate.TO_SEND);
        this.setView(contentView);
    }

    private void initListeners() {
        this.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = WishListShareDialog.this.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(SEND_EMAIL_LISTENER);
            }
        });
        freeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseRadioButton(freeEmailButton);
            }
        });
        freeEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                chooseRadioButton(freeEmailButton);
            }
        });
    }

    private void initViews() {
        String personalEmail = customerContext.getPersonalEmail();
        String professionalEmail = customerContext.getProfessionalEmail();

        if (personalEmail != null && !personalEmail.trim().isEmpty()) {
            personalEmailButton.setText(personalEmail);
        } else {
            personalEmailButton.setVisibility(View.GONE);
        }
        if (professionalEmail != null && !professionalEmail.trim().isEmpty()) {
            professionalEmailButton.setText(professionalEmail);
        } else {
            professionalEmailButton.setVisibility(View.GONE);
        }

    }

    private void initRadioButtons(View view) {

        personalEmailButton = (RadioButton) view.findViewById(R.id.personal_email_button);
        personalEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseRadioButton(personalEmailButton);
            }
        });
        radioButtons.add(personalEmailButton);

        professionalEmailButton = (RadioButton) view.findViewById(R.id.professional_email_button);
        professionalEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseRadioButton(professionalEmailButton);
            }
        });
        radioButtons.add(professionalEmailButton);

        freeEmailButton = (RadioButton) view.findViewById(R.id.free_email_button);
        freeEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseRadioButton(freeEmailButton);
            }
        });

        radioButtons.add(freeEmailButton);
    }

    private String getChosenEmail() {
        if (personalEmailButton.isChecked()) {
            return customerContext.getPersonalEmail();
        } else if (professionalEmailButton.isChecked()) {
            return customerContext.getProfessionalEmail();
        } else {
            return freeEmail.getText().toString();
        }
    }

    private void chooseRadioButton(RadioButton chosenRadioButton) {
        for (RadioButton radioButton : radioButtons) {
            radioButton.setChecked(radioButton == chosenRadioButton);
        }
    }

    private void applyState(ESendingSate state) {
        switch (state) {
            case TO_SEND:
                // View
                String personalEmail = customerContext.getPersonalEmail();
                String professionalEmail = customerContext.getProfessionalEmail();
                toSendView.setVisibility(View.VISIBLE);
                wishlistSendingInProgress.setVisibility(View.GONE);
                sendingStatus.setVisibility(View.GONE);
                // Title
                if (personalEmail == null && professionalEmail == null) {
                    freeEmailButton.setVisibility(View.GONE);
                    this.setTitle(R.string.type_email_for_wishlist);
                } else {
                    this.setTitle(R.string.choose_email_for_wishlist);
                }

                // Button
                this.setButton(BUTTON_POSITIVE, this.getContext().getString(android.R.string.ok), EMPTY_ON_CLICK_LISTENER);
                this.setButton(BUTTON_NEGATIVE, this.getContext().getString(android.R.string.cancel), EMPTY_ON_CLICK_LISTENER);
                if (this.getButton(BUTTON_POSITIVE) != null) {
                    this.getButton(BUTTON_POSITIVE).setOnClickListener(SEND_EMAIL_LISTENER);
                    this.getButton(BUTTON_POSITIVE).setText(R.string.send);
                    this.getButton(BUTTON_POSITIVE).invalidate();
                }
                break;
            case SENDING:
                // Buttons
                this.getButton(BUTTON_POSITIVE).setVisibility(View.GONE);
                // View
                toSendView.setVisibility(View.GONE);
                wishlistSendingInProgress.setVisibility(View.VISIBLE);
                sendingStatus.setVisibility(View.GONE);
                //Title
                this.setTitle(R.string.sending_in_progress);
                break;
            case SENT_SUCCESS:
                // Buttons
                this.setButton(BUTTON_POSITIVE, this.getContext().getString(android.R.string.ok), EMPTY_ON_CLICK_LISTENER);
                this.getButton(BUTTON_POSITIVE).setText(android.R.string.ok);
                this.getButton(BUTTON_POSITIVE).setVisibility(View.VISIBLE);
                this.getButton(BUTTON_POSITIVE).invalidate();
                this.getButton(BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onConfirmWishListSharingListener.onWishListSharingFinished(true);
                        WishListShareDialog.this.dismiss();
                    }
                });
                //View
                toSendView.setVisibility(View.GONE);
                wishlistSendingInProgress.setVisibility(View.GONE);
                sendingStatus.setVisibility(View.VISIBLE);
                sendingStatus.setText(R.string.wishlist_sharing_success);
                //Title
                this.setTitle(R.string.success);
                break;
            case SENT_FAILURE:
                // Buttons
                this.getButton(BUTTON_POSITIVE).setVisibility(View.VISIBLE);
                this.getButton(BUTTON_POSITIVE).setText(R.string.retry);
                this.getButton(BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        applyState(ESendingSate.TO_SEND);
                    }
                });
                this.getButton(BUTTON_POSITIVE).invalidate();
                //View
                toSendView.setVisibility(View.GONE);
                wishlistSendingInProgress.setVisibility(View.GONE);
                sendingStatus.setVisibility(View.VISIBLE);
                sendingStatus.setText(R.string.wishlist_sharing_failure);
                //Title
                this.setTitle(R.string.error);
                break;

        }
    }

    public static void shareWishlist(Context context,
                                     String email,
                                     CustomerContext customerContext,
                                     SellerContext sellerContext,
                                     List<ProductItem> productItems,
                                     ProductSharePresenter productSharePresenter,
                                     ConfigHelper configHelper
    ) {
        ProductShare productShare = new ProductShare();
        productShare.setChannel(ProductShare.EMAIL_CHANNEL);
        productShare.setChannel_ref(email);
        productShare.setCustomer_id(customerContext.getCustomerId());
        productShare.setSeller_id(sellerContext.getSeller().getId());
        productShare.setShop_id(configHelper.getCurrentShop().getId());
        List<String> productsIds = new ArrayList<>();
        if (productItems != null) {
            for (ProductItem productItem : productItems) {
                if (productItem.getProduct() != null) {
                    productsIds.add(productItem.getProduct().getId());
                }
            }
            productShare.setProduct_ids(productsIds);
            productSharePresenter.shareProducts(productShare);
        }
    }

}
