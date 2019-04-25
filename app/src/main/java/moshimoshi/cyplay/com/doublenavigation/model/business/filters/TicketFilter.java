package moshimoshi.cyplay.com.doublenavigation.model.business.filters;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import ninja.cyplay.com.apilibrary.models.filters.ResourceFilterValue;
import ninja.cyplay.com.apilibrary.models.filters.ResourceFilter;

/**
 * Created by romainlebouc on 18/09/16.
 */
@Parcel
public class TicketFilter extends ResourceFilter<TicketFilter,TicketFilterValue> {


    public List<TicketFilterValue> values;

    @Override
    public ResourceFilter copyWithValue(ResourceFilterValue filterValue) {
        TicketFilter filter = new TicketFilter();
        filter.label = this.label;
        filter.key = this.key;
        filter.level = this.level;
        filter.setValues(new ArrayList<TicketFilterValue>());
        filter.getValues().add((TicketFilterValue)filterValue);
        return filter;
    }

    public List<TicketFilterValue> getValues() {
        return values;
    }

    public void setValues(List<TicketFilterValue> values) {
        this.values = values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TicketFilter that = (TicketFilter) o;

        if (label != null ? !label.equals(that.label) : that.label != null) return false;
        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        return level != null ? level.equals(that.level) : that.level == null;

    }


    /*public static List<Ticket> filterTickets(List<Ticket> tickets, List<TicketFilter> ticketFilters){
        List<Ticket> result = new ArrayList<>(tickets);
        if (ticketFilters != null && !ticketFilters.isEmpty()) {
            for (final TicketFilter filter : ticketFilters) {
                if (filter.getValues() != null) {
                    for (final TicketFilterValue filterValue : filter.getValues()) {
                        // do filtering over the same list
                        result = FluentIterable.from(result).filter(new Predicate<Ticket>() {
                            public boolean apply(Ticket input) {
                                return filterValue.getKey().equals(ReflectionUtils.get(input, filter.getKey()));
                            }
                        }).toList();
                    }
                }

            }
        }
        // Because FluentIterable is sending back a immutable List
        return  new ArrayList(result);
    }*/
}