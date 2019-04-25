package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.custom.CalendarDayEvent;
import moshimoshi.cyplay.com.doublenavigation.utils.CompatUtils;

/**
 * Created by damien on 15/04/16.
 */
public class TimelineItemViewHolder extends RecyclerView.ViewHolder {

    @Nullable
    @BindView(R.id.event_date)
    TextView date;

    @Nullable
    @BindView(R.id.event_title)
    TextView title;

    @Nullable
    @BindView(R.id.timeline_background_top)
    View backgroundTop;

    @Nullable
    @BindView(R.id.timeline_background_bottom)
    View backgroundBottom;

    @Nullable
    @BindView(R.id.cell_customer_timeline_container)
    FrameLayout cellContainer;

    private final static String EVENTS_DATE_FORMAT_PATTERN = "dd MMM yy";
    private final static SimpleDateFormat GET_EVENTS_DATE_FORMAT = new SimpleDateFormat(EVENTS_DATE_FORMAT_PATTERN);

    private CalendarDayEvent calendarDayEvent;
    private int position;

    private final static int[] BACKGROUND_COLORS = {Color.parseColor("#ec407a"),
            Color.parseColor("#8bc34a"),
            Color.parseColor("#7e57c2"),
            Color.parseColor("#4db6ac"),
            Color.parseColor("#e65100")};

    public TimelineItemViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);


    }

    public void setCalendarDayEvent(CalendarDayEvent calendarDayEvent, int position) {
        this.calendarDayEvent = calendarDayEvent;
        this.position = position;
        this.date.setText(calendarDayEvent != null ? GET_EVENTS_DATE_FORMAT.format(calendarDayEvent.getCalendarDay().getDate()) : "");
        this.title.setText(calendarDayEvent != null ? calendarDayEvent.getEvent().getSummary().toUpperCase() : "");

        CompatUtils.setDrawableTint(backgroundTop.getBackground(), BACKGROUND_COLORS[position % BACKGROUND_COLORS.length]);
        CompatUtils.setDrawableTint(backgroundBottom.getBackground(), BACKGROUND_COLORS[position % BACKGROUND_COLORS.length]);

    }
}
