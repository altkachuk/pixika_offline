package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by damien on 23/08/16.
 */
public class TogglePriceView extends FrameLayout {

    @Nullable
    @BindView(R.id.hidden_price)
    TextView hiddenPrice;

    @Nullable
    @BindView(R.id.visible_price_label)
    TextView visiblePrice;

    private String price;

    private Boolean priveVisibility = false;

    public TogglePriceView(Context context) {
        super(context);
        init();
    }

    public TogglePriceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TogglePriceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_toggle_price_view, this, true);
        // Bind xml
        ButterKnife.bind(this);
        // Set price
        if (price != null && price.length() > 0)
            visiblePrice.setText(price);
    }

    public void toggle() {
        hiddenPrice.setVisibility(priveVisibility ? VISIBLE : GONE);
        visiblePrice.setVisibility(priveVisibility ? GONE : VISIBLE);
        priveVisibility = !priveVisibility;
    }

    public void setPrice(String price) {
        this.price = price;
        if (visiblePrice != null)
            visiblePrice.setText(price);
    }


}
