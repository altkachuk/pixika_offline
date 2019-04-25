package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketItemOffer;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.BasketItemOfferViewHolder;

/**
 * Created by wentongwang on 07/03/2017.
 */

public class BasketItemOffersByTypeAdapter extends AbstractOffersByTypeAdapter<BasketItemOfferViewHolder, BasketItemOffer> {

    public BasketItemOffersByTypeAdapter(Context context) {
        super(context);
    }


    @Override
    public BasketItemOfferViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        BasketItemOfferViewHolder offersViewHolder = null;
//        switch (viewType) {
//            case TYPE_BASKET_OFFER:
//                View basketOfferView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_basket_offer_item, parent, false);
//                offersViewHolder = new BasketItemOfferViewHolder(basketOfferView);
//                break;
//            case TYPE_CUSTOMER_OFFER:
        View customerOfferView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_customer_offer_item, parent, false);
        offersViewHolder = new BasketItemOfferViewHolder(customerOfferView);
//                break;
//        }
        return offersViewHolder;
    }

}
