package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.TicketLine;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.TicketLineViewHolder;


/**
 * Created by romainlebouc on 04/08/16.
 */
public class CustomerTicketLinesAdapter extends ItemAdapter<TicketLine, TicketLineViewHolder> {

    public CustomerTicketLinesAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public TicketLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_sale_history, parent, false);
        return new TicketLineViewHolder(v);
    }

    @Override
    public List<TicketLine> getItems() {
        return items;
    }

    @Override
    public void setItems(List<TicketLine> ticketLines) {
        this.items = ticketLines;
    }
}
