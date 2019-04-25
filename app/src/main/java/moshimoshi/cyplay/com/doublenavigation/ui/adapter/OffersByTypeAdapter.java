package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.IOffer;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.OffersViewHolder;

/**
 * Created by wentongwang on 07/03/2017.
 */

public class OffersByTypeAdapter extends AbstractOffersByTypeAdapter<OffersViewHolder, IOffer> {

    public OffersByTypeAdapter(Context context) {
        super(context);
    }

    @Override
    public OffersViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        OffersViewHolder offersViewHolder = null;
        View customerOfferView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_customer_offer_read_only_item, parent, false);
        offersViewHolder = new OffersViewHolder(customerOfferView);
        return offersViewHolder;
    }
}
