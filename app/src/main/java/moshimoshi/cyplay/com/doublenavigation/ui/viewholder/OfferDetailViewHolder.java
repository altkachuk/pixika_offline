package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketOffer;

/**
 * Created by wentongwang on 18/04/2017.
 */

public class OfferDetailViewHolder extends ItemViewHolder<BasketOffer> {

    @BindView(R.id.offer_name)
    TextView offerName;

    @BindView(R.id.offer_description)
    TextView offerDescription;

    public OfferDetailViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setItem(BasketOffer offer) {
        offerName.setText(offer.getOffer().getName());
        offerDescription.setText(offer.getOffer().getDescription());
    }
}
