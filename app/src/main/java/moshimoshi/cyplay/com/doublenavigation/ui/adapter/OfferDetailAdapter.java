package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketOffer;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.OfferDetailViewHolder;

/**
 * Created by wentongwang on 18/04/2017.
 */

public class OfferDetailAdapter extends ItemAdapter<BasketOffer, OfferDetailViewHolder> {

    public OfferDetailAdapter(Context context) {
        super(context);
    }

    @Override
    public OfferDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_offer_detail, parent, false);
        return new OfferDetailViewHolder(v);
    }
}
