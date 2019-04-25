
package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerOfferState;
import moshimoshi.cyplay.com.doublenavigation.model.business.Offer;
import moshimoshi.cyplay.com.doublenavigation.presenter.BasketPresenter;
import moshimoshi.cyplay.com.doublenavigation.utils.DateUtils;

/**
 * Created by damien on 13/04/16.
 */
public class OfferViewHolder extends ItemViewHolder<CustomerOfferState> {

    @Nullable
    @BindView(R.id.offer_content)
    public TextView content;

    @Nullable
    @BindView(R.id.offer_date)
    public TextView date;

    @Nullable
    @BindView(R.id.offer_quantity)
    public TextView quantity;

    private CustomerOfferState customerOfferState;
    private BasketPresenter presenter;

    public OfferViewHolder(View view, BasketPresenter presenter) {
        super(view);
        this.presenter = presenter;
    }

    @Override
    public void setItem(CustomerOfferState customerOfferState) {
        this.customerOfferState = customerOfferState;
        if (customerOfferState != null && customerOfferState.getOffer() != null) {
            Offer offer = customerOfferState.getOffer();
            content.setText(offer.getName());
            String dateStringBuilder = "Du : " +
                    DateUtils.formatDate("dd MMM yyyy", offer.getFrom_date()) +
                    " " +
                    "Au : " +
                    DateUtils.formatDate("dd MMM yyyy", offer.getTo_date());
            date.setText(dateStringBuilder);

        }
    }
}
