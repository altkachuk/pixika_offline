package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Category;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sale;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.DateUtils;

/**
 * Created by damien on 20/04/16.
 */
public class SaleViewHolder extends ItemViewHolder<Sale> {

    @Inject
    ConfigHelper configHelper;

    @Nullable
    @BindView(R.id.name_view)
    public TextView name;

    @Nullable
    @BindView(R.id.creation_date_view)
    public TextView creationdDate;

    @Nullable
    @BindView(R.id.total_view)
    public TextView total;

    @Nullable
    @BindView(R.id.category_loading)
    public ProgressBar loading;

    public SaleViewHolder(View view) {
        super(view);
    }

    @Override
    public void setItem(Sale sale) {
        if (name != null) {
            name.setText(sale.getCustomer().getDetails().getFirst_name() + " " + sale.getCustomer().getDetails().getLast_name());
        }
        if (creationdDate != null) {
            creationdDate.setText(DateUtils.formatDate("HH:mm:ss dd MMM yyyy", sale.getBasket().getCreation_date()));
        }
        if (total != null) {
            total.setText(configHelper.formatPrice(sale.getBasket().getTotal()));
        }
    }

}