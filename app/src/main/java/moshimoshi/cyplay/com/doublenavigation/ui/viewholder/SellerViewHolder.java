package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Seller;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.PicturedBadge;

/**
 * Created by romainlebouc on 20/04/16.
 */
public class SellerViewHolder extends ItemViewHolder<Seller> {

    @Nullable
    @BindView(R.id.seller_profile_view)
    public PicturedBadge picturedBadge;

    @Nullable
    @BindView(R.id.seller_first_name)
    public TextView firstName;

    @Nullable
    @BindView(R.id.seller_last_name)
    public TextView lastName;

    public SellerViewHolder(View view) {
        super(view);
        if (firstName != null) {
            firstName.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimaryDark));
        }
        if (lastName != null) {
            lastName.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimaryDark));
        }
    }

    @Override
    public void setItem(Seller seller) {
        if (seller != null) {
            if (picturedBadge != null) {
                this.picturedBadge.setProfile(seller);
            }
            if (firstName != null) {
                this.firstName.setText(seller.getFirst_name());
            }
            if (lastName != null) {
                this.lastName.setText(seller.getLast_name());
            }
        }

    }

}
