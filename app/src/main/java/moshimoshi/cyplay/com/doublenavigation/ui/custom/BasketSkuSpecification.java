package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductSpecification;

/**
 * Created by romainlebouc on 22/11/16.
 */
public class BasketSkuSpecification extends LinearLayout {

    @BindView(R.id.sku_spec_label)
    TextView label;


    @BindView(R.id.sku_spec_value)
    TextView value;


    public BasketSkuSpecification(Context context) {
        this(context, null);
    }

    public BasketSkuSpecification(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BasketSkuSpecification(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_basket_sku_spec, this, true);
        // Bind xml
        ButterKnife.bind(this);
    }

    public void setProductSpecification(ProductSpecification productSpecification) {
        label.setText(productSpecification.getSpec() + " : ");
        value.setText(productSpecification.getValue() + (productSpecification.getUnit() != null ? " " + productSpecification.getUnit() : ""));
    }

    public void changeTextColor(int color) {
        label.setTextColor(color);
        value.setTextColor(color);
    }
}
