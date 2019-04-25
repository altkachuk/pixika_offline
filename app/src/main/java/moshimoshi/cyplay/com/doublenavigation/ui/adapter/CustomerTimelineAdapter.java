package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Event;
import moshimoshi.cyplay.com.doublenavigation.model.custom.CalendarDayEvent;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.TimelineItemViewHolder;

/**
 * Created by damien on 15/04/16.
 */
public class CustomerTimelineAdapter extends RecyclerView.Adapter<TimelineItemViewHolder> {


    private final static String EVENTS_DATE_FORMAT_PATTERN = "dd MMM yyyy";
    private final static SimpleDateFormat GET_EVENTS_DATE_FORMAT = new SimpleDateFormat(EVENTS_DATE_FORMAT_PATTERN);

    private Context context;

    private List<CalendarDayEvent> items;

    public CustomerTimelineAdapter(Context ctx) {
        this.context = ctx;
    }

    public void setItems(List<CalendarDayEvent> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public TimelineItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_customer_timeline_item, parent, false);
        return new TimelineItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TimelineItemViewHolder holder, int position) {
        CalendarDayEvent item = items.get(position);
        if (item != null) {
            Event event = item.getEvent();
            if (event !=null){
                holder.setCalendarDayEvent(item, position);
            }
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }
}
