package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Seller;
import moshimoshi.cyplay.com.doublenavigation.model.business.Shop;
import moshimoshi.cyplay.com.doublenavigation.model.business.Ticket;

/**
 * Created by romainlebouc on 10/06/16.
 */
public class TicketViewHolder extends ItemViewHolder<Ticket> {

    private final DateFormat ticketDateFormat;


    @Nullable
    @BindView(R.id.customer_ticket_date)
    TextView ticketDate;

    @Nullable
    @BindView(R.id.customer_ticket_shop_name)
    TextView ticketShopName;

    @Nullable
    @BindView(R.id.customer_ticket_seller_name)
    TextView ticketSellerName;

    private Ticket ticket;

    public TicketViewHolder(View itemView) {
        super(itemView);
        ticketDateFormat = DateFormat.getDateInstance(DateFormat.SHORT, this.context.getResources().getConfiguration().locale);
    }

    @Override
    public void setItem(Ticket ticket) {
        this.ticket = ticket;
        if (ticketDate != null) {
            ticketDate.setText(this.ticket.getPurchaseDate() != null ? ticketDateFormat.format(this.ticket.getPurchaseDate()) : "");
        }
        if (ticketShopName != null) {
            Shop shop = ticket.getShop();
            if (shop != null) {
                ticketShopName.setText(shop.getName(true));
            } else {
                setUndefined(ticketShopName);
            }

        }
        if (ticketSellerName != null) {
            Seller seller = ticket.getSeller();
            if (seller != null) {
                ticketSellerName.setText(seller.getFormatName());
                ticketSellerName.setTextColor(ContextCompat.getColor(this.context, R.color.VeryDarkGray));
            } else {
                setUndefined(ticketSellerName);
            }

        }
    }

    public void setUndefined(TextView textView) {
        textView.setText(R.string.undefined_attribute);
        textView.setTextColor(ContextCompat.getColor(this.context, R.color.LightGrey));
    }


}
