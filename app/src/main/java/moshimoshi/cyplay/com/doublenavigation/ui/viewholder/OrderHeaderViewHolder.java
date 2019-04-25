package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;
import moshimoshi.cyplay.com.doublenavigation.model.business.Ticket;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.DateUtils;

/**
 * Created by damien on 06/06/16.
 */
public class OrderHeaderViewHolder extends ATicketViewHolder {

    @Inject
    ConfigHelper configHelper;

    @Nullable
    @BindView(R.id.container)
    LinearLayout background;

    @Nullable
    @BindView(R.id.ticket_date)
    TextView ticketDate;

    @Nullable
    @BindView(R.id.ticket_total)
    TextView total;

    @Nullable
    @BindView(R.id.ticket_id)
    TextView ticketId;

    private Context context;

    public OrderHeaderViewHolder(View view) {
        super(view);
        this.context = view.getContext();
        PlayRetailApp.get(context).inject(this);
        ButterKnife.bind(this, view);
        this.background.setBackgroundColor(ContextCompat.getColor(context, R.color.grey));
    }

    @Override
    public void setTicketLine(Ticket ticket, int position) {
        if (ticket != null) {
            ticketDate.setText(DateUtils.formatDate("dd MMMM yyyy", ticket.getPurchaseDate()));
            total.setText(configHelper.formatPrice(ticket.getTotal()));
            ticketId.setText(ticket.getId());
        }
    }

}
