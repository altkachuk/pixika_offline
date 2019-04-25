package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.CustomerAutocompleteViewHolder;

/**
 * Created by damien on 02/05/16.
 */
public class CustomerAutocompleteAdapter extends RecyclerView.Adapter<CustomerAutocompleteViewHolder> {

    private Context context;

    private List<String> customers;

    public CustomerAutocompleteAdapter(Context ctx) {
        this.context = ctx;
    }

    public void setCustomers(List<String> customers) {
        this.customers = customers;
        notifyDataSetChanged();
    }

    public void clearCustomers() {
        if (customers != null) {
            customers.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public CustomerAutocompleteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_customer_autocomplete, parent, false);
        return new CustomerAutocompleteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CustomerAutocompleteViewHolder holder, int position) {
        String customerName = customers.get(position);
        holder.name.setText(customerName);
    }

    @Override
    public int getItemCount() {
        return customers != null ? customers.size() : 0;
    }
}
