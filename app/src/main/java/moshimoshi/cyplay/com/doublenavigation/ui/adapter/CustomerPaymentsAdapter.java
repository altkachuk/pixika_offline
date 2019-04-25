package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Payment;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerPaymentTransaction;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.PaymentsViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.TransactionViewHolder;


/**
 * Created by wentongwang on 20/03/2017.
 */


public class CustomerPaymentsAdapter extends AbstractExpandableItemAdapter<PaymentsViewHolder, TransactionViewHolder> {

    private List<Payment> payments;
    private Fragment fragment;

    public CustomerPaymentsAdapter(Fragment fragment) {
        super();
        this.fragment = fragment;
        setHasStableIds(true);
    }

    @Override
    public int getGroupCount() {
        return payments != null ? payments.size() : 0;
    }

    @Override
    public int getChildCount(int groupPosition) {
        List<CustomerPaymentTransaction> transactions = payments.get(groupPosition).getTransactions();
        return transactions != null ? transactions.size() : 0;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return payments.get(groupPosition).getId().hashCode();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return payments.get(groupPosition).getTransactions().get(childPosition).getId().hashCode();
    }

    @Override
    public PaymentsViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_customer_payments_item, parent, false);
        return new PaymentsViewHolder(v, fragment.getTag());
    }

    @Override
    public TransactionViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_customer_transaction_item, parent, false);
        return new TransactionViewHolder(v);
    }

    @Override
    public void onBindGroupViewHolder(PaymentsViewHolder holder, int groupPosition, int viewType) {
        holder.setItem(payments.get(groupPosition));
    }

    @Override
    public void onBindChildViewHolder(TransactionViewHolder holder, int groupPosition, int childPosition, int viewType) {
        holder.setItem(payments.get(groupPosition).getTransactions().get(childPosition));
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(PaymentsViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        return true;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
        if (payments != null) {
            this.notifyDataSetChanged();
        }

    }
}