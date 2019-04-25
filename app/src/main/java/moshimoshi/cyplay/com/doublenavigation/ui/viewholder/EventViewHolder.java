package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;


import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.custom.CalendarDayEvent;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.CalendarReportActivity;
import moshimoshi.cyplay.com.doublenavigation.utils.DateUtils;

/**
 * Created by damien on 13/04/16.
 */
public class EventViewHolder extends IEventViewHolder {

    @Nullable
    @BindView(R.id.event_cell_header)
    LinearLayout header;

    @Nullable
    @BindView(R.id.event_title)
    public TextView eventTitle;

    @Nullable
    @BindView(R.id.event_desc)
    public TextView eventDescription;

    @Nullable
    @BindView(R.id.event_date)
    public TextView eventDate;

    @Nullable
    @BindView(R.id.event_location)
    public TextView eventLocation;

    @Nullable
    @BindView(R.id.event_location_icon)
    public ImageView eventLocationIcon;

    @Nullable
    @BindView(R.id.event_report_button)
    public Button reportButton;

    @Nullable
    @BindView(R.id.event_done_button)
    public Button doneButton;


    private CalendarDayEvent calendarDayEvent;

    public EventViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
        if (reportButton != null) {
            reportButton.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimaryDark));
        }

        doneButton.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimaryDark));
    }

    @Nullable
    @OnClick(R.id.event_report_button)
    public void onEventReportClick() {
        Intent intent = new Intent(this.itemView.getContext(), CalendarReportActivity.class);
        this.itemView.getContext().startActivity(intent);
    }

    public void setCalendarDayEvent(CalendarDayEvent calendarDayEvent) {

        this.calendarDayEvent = calendarDayEvent;

        if (eventTitle != null) {
            eventTitle.setText(calendarDayEvent.getEvent().summary);
        }


        if (eventDescription != null) {
            eventDescription.setText(calendarDayEvent.getEvent().description);
        }

        Date date = calendarDayEvent.getCalendarDay().getDate();
        if (date != null) {
            this.eventDate.setText(DateUtils.formatDate("dd MMMM yyyy", date));
        }

        if (eventLocation != null) {
            eventLocation.setText(calendarDayEvent.getEvent().location);
        }

    }
}
