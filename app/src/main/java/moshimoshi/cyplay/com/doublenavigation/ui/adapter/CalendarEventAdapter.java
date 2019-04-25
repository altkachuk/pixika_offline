package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;
import moshimoshi.cyplay.com.doublenavigation.model.business.EventSource;
import moshimoshi.cyplay.com.doublenavigation.model.custom.CalendarDayEvent;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.EventViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.IEventViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.MonthEventViewHolder;


/**
 * Created by romainlebouc on 19/04/16.
 */
public class CalendarEventAdapter extends RecyclerView.Adapter<IEventViewHolder> {
    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    private Context context;

    private List<CalendarDayEvent> events;

    public CalendarEventAdapter(Context ctx) {
        this.context = ctx;
        PlayRetailApp.get(ctx).inject(this);
    }

    public void setEvents(List<CalendarDayEvent> events) {
        this.events = events;
    }

    public CalendarDayEvent getItem(int position) {
        return events.get(position);
    }

    public List<CalendarDayEvent> getItems() {
        return events;
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        CalendarDayEvent e = this.getItem(position);
        return e.getEvent().eventSource;

    }

    @Override
    public IEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EventSource eventSource = EventSource.values()[viewType];
        switch (eventSource) {
            case REAL:
                View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_event, parent, false);
                return new EventViewHolder(v1);
            case META:
                View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_month_event, parent, false);
                return new MonthEventViewHolder(v2);

            default:
                return null;
        }

    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        //bus.register(this);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        //bus.unregister(this);
    }

    //@Subscribe
    //public void dateChosen(CalendarDateChosenEvent event) {
    //    this.refDate.setTime(event.getDay().getDate().getTime());
    //    this.notifyDataSetChanged();
    //}

    @Override
    public void onBindViewHolder(IEventViewHolder holder, int position) {
        CalendarDayEvent calendarDayEvent = events.get(position);
        if (calendarDayEvent != null) {
            holder.setCalendarDayEvent(calendarDayEvent);
        }
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }

    @Override
    public int getItemCount() {
        return events != null ? events.size() : 0;
    }

}
