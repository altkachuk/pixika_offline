package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;


import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Ticket;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.CustomerTicketLinesAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.SpaceItemDecoration;

/**
 * Created by romainlebouc on 10/06/16.
 */
public class TicketWithProductsViewHolder extends ItemViewHolder<Ticket> {


    private final DateFormat ticketDateFormat;


    @BindView(R.id.customer_ticket_date)
    TextView ticketDate;

    @BindView(R.id.customer_ticket_shop)
    TextView ticketShop;

    @BindView(R.id.customer_ticket_lines)
    RecyclerView ticketLinesRecyclerView;

    private Ticket ticket;

    CustomerTicketLinesAdapter customerTicketLinesAdapter;

    public TicketWithProductsViewHolder(View itemView) {
        super(itemView);
        ticketDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, this.context.getResources().getConfiguration().locale);
        customerTicketLinesAdapter = new CustomerTicketLinesAdapter(context);
        initRecyclerView();
    }

    @Override
    public void setItem(Ticket ticket) {
        this.ticket = ticket;
        ticketDate.setText(ticket.getPurchaseDate() != null ? ticketDateFormat.format(ticket.getPurchaseDate()).toUpperCase() : "");
        ticketShop.setText(ticket.getShop() != null ? ticket.getShop().getShort_name() : "");
        customerTicketLinesAdapter.setItems(this.ticket.getItems());
        customerTicketLinesAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        // optimization
        ticketLinesRecyclerView.setHasFixedSize(true);
        // set layout
        ticketLinesRecyclerView.setLayoutManager(layoutManager);
        ticketLinesRecyclerView.setAdapter(customerTicketLinesAdapter);

        int spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, this.context.getResources().getDisplayMetrics());
        ticketLinesRecyclerView.addItemDecoration(new SpaceItemDecoration(spacing));

        ticketLinesRecyclerView.setNestedScrollingEnabled(false);
    }
}
