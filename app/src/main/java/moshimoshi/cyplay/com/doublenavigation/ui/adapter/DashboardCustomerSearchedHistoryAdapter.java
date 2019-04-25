package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.CustomerPreviewViewHolder;

/**
 * Created by damien on 08/06/16.
 */
public class DashboardCustomerSearchedHistoryAdapter extends RecyclerView.Adapter<CustomerPreviewViewHolder> {

    private Context context;

    private List<Customer> customerPreviews;

    public DashboardCustomerSearchedHistoryAdapter(Context ctx) {
        this.context = ctx;
    }

    public void setCustomerPreviews(List<Customer> customerPreviews) {
        this.customerPreviews = customerPreviews;
        notifyDataSetChanged();
    }

    public void clearCustomerPreviews() {
        if (customerPreviews != null) {
            customerPreviews.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public CustomerPreviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_customer_history_seller_dashboard, parent, false);
        return new CustomerPreviewViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CustomerPreviewViewHolder holder, int position) {
        Customer preview = customerPreviews.get(position);
        if (preview != null) {
            holder.setItem(preview);
        }
    }

    @Override
    public int getItemCount() {
        return customerPreviews != null ? customerPreviews.size() : 0;
    }

}
