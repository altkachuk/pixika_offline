package moshimoshi.cyplay.com.doublenavigation.model.business.parcel;

import org.parceler.Parcel;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.Ticket;

/**
 * Created by damien on 08/06/16.
 */
@Parcel
public class TicketListParcelWrapper {

    List<Ticket> tickets;

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

}
