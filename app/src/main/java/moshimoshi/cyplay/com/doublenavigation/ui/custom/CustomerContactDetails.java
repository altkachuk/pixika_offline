package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by romainlebouc on 29/07/16.
 */
public abstract class CustomerContactDetails<Type> extends LinearLayout {

    @Nullable
    @BindView(R.id.customer_contact_details_title)
    public TextView title;

    public CustomerContactDetails(Context context) {
        this(context, null);
    }

    public CustomerContactDetails(Context context, AttributeSet attrs) {
        this(context,attrs, 0);
    }

    public CustomerContactDetails(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(getLayoutResourceId(), this, true);
        // Bind xml
        ButterKnife.bind(this);
    }

    public void setTitle(String title){
        if ( this.title !=null){
            this.title.setText(title);
        }
    }

    public void setTitle(int title_resource_od){
        if ( this.title !=null){
            this.title.setText(title_resource_od);
        }
    }

    public abstract void setValue(Type value);
    public abstract int getLayoutResourceId();

    public void setUndefined(TextView textView){
        textView.setText(R.string.undefined_attribute);
        textView.setTextColor(ContextCompat.getColor(this.getContext(),R.color.LightGrey));
    }


}
