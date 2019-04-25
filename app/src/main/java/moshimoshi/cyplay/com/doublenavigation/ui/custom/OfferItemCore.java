package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Offer;

/**
 * Created by wentongwang on 04/04/2017.
 */

public class OfferItemCore extends LinearLayout {

    private Context context;
    private final DateFormat offersDateFormat;

    @BindView(R.id.offer_data)
    TextView tvOfferData;

    @BindView(R.id.offer_name)
    TextView tvOfferName;

    @BindView(R.id.offer_description)
    TextView tvOfferDescription;

    public OfferItemCore(Context context) {
        this(context, null);
    }

    public OfferItemCore(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OfferItemCore(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        offersDateFormat = DateFormat.getDateInstance(DateFormat.SHORT, this.context.getResources().getConfiguration().locale);
        initView();
    }

    private void initView(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.cell_offers_item_core, this, true);
        ButterKnife.bind(this);
    }

    public void fillOffer(Offer offer){
        if (offer.getFrom_date() == null || offer.getTo_date() == null) {
            tvOfferData.setVisibility(GONE);
        }else {
            tvOfferData.setVisibility(VISIBLE);
            tvOfferData.setText(context.getResources().getString(R.string.customer_offer_from_and_to_date,
                    offersDateFormat.format(offer.getFrom_date()),
                    offersDateFormat.format(offer.getTo_date())));
        }
        if (offer.getName().isEmpty())
            tvOfferName.setVisibility(GONE);
        else {
            tvOfferName.setVisibility(VISIBLE);
            tvOfferName.setText(offer.getName());
        }

        if (offer.getDescription().isEmpty())
            tvOfferDescription.setVisibility(GONE);
        else{
            tvOfferDescription.setVisibility(VISIBLE);
            tvOfferDescription.setText(offer.getDescription());
        }
    }

    public void hideOfferName(){
        tvOfferName.setVisibility(GONE);
    }

}
