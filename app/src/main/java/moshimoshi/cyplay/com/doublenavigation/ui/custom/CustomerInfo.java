package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.BuildConfig;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerDetails;
import moshimoshi.cyplay.com.doublenavigation.model.business.Mail;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerDetailsPhone;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerLoyalty;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProfessionalDetails;
import moshimoshi.cyplay.com.doublenavigation.utils.PhoneUtils;
import moshimoshi.cyplay.com.doublenavigation.utils.StringUtils;

/**
 * Created by romainlebouc on 16/11/16.
 */
public class CustomerInfo extends LinearLayout {

    @Nullable
    @BindView(R.id.customer_info_id)
    CustomerStringContactDetails customerId;

    @Nullable
    @BindView(R.id.customer_info_fidelity)
    CustomerStringContactDetails infoFidelity;

    @Nullable
    @BindView(R.id.customer_info_loyalty)
    CustomerLoyaltyContactDetails infoLoyalty;

    @Nullable
    @BindView(R.id.customer_info_address)
    CustomerStringContactDetails infoAddress;

    @Nullable
    @BindView(R.id.customer_info_email)
    CustomerStringContactDetails infoEmail;

    @Nullable
    @BindView(R.id.customer_info_phone)
    CustomerStringContactDetails infoPhone;

    @Nullable
    @BindView(R.id.customer_info_birthdate)
    CustomerStringContactDetails infoBirthdate;

    @Nullable
    @BindView(R.id.customer_info_birthdate_texto)
    CustomerStringContactDetails infoBirthdateTexto;

    @Nullable
    @BindView(R.id.customer_info_optin_email)
    CustomerBooleanContactDetails infoEmailOptin;

    @Nullable
    @BindView(R.id.customer_info_partneroptin_email)
    CustomerBooleanContactDetails infoEmailPartnerOptin;

    @Nullable
    @BindView(R.id.customer_info_organization_name)
    CustomerStringContactDetails infoOrganizationName;

    @Nullable
    @BindView(R.id.customer_info_organization_code)
    CustomerStringContactDetails infoOrganizationCode;

    @Nullable
    @BindView(R.id.customer_info_source)
    CustomerStringContactDetails infoSource;

    @Nullable
    @BindView(R.id.customer_info_images)
    CustomerImagesDetails infoImages;

    public DateFormat dateFormat;

    public CustomerInfo(Context context) {
        super(context);
        init();
    }

    public CustomerInfo(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomerInfo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomerInfo(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.custom_customer_info, this);
        ButterKnife.bind(this);
        dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, getResources().getConfiguration().locale);

        if (customerId != null) {
            customerId.setTitle(R.string.customer_id);
            customerId.setIcon(R.drawable.ic_customer_id);
        }

        if (infoFidelity != null) {
            infoFidelity.setTitle(R.string.loyalty);
            infoFidelity.setIcon(R.drawable.ic_customer_loyalty);
        }

        if ( infoLoyalty !=null){
            infoLoyalty.setTitle(R.string.loyalty);
            infoLoyalty.setIcon(R.drawable.ic_customer_loyalty);
        }

        if (infoAddress != null) {
            infoAddress.setTitle(R.string.address);
            infoAddress.setIcon(R.drawable.ic_position);
        }

        if (infoEmail != null) {
            infoEmail.setTitle(R.string.email);
            infoEmail.setIcon(R.drawable.ic_email);
        }

        if (infoPhone != null) {
            infoPhone.setTitle(R.string.phone);
            infoPhone.setIcon(R.drawable.ic_phone);
        }

        if (infoBirthdate != null) {
            infoBirthdate.setTitle(R.string.birthdate);
            infoBirthdate.setIcon(R.drawable.ic_calendar);
        }

        if (infoBirthdateTexto != null) {
            infoBirthdateTexto.setTitle(R.string.birthdate);
            infoBirthdateTexto.setIcon(R.drawable.ic_calendar);
        }

        if (infoEmailOptin != null) {
            infoEmailOptin.setTitle(this.getResources().getString(R.string.receive_client_offers_email, BuildConfig.CLIENT_NAME));
        }

        if (infoEmailPartnerOptin != null) {
            infoEmailPartnerOptin.setTitle(R.string.receive_partner_offers_email);
        }

