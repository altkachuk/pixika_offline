package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.custom.CalendarDayEvent;

/**
 * Created by romainlebouc on 27/04/16.
 */
public class MonthEventViewHolder extends IEventViewHolder {


    @Nullable
    @BindView(R.id.event_title)
    public TextView eventTitle;

    @Nullable
    @BindView(R.id.meta_event_background)
    public LinearLayout background;

    private CalendarDayEvent calendarDayEvent;

    public MonthEventViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
        this.background.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimaryDark));
    }

    @Override
    public void setCalendarDayEvent(CalendarDayEvent calendarDayEvent) {
        this.calendarDayEvent = calendarDayEvent;
        this.eventTitle.setText(calendarDayEvent.getEvent().getSummary());
    }

}
