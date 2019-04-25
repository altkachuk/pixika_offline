package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by romainlebouc on 16/08/16.
 */
public class CustomerStringContactDetails extends CustomerContactDetails<String> {

    @Nullable
    @BindView(R.id.customer_contact_details_icon)
    public ImageView icon;

    @Nullable
    @BindView(R.id.customer_contact_details_value)
    public TextView value;

    @Nullable
    @BindView(R.id.customer_contact_details_title)
    public TextView title;

    public CustomerStringContactDetails(Context context) {
        super(context);
    }

    public CustomerStringContactDetails(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomerStringContactDetails(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTitle(int stringResourceId) {
        if (this.title != null) {
            this.title.setText(stringResourceId);
        }
    }

    @Override
    public void setValue(String value) {
        if (this.value != null) {
            if (value != null && !value.trim().equals( "")) {
                this.value.setText(value);
                this.value.setTextColor(ContextCompat.getColor(this.getContext(), R.color.GreyishBrown));
            } else {
                setUndefined(this.value);
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
        return R.layout.custom_customer_string_contact_details;
    }

}
