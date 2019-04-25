package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerPaymentTransaction;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPaymentType;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.ETransactionStatus;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;

/**
 * Created by romainlebouc on 03/04/2017.
 */

public class TransactionViewHolder extends ItemViewHolder<CustomerPaymentTransaction> {

    @Inject
    ConfigHelper configHelper;

    @BindView(R.id.transaction_amout)
    TextView amount;

    @BindView(R.id.transaction_mean)
    TextView transactionMean;

    @BindView(R.id.transaction_status)
    TextView status;

    @BindView(R.id.transaction_date)
    TextView transactionDate;

    final DateFormat dateFormat;

    public TransactionViewHolder(View itemView) {
        super(itemView);
        dateFormat = DateFormat.getDateTimeInstance(
                DateFormat.DEFAULT,
                DateFormat.DEFAULT,
                context.getResources().getConfiguration().locale);
    }


    @Override
    public void setItem(CustomerPaymentTransaction customerPaymentTransaction) {

        amount.setText(configHelper.formatPrice(customerPaymentTransaction.getAmount()));
        Date creationDate = customerPaymentTransaction.getCreation_date();
        if (creationDate != null) {
            transactionDate.setText(dateFormat.format(creationDate));
        } else {
            transactionDate.setText("");
        }

        ETransactionStatus eTransactionStatus = customerPaymentTransaction.getETransactionStatus();
        if (eTransactionStatus != null) {
            status.setText(customerPaymentTransaction.getETransactionStatus().getLabelId());
        } else {
            status.setText("");
        }

        EPaymentType ePaymentType = customerPaymentTransaction.getEPaymentType();
        if (ePaymentType != null) {
            transactionMean.setText(ePaymentType.getLabelId());
        } else {
            transactionMean.setText("");
        }

    }
}
