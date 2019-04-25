package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.utils.StringUtils;

/**
 * Created by damien on 03/05/16.
 */
public class CustomerPreviewViewHolder extends ItemViewHolder<Customer> {

    private final static String EMPTY_FIELD = "non renseigné";

    @Nullable
    @BindView(R.id.customer_gender)
    protected ImageView gender;

    @Nullable
    @BindView(R.id.customer_name)
    protected TextView name;

    @Nullable
    @BindView(R.id.customer_city)
    protected TextView city;

    @Nullable
    @BindView(R.id.customer_first_email)
    protected TextView firstEmail;

    @Nullable
    @BindView(R.id.customer_organization_name)
    protected TextView customerOrganizationName;

    @Nullable
    @BindView(R.id.customer_source)
    protected TextView customerSource;

    private Customer customer;

    public CustomerPreviewViewHolder(View view) {
        super(view);
    }

    @Override
    public void setItem(Customer customer) {
        this.customer = customer;

        if (gender != null) {
            gender.setImageResource(customer.getDetails().getEGender().getIconResource());
        }

        if (name != null) {
            name.setText(customer.getDetails().getFormatName(this.context));
        }

        if (customerOrganizationName != null) {
            fillTextView(customerOrganizationName, customer.getProfessionalDetails().getOrganizationName());
        }

        if (customerSource != null) {
            fillTextView(customerSource, customer.getSource());
        }

        if (city != null) {
            if (customer.getDetails() != null
                    && customer.getDetails().getMail() != null) {
                fillTextView(city, customer.getDetails().getMail().getCity());
            } else {
                fillTextView(city, null);
            }
        }

        if (firstEmail != null) {
            if (customer.getDetails() != null
                    && customer.getDetails().getEmail() != null) {
                fillTextView(firstEmail, customer.getDetails().getEmail().getFirstEmail());
            } else {
                fillTextView(firstEmail, null);
            }
        }

    }



    @Nullable
    public ImageView getGender() {
        return gender;
    }

    public void fillTextView(TextView textView, String value) {
        if (!StringUtils.isEmpty(value)) {
            textView.setText(value);
            textView.setTypeface(null, Typeface.NORMAL);
            textView.setTextColor(ContextCompat.getColor(textView.getContext(), R.color.Gray));
        } else {
            textView.setText(EMPTY_FIELD);
            textView.setTypeface(null, Typeface.ITALIC);
            textView.setTextColor(ContextCompat.getColor(textView.getContext(), R.color.Silver));
        }
    }

}
