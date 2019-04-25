package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerDetails;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerDetailsContactability;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerKpi;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EResourceAttribute;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.DisplayActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.CustomerActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.CustomerFormActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.DisplayEventFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceBaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.CustomerInfo;
import moshimoshi.cyplay.com.doublenavigation.utils.ActionEventHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.CompatUtils;
import moshimoshi.cyplay.com.doublenavigation.utils.StringUtils;
import ninja.cyplay.com.apilibrary.models.business.enums.EActionStatus;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;
import ninja.cyplay.com.apilibrary.utils.Constants;

/**
 * Created by romainlebouc on 29/07/16.
 */
public class CustomerInfoFragment extends ResourceBaseFragment<Customer> implements DisplayEventFragment {

    @Inject
    CustomerContext customerContext;

    @Inject
    ActionEventHelper actionEventHelper;

    @BindView(R.id.customer_edit_form_button)
    ImageView customerEditButton;

    @Nullable
    @BindView(R.id.customer_edit_form_text)
    TextView customerEditText;

    @BindView(R.id.customer_rfm_container)
    LinearLayout rfmContainer;

    @Nullable
    @BindView(R.id.customer_info_customer_name)
    TextView customerName;

    @Nullable
    @BindView(R.id.customer_info_first_name)
    TextView customerFirstName;

    @Nullable
    @BindView(R.id.customer_info_last_name)
    TextView customerLastName;

    @BindView(R.id.customer_info)
    CustomerInfo customerInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_customer_info, container, false);
    }

    @Override
    protected void setResource(Customer customer) {
        if (customer != null && customer.getDetails() != null) {
            customerInfo.setCustomer(customer);

            CustomerDetails details = customer.getDetails();
            if (customerName != null) {
                customerName.setText(details.getFormatName(this.getContext()));
            }
            if (customerFirstName != null) {
                customerFirstName.setText(details.getFirst_name());
            }
            if (customerLastName != null) {
                customerLastName.setText(details.getLast_name());
            }

            CustomerKpi customerKpi = customer.getKpi();
            if (customerKpi != null) {
                String rfm = customerKpi.getRfm();
                Integer rfmColor = configHelper.getCustomerRfmColor(rfm);
                if (rfmColor != null) {
                    rfmContainer.setBackgroundColor(rfmColor);
                    rfmContainer.invalidate();
                    CompatUtils.setDrawableTint(customerEditButton.getDrawable(), rfmColor);

                    if (customerEditText != null) {
                        customerEditText.setTextColor(rfmColor);
                    }
                }
            }

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (configHelper.getCustomerConfig().getEditable() != null) {
            customerEditButton.setVisibility(configHelper.getCustomerConfig().getEditable() ? View.VISIBLE : View.GONE);
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    @Subscribe
    public void onResourceResponseEvent(ResourceResponseEvent<Customer> resourceResponseEvent) {
        if (EResourceType.CUSTOMER == resourceResponseEvent.getEResourceType()) {
            super.onResourceResponse(resourceResponseEvent);
        }
    }

    @Override
    @Subscribe
    public void onResourceRequestEvent(ResourceRequestEvent<Customer> resourceRequestEvent) {
        if (EResourceType.CUSTOMER == resourceRequestEvent.getEResourceType()) {
            super.onResourceRequest(resourceRequestEvent);
        }
    }

    @Override
    public Customer getCachedResource() {
        return ((ResourceActivity<Customer>) this.getActivity()).getResource();
    }

    @Override
    public void loadResource() {
        ((CustomerActivity) this.getActivity()).loadResource();
    }

    private String formatOptin(CustomerDetailsContactability contactability) {
        if (contactability != null) {
            List<String> optin = new ArrayList<>();
            if (contactability.getPhone() != null && contactability.getPhone()) {
                optin.add(this.getResources().getString(R.string.phone).toUpperCase());
            }
            if (contactability.getSms() != null && contactability.getSms()) {
                optin.add(this.getResources().getString(R.string.sms).toUpperCase());
            }
            if (contactability.getNewsletter() != null && contactability.getNewsletter()) {
                optin.add(this.getResources().getString(R.string.newsletter).toUpperCase());
            }
            if (contactability.getMail() != null && contactability.getMail()) {
                optin.add(this.getResources().getString(R.string.mail).toUpperCase());
            }
            if (contactability.getEmail() != null && contactability.getEmail()) {
                optin.add(this.getResources().getString(R.string.email).toUpperCase());
            }
            return StringUtils.join(optin.toArray(), " • ");
        } else {
            return "";
        }

    }

    private String formatPartnerOptin(CustomerDetailsContactability contactability) {
        if (contactability != null) {
            List<String> optin = new ArrayList<>();
            if (contactability.getPartner_phone() != null && contactability.getPartner_phone()) {
                optin.add(this.getResources().getString(R.string.phone).toUpperCase());
            }
            if (contactability.getPartner_sms() != null && contactability.getPartner_sms()) {
                optin.add(this.getResources().getString(R.string.sms).toUpperCase());
            }
            if (contactability.getPartner_newsletter() != null && contactability.getPartner_newsletter()) {
                optin.add(this.getResources().getString(R.string.newsletter).toUpperCase());
            }
            if (contactability.getPartner_mail() != null && contactability.getPartner_mail()) {
                optin.add(this.getResources().getString(R.string.mail).toUpperCase());
            }
            if (contactability.getPartner_email() != null && contactability.getPartner_email()) {
                optin.add(this.getResources().getString(R.string.email).toUpperCase());
            }
            return StringUtils.join(optin.toArray(), " • ");
        } else {
            return "";
        }
    }


    @Optional
    @OnClick({R.id.customer_edit_form_button, R.id.customer_edit_form_text})
    public void onCustomerEditFormClick() {
        startFormIntent();
    }

    private void startFormIntent() {
        // start intent
        Customer customer = customerContext.getCustomer();
        if (customer != null) {
            Intent intent = new Intent(getContext(), CustomerFormActivity.class);
            intent.putExtra(IntentConstants.EXTRA_CUSTOMER, Parcels.wrap(customer));
            intent.putExtra(IntentConstants.EXTRA_FORM_EDIT_MODE, Constants.FORM_UPDATE_KEY);

            getActivity().startActivityForResult(intent, IntentConstants.EXTRA_FORM_EDIT_RESULT);
            getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.no_anim);
        }
    }

    @Override
    public void logEvent() {
        actionEventHelper.log(new DisplayActionEventData()
                .setResource(EResource.CUSTOMER)
                .setAttribute(EResourceAttribute.DETAILS.getCode())
                .setValue(customerContext !=null ? customerContext.getCustomerId() : null)
                .setStatus(EActionStatus.SUCCESS));
    }


}
