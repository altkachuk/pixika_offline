package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;

/**
 * Created by romainlebouc on 30/03/2017.
 */

public abstract class AbstractSummary<Item> extends LinearLayout {
    protected boolean needBottonLine = false;

    @Inject
    ConfigHelper configHelper;

    @Nullable
    @BindView(R.id.basket_product_count)
    TextView basketItemCount;

    @Nullable
    @BindView(R.id.basket_sub_total)
    TextView basketSubTotal;

    @Nullable
    @BindView(R.id.basket_offers_reduction)
    TextView basketOffers;

    @Nullable
    @BindView(R.id.transaction_means)
    TextView transactionMeans;

    @Nullable
    @BindView(R.id.basket_shipping_fees)
    TextView shippingFees;

    @Nullable
    @BindView(R.id.basket_total)
    TextView basketTotalValue;

    @Nullable
    @BindView(R.id.bottom_line_layout)
    View bottomLineLayout;

    @Nullable
    @BindView(R.id.shipping_fees_container)
    View shippingFeesContainer;

    @Nullable
    @BindView(R.id.delivery_fee_not_specified_message)
    TextView noShippingFeesMessage;

    @Nullable
    @BindView(R.id.reduction_container)
    protected View reductionContainer;

    @Nullable
    @BindView(R.id.delivery_fee_container)
    protected View deliveryFeeContainer;

    @Nullable
    @BindView(R.id.payment_mode_container)
    protected View paymentModeContainer;

    @Nullable
    @BindView(R.id.summary_container)
    protected LinearLayout summaryContainer;

    protected Context context;

    public AbstractSummary(Context context) {
        this(context, null);
    }

    public AbstractSummary(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbstractSummary(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        PlayRetailApp.get(context).inject(this);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(getLayoutId(), this, true);
        // Bind xml
        ButterKnife.bind(this);
        this.context = context;
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BasketSummary);
        needBottonLine = ta.getBoolean(R.styleable.BasketSummary_needBottomLine, false);
        ta.recycle();
    }

    public abstract void setItem(Item item, boolean includeDeliveryFees);

    public abstract int getLayoutId();

    protected void changeCellsBackground() {
        int visiableChildCount = -1;
        if (summaryContainer != null) {
            for (int i = 0; i < summaryContainer.getChildCount(); i++) {
                View child = summaryContainer.getChildAt(i);
                if (child.getVisibility() == VISIBLE) {
                    visiableChildCount++;
                }
                child.setBackgroundColor(ContextCompat.getColor(this.context, visiableChildCount % 2 == 0 ? R.color.grey : R.color.summary_second_bg));
            }
        }
    }

}
