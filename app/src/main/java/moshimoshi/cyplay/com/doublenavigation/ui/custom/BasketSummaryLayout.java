package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Basket;

/**
 * Created by wentongwang on 26/04/2017.
 */

public class BasketSummaryLayout extends AbstractSummary<Basket> {

    private Basket basket;

    public BasketSummaryLayout(Context context) {
        this(context, null);
    }

    public BasketSummaryLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BasketSummaryLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setItem(Basket basket, boolean includeDeliveryFees) {
        this.basket = basket;

        if (basket.getTotal_discount() == null || basket.getTotal_discount() == 0) {
            reductionContainer.setVisibility(GONE);
        } else {
            reductionContainer.setVisibility(VISIBLE);
            if (basketOffers != null) {
                basketOffers.setText("-" + configHelper.formatPrice(basket.getTotal_discount()));
            }
        }
        paymentModeContainer.setVisibility(GONE);

        if (basketSubTotal != null) {
            basketSubTotal.setText(configHelper.formatPrice(basket.getItems_total_amount()));
        }

        if (basketItemCount != null) {
            String count = String.valueOf(basket.getBasketItemsCount());
            String productsCount = this.getResources().getQuantityString(R.plurals.product_count, basket.getBasketItemsCount(), basket.getBasketItemsCount());
            int countPos = productsCount.indexOf(count);
            int countLength = count.length();
            //change the color of products number text
            SpannableStringBuilder style = new SpannableStringBuilder(productsCount);
            style.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.text_black)),
                    countPos, countPos + countLength,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            basketItemCount.setText(style);
        }

        if (basketTotalValue != null) {
            if (includeDeliveryFees) {
                basketTotalValue.setText(configHelper.formatPrice(basket.getTotal()));
            } else {
                basketTotalValue.setText(configHelper.formatPrice(basket.getTotal() - basket.getDelivery_fees_amount()));
            }
        }
        if (includeDeliveryFees) {
            showShippingFees();
        } else {
            hideDeliveryFeeContainer();
        }
        changeCellsBackground();
    }

    @Override
    public int getLayoutId() {
        return R.layout.customer_summary_layout;
    }

    private void showShippingFees() {
        if (basket.hasDeliveries()) {
            if (basket.getDelivery_fees_amount() == null) {
                noShippingFeesMessage.setVisibility(VISIBLE);
                deliveryFeeContainer.setVisibility(GONE);
            } else {
                deliveryFeeContainer.setVisibility(VISIBLE);
                if (shippingFees != null)
                    shippingFees.setText(configHelper.formatPrice(basket.getDelivery_fees_amount()));

                if (noShippingFeesMessage != null)
                    noShippingFeesMessage.setVisibility(GONE);
            }

        } else {
            deliveryFeeContainer.setVisibility(GONE);
        }
    }

    private void hideDeliveryFeeContainer() {
        deliveryFeeContainer.setVisibility(GONE);
        if (basket != null) {
            noShippingFeesMessage.setVisibility(basket.hasHomeDelivery() ? VISIBLE : GONE);
        }
    }

}
