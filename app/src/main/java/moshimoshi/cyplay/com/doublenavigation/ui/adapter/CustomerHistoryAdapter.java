package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.CustomerPreviewViewHolder;

/**
 * Created by damien on 09/05/16.
 */
public class CustomerHistoryAdapter extends CustomerPreviewAdapter {

    public CustomerHistoryAdapter(Context ctx, int layout) {
        super(ctx, layout);
    }

    @Override
    public void onBindViewHolder(CustomerPreviewViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if ( holder.getGender() !=null){
            holder.getGender().setImageResource(R.drawable.ic_history_black_24dp);
        }
    }

}
