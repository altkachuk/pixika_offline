package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.PurchaseCollection;

/**
 * Created by romainlebouc on 27/01/2017.
 */

public class PurchaseCollectionViewHolder extends ItemViewHolder<PurchaseCollection> {

    @BindView(R.id.purchase_collection_label)
    TextView label;

    @BindView(R.id.delivery_mode_name)
    TextView deliveryModeName;

    public PurchaseCollectionViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setItem(PurchaseCollection purchaseCollection) {
        label.setText(purchaseCollection.getEPurchaseCollectionMode().getLabelId());
        if (purchaseCollection.getDelivery_mode() != null) {
            deliveryModeName.setText(purchaseCollection.getDelivery_mode().getName());
        } else {
            deliveryModeName.setText("");
        }

    }
}
