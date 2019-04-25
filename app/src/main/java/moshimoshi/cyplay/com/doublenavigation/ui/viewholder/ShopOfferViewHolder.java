package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.IOffer;
import moshimoshi.cyplay.com.doublenavigation.model.business.Offer;


/**
 * Created by romainlebouc on 02/09/16.
 */
public class ShopOfferViewHolder extends IEmptyOrItemViewHolder<IOffer> {

    @Nullable
    @BindView(R.id.offer_id)
    TextView offerId;

    @Nullable
    @BindView(R.id.offer_type)
    TextView offerType;

    @Nullable
    @BindView(R.id.offer_period)
    TextView offerPeriod;

    @Nullable
    @BindView(R.id.offer_name)
    TextView offerName;

    @Nullable
    @BindView(R.id.offer_description)
    TextView offerDescription;

    @Nullable
    @BindView(R.id.offer_icon)
    ImageView offerIcon;

    private Context context;
    private final DateFormat dateFormat;


    public ShopOfferViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = itemView.getContext();
        dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, context.getResources().getConfiguration().locale);
    }

    @Override
    public void setItem(IOffer iOffer) {
        Offer offer = iOffer.getOffer();
        if (offer != null) {
            offerId.setText(offer.getId());
            offerName.setText(offer.getName());
            if (offer.getType() != null) {
                offerType.setText(context.getString(R.string.shop_offer_type,
                        context.getString(offer.getType().getLabelId())));
            } else
                offerType.setText(context.getString(R.string.shop_offer_type));

            String period = null;
            if (offer.getFrom_date() != null && offer.getTo_date() != null) {
                period = this.context.getString(R.string.period_from_to,
                        dateFormat.format(offer.getFrom_date()),
                        dateFormat.format(offer.getTo_date()));

            } else if (offer.getTo_date() != null) {
                period = this.context.getString(R.string.period_to, dateFormat.format(offer.getTo_date()));
            }
            offerPeriod.setText(period);

            if (offerIcon != null && offer.getType() != null && offer.getType().getIconId() > 0) {
                Picasso.get().load(offer.getType().getIconId()).into(offerIcon);
            }

        }
    }

}