        if (infoOrganizationName != null) {
            infoOrganizationName.setTitle(R.string.form_organization_name);
            infoOrganizationName.setIcon(R.drawable.ic_customer_loyalty);
        }

        if (infoOrganizationCode != null) {
            infoOrganizationCode.setTitle(R.string.form_organization_code);
            infoOrganizationCode.setIcon(R.drawable.ic_customer_loyalty);
        }

        if (infoSource != null) {
            infoSource.setTitle(R.string.form_source);
            infoSource.setIcon(R.drawable.ic_customer_loyalty);
        }

        if (infoImages != null) {
            infoImages.setTitle(R.string.form_badges);
            infoImages.setIcon(R.drawable.ic_customer_loyalty);
        }
    }

    public void setCustomer(Customer customer) {
        CustomerDetails details = customer.getDetails();
        ProfessionalDetails professionalDetails = customer.getProfessionalDetails();
        CustomerLoyalty loyalty = customer.getLoyalty();
        if (loyalty != null && infoFidelity != null) {
            infoFidelity.setValue(this.getResources().getQuantityString(R.plurals.customer_loyalty_points,
                    loyalty.getPoints() != null ? loyalty.getPoints().intValue() : 0,
                    loyalty.getId() != null ? loyalty.getId() : "",
                    loyalty.getProgram() != null ? loyalty.getProgram() : "",
                    loyalty.getPoints() != null ? loyalty.getPoints().intValue() : 0)
            );
        }

        if ( loyalty !=null && infoLoyalty!=null){
            infoLoyalty.setValue(loyalty);
        }


        if ( customerId !=null){
            customerId.setValue(customer.getId() != null ? customer.getId() : "");
        }

        if (infoAddress != null) {
            infoAddress.setValue(formatAddress(details.getMail()));
        }

        if (infoPhone != null) {
            if (details.getPhone() != null) {
                CustomerDetailsPhone.PhoneDetail phoneDetail = details.getPhone().getFirstPhoneNumber();
                if (phoneDetail != null) {
                    infoPhone.setTitle(phoneDetail.getePhoneType().getLabelResourceId());

                    String formattedPhoneNumber = PhoneUtils.formatPhoneNumber(phoneDetail.getPhoneNumber());
                    infoPhone.setValue(formattedPhoneNumber);
                } else {
                    infoPhone.setValue("");
                }
            } else {
                infoPhone.setValue("");
            }
        }


        if (infoEmail != null) {
            if (details.getEmail() != null) {
                infoEmail.setValue(details.getEmail().getFirstEmail());
            } else {
                infoEmail.setValue("");
            }
        }

        if (infoBirthdateTexto != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM");
            String birthDate = details.getBirth_date() != null ? simpleDateFormat.format(details.getBirth_date()) : "";
            infoBirthdateTexto.setValue(birthDate);
        }

        if (infoBirthdate != null) {
            String birthDate = details.getBirth_date() != null ? dateFormat.format(details.getBirth_date()) : "";
            infoBirthdate.setValue(birthDate);
        }


        if (infoEmailOptin != null) {
            infoEmailOptin.setValue(details.getContactability() != null && details.getContactability().getEmail() != null ? details.getContactability().getEmail() : false);
        }

        if (infoEmailPartnerOptin != null) {
            infoEmailPartnerOptin.setValue(details.getContactability() != null && details.getContactability().getPartner_email() != null ? details.getContactability().getPartner_email() : false);
        }

        if (infoOrganizationName != null) {
            infoOrganizationName.setValue(professionalDetails.getOrganizationName());
        }

        if (infoOrganizationCode != null) {
            infoOrganizationCode.setValue(professionalDetails.getCode());
        }

        if (infoSource!= null) {
            infoSource.setValue(customer.getSource());
        }

        if (infoImages!= null) {
            infoImages.setValue(details.getMedias());
        }

    }

    private String formatAddress(Mail mail) {
        if (mail != null) {
            List<String> addressItems = new ArrayList<>();
            for (String item : new String[]{mail.getAddress1(),
                    mail.getAddress2(),
                    mail.getAddress3(),
                    mail.getAddress4(),
                    mail.getZipcode(),
                    mail.getCity(),
                    mail.getRegion(),
                    mail.getCountry()}) {
                if (item != null) {
                    addressItems.add(item);
                }
            }
            return StringUtils.join(addressItems.toArray(), " • ");
        } else {
            return "";
        }

    }


}
