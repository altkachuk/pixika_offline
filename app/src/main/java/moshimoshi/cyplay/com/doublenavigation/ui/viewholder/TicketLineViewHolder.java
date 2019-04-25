package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;


import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.Ticket;
import moshimoshi.cyplay.com.doublenavigation.model.business.TicketLine;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.ProductWithSelectorsActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.ProductItemCore;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;

/**
 * Created by damien on 13/04/16.
 */
public class TicketLineViewHolder extends ItemViewHolder<TicketLine> {

    @Inject
    ConfigHelper configHelper;

    @Inject
    CustomerContext customerContext;

    @BindView(R.id.product_item_core)
    ProductItemCore productItemCore;

    @BindView(R.id.item_quantity)
    TextView itemQuantity;

    @BindView(R.id.item_unit_price)
    TextView itemUnitPrice;

    private Ticket parentTicket;
    private TicketLine ticketLine;

    public TicketLineViewHolder(View view) {
        super(view);
    }

    public void setItem(TicketLine ticketLine, Ticket parentTicket) {
        this.parentTicket = parentTicket;
        this.setItem(ticketLine);
    }

    @Override
    public void setItem(TicketLine ticketLine) {
        this.ticketLine = ticketLine;
        if (ticketLine != null) {
            Product product = ticketLine.getProduct();
            productItemCore.fillProductItem(product, ticketLine.getSku_id());
        }
        itemQuantity.setText(ticketLine.getQty() != null ? this.context.getResources().getString(R.string.item_quantity, ticketLine.getQty().intValue()) : null);
        itemUnitPrice.setText(configHelper.formatPrice(parentTicket.getShop(), ticketLine.getUnit_price()));
    }

    @Nullable
    @OnClick(R.id.product_item_cell_content)
    public void onProductClick() {
        if (ticketLine != null && ticketLine.getProduct() != null) {
            Intent intent = new Intent(context, ProductWithSelectorsActivity.class);
            intent.putExtra(IntentConstants.EXTRA_PRODUCT_NAME, ticketLine.getProduct().getName());
            intent.putExtra(IntentConstants.EXTRA_PRODUCT_ID, ticketLine.getProduct().getId());
            intent.putExtra(IntentConstants.EXTRA_SKU_ID, ticketLine.getSku_id());
            context.startActivity(intent);
        }
    }

}
