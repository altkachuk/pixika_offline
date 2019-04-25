package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.utils.CompatUtils;

/**
 * Created by romainlebouc on 16/08/16.
 */
public class CustomerBooleanContactDetails  extends CustomerContactDetails<Boolean>{

    @Nullable
    @BindView(R.id.customer_contact_details_picture_value)
    protected ImageView pictureValue;

    @Nullable
    @BindView(R.id.customer_contact_details_value)
    protected TextView value;

    public CustomerBooleanContactDetails(Context context) {
        super(context);
    }

    public CustomerBooleanContactDetails(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomerBooleanContactDetails(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setValue(Boolean value) {
        if ( pictureValue !=null){
            if ( value !=null && value){
                CompatUtils.setDrawableTint(this.pictureValue.getDrawable(), ContextCompat.getColor(getContext(), R.color.colorAccent));
            }else{
                CompatUtils.setDrawableTint(this.pictureValue.getDrawable(), ContextCompat.getColor(getContext(), R.color.LightGrey));
            }
        }
        if (this.value !=null ){
            if ( value !=null){
                this.value.setText(value ? R.string.yes : R.string.no);
            }else{
                setUndefined(this.value);
            }

        }

    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.custom_customer_boolean_contact_details;
    }
}
