package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import moshimoshi.cyplay.com.doublenavigation.model.custom.CalendarDayEvent;

/**
 * Created by romainlebouc on 27/04/16.
 */
public abstract class IEventViewHolder extends RecyclerView.ViewHolder {

    public IEventViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void setCalendarDayEvent(CalendarDayEvent calendarDayEvent);

}
