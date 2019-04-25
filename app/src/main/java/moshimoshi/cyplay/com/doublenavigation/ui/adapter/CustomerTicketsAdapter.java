package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Ticket;
import moshimoshi.cyplay.com.doublenavigation.model.business.TicketLine;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.TicketLineViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.TicketViewHolder;

/**
 * Created by romainlebouc on 02/02/2017.
 */

public class CustomerTicketsAdapter extends AbstractExpandableItemAdapter<TicketViewHolder, TicketLineViewHolder> {

    private Context context;
    private List<Ticket> tickets;

    public CustomerTicketsAdapter(Context context) {
        this.context = context;
        setHasStableIds(true);
    }

    public void setItems(List<Ticket> tickets) {
        this.tickets = tickets;
        this.notifyDataSetChanged();
    }

    public List<Ticket> getItems() {
        return tickets;
    }

    @Override
    public int getGroupCount() {
        return tickets != null ? tickets.size() : 0;
    }

    @Override
    public int getChildCount(int groupPosition) {
        if (tickets != null) {
            Ticket ticket = tickets.get(groupPosition);
            return ticket.getItems() == null ? 0 : ticket.getItems().size();
        } else {
            return 0;
        }
    }

    @Override
    public long getGroupId(int groupPosition) {
        // This method need to return unique value within all group items.
        return tickets != null ? tickets.get(groupPosition).hashCode() : 0l;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // This method need to return unique value within the group.
        if (tickets != null) {
            Ticket ticket = tickets.get(groupPosition);
            return ticket.getItems() == null ? 0 : ticket.getItems().get(childPosition).hashCode();
        } else {
            return 0;
        }
    }

    @Override
    public TicketViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public TicketLineViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_sale_history, parent, false);
        return new TicketLineViewHolder(view);
    }

    @Override
    public void onBindGroupViewHolder(TicketViewHolder holder, int groupPosition, int viewType) {
        Ticket ticket = tickets.get(groupPosition);
        if (ticket != null) {
            holder.setItem(ticket);
        }
    }

    @Override
    public void onBindChildViewHolder(TicketLineViewHolder holder, int groupPosition, int childPosition, int viewType) {
        TicketLine ticketLine = tickets.get(groupPosition).getItems().get(childPosition);
        if (ticketLine != null) {
            holder.setItem(ticketLine, tickets.get(groupPosition));
        }
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(TicketViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        return true;
    }

}
