package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerLoyalty;

/**
 * Created by romainlebouc on 16/08/16.
 */
public class CustomerLoyaltyContactDetails extends CustomerContactDetails<CustomerLoyalty> {

    @Nullable
    @BindView(R.id.customer_contact_details_icon)
    public ImageView icon;

    @Nullable
    @BindView(R.id.customer_contact_details_value)
    public TextView value;

    @Nullable
    @BindView(R.id.customer_contact_details_value_2)
    public TextView value2;

    @Nullable
    @BindView(R.id.customer_contact_details_title)
    public TextView title;

    private DateFormat dateFormat;

    public CustomerLoyaltyContactDetails(Context context) {
        super(context);
        init();
    }

    public CustomerLoyaltyContactDetails(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomerLoyaltyContactDetails(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        dateFormat = DateFormat.getDateInstance(
                DateFormat.DEFAULT,
                this.getContext().getResources().getConfiguration().locale);
    }

    public void setTitle(int stringResourceId) {
        if (this.title != null) {
            this.title.setText(stringResourceId);
        }
    }

    @Override
    public void setValue(CustomerLoyalty loyalty) {
        String value = this.getResources().getQuantityString(R.plurals.customer_loyalty_points,
                loyalty.getPoints() != null ? loyalty.getPoints().intValue() : 0,
                loyalty.getId() != null ? loyalty.getId() : "",
                loyalty.getProgram() != null ? loyalty.getProgram() : "",
                loyalty.getPoints() != null ? loyalty.getPoints().intValue() : 0);

        if (this.value != null) {
            if (value != null && !value.trim().equals("")) {
                this.value.setText(value);
                this.value.setTextColor(ContextCompat.getColor(this.getContext(), R.color.GreyishBrown));
            } else {
                setUndefined(this.value);
            }
        }

        if (loyalty.getECustomerLoyaltyState() == null || loyalty.getProgram() == null) {
            this.value2.setVisibility(GONE);
        } else {
            String value2 = this.getResources().getString(R.string.customer_loyalty_validity,
                    dateFormat.format(loyalty.getEnd_date()),
                    this.getContext().getString(loyalty.getECustomerLoyaltyState().getLabelId())
            );

            if (this.value != null) {
                if (value != null && !value.trim().equals("")) {
                    this.value2.setText(value2);
                    this.value2.setTextColor(ContextCompat.getColor(this.getContext(), R.color.GreyishBrown));
                } else {
                    this.value2.setText("");
                }
            }
        }
    }

    public void setIcon(int iconResourceId) {
        if (icon != null) {
            this.icon.setVisibility(VISIBLE);
            this.icon.setImageResource(iconResourceId);
        }
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.custom_customer_loyalty_contact_details;
    }

}
