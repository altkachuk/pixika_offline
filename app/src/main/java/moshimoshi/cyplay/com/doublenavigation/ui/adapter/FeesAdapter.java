package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Fee;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.FeeViewHolder;

/**
 * Created by romainlebouc on 30/01/2017.
 */

public class FeesAdapter extends ItemAdapter<Fee, FeeViewHolder> {

    public FeesAdapter(Context context) {
        super(context);
    }

    @Override
    public FeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View productView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_fee, parent, false);
        return new FeeViewHolder(productView);
    }
}
