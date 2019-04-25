package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.CustomerPreviewViewHolder;

/**
 * Created by damien on 03/05/16.
 */
public class CustomerPreviewAdapter extends ItemAdapter<Customer, CustomerPreviewViewHolder> {

    private int layout;

    public CustomerPreviewAdapter(Context context, int layout) {
        super(context);
        this.layout = layout;
    }

    @Override
    public CustomerPreviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new CustomerPreviewViewHolder(v);
    }

}
