package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import moshimoshi.cyplay.com.doublenavigation.model.business.Ticket;

/**
 * Created by romainlebouc on 16/05/16.
 */
public abstract class ATicketViewHolder extends RecyclerView.ViewHolder {

    public ATicketViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void setTicketLine(Ticket ticket, int position);
}
