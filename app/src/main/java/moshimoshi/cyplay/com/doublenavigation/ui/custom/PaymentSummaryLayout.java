package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerPaymentTransaction;
import moshimoshi.cyplay.com.doublenavigation.model.business.Payment;
import moshimoshi.cyplay.com.doublenavigation.utils.StringUtils;

/**
 * Created by wentongwang on 28/04/2017.
 */

public class PaymentSummaryLayout extends AbstractSummary<Payment> {

    Payment payment;

    public PaymentSummaryLayout(Context context) {
        this(context, null);
    }

    public PaymentSummaryLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaymentSummaryLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setItem(Payment payment, boolean includeDeliveryFees) {
        this.payment = payment;
        if (basketOffers != null) {
            if (payment != null) {
                if (payment.getItems_total_discount() == null || payment.getItems_total_discount() == 0) {
                    reductionContainer.setVisibility(GONE);
                } else {
                    reductionContainer.setVisibility(VISIBLE);
                    basketOffers.setText("-" + configHelper.formatPrice(payment.getItems_total_discount()));
                }
            } else {
                reductionContainer.setVisibility(GONE);
            }

        }

        if (shippingFees != null) {
            if (payment != null) {
                deliveryFeeContainer.setVisibility(VISIBLE);
                shippingFees.setText(configHelper.formatPrice(payment.getDelivery_fees_amount()));
            } else {
                deliveryFeeContainer.setVisibility(GONE);
            }
        }

        if (basketItemCount != null) {
            if (payment != null) {
                String count = String.valueOf(payment.getTotalItemCount());
                String productsCount = this.getResources().getQuantityString(R.plurals.product_count, payment.getTotalItemCount(), payment.getTotalItemCount());
                int countPos = productsCount.indexOf(count);
                int countLength = count.length();
                //change the color of products number text
                SpannableStringBuilder style = new SpannableStringBuilder(productsCount);
                style.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.text_black)),
                        countPos, countPos + countLength,
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                basketItemCount.setText(style);
            } else {
                basketItemCount.setText("");
            }
        }

        if (basketSubTotal != null) {
            if (payment != null) {
                basketSubTotal.setText(configHelper.formatPrice(payment.getItems_total_amount()));
            } else {
                basketSubTotal.setText("");
            }
        }

        if (basketTotalValue != null) {
            if (payment != null) {
                if (includeDeliveryFees) {
                    basketTotalValue.setText(configHelper.formatPrice(payment.getTotal()));
                } else {
                    basketTotalValue.setText(configHelper.formatPrice(payment.getTotal() - payment.getDelivery_fees_amount()));
                }
            } else {
                basketTotalValue.setText("");
            }
        }

        if (transactionMeans != null) {
            List<CustomerPaymentTransaction> customerPaymentTransactions = payment.getTransactions();
            if (customerPaymentTransactions != null) {
                String[] paymentMeans = new String[customerPaymentTransactions.size()];
                int i = 0;
                for (CustomerPaymentTransaction customerPaymentTransaction : customerPaymentTransactions) {
                    paymentMeans[i] = customerPaymentTransaction.getEPaymentType() != null ? getContext().getString(customerPaymentTransaction.getEPaymentType().getLabelId()) : "";
                }
                paymentModeContainer.setVisibility(VISIBLE);
                transactionMeans.setText(StringUtils.join(paymentMeans, ", "));
            } else {
                paymentModeContainer.setVisibility(GONE);
                transactionMeans.setText("");
            }
        }

        if (payment != null) {
            showShippingFees(payment);
        }

        changeCellsBackground();
    }

    @Override
    public int getLayoutId() {
        return R.layout.customer_summary_layout;
    }

    private void showShippingFees(Payment payment) {
        if (payment.hasDeliveries()) {
            if (payment.getDelivery_fees_amount() == null) {
                deliveryFeeContainer.setVisibility(GONE);
            } else {
                deliveryFeeContainer.setVisibility(VISIBLE);
                if (shippingFees != null)
                    shippingFees.setText(configHelper.formatPrice(payment.getDelivery_fees_amount()));
            }
        } else {
            deliveryFeeContainer.setVisibility(GONE);
        }

    }
}
