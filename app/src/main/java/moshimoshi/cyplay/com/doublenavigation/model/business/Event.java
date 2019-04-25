package moshimoshi.cyplay.com.doublenavigation.model.business;

import android.util.Log;

import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ninja.cyplay.com.apilibrary.models.ModelResource;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AEvent;

/**
 * Created by romainlebouc on 26/04/16.
 */
@Parcel
@ModelResource
public class Event extends PR_AEvent {

    public transient final static SimpleDateFormat IN_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    //public final static int STRUCTURE_EVENT = 1;

    public Boolean one_day;
    public String description;
    public String start_date;
    public String end_date;
    public String summary;
    public String location;
    public List<Integer> related_hobby;
    public EventType type;
    public Integer eventSource = EventSource.REAL.ordinal();
    public EventRRule rrule;
    public List<EventMedia> medias;

    public Date getStartDate() {
        Date result = null;

        if (start_date != null) {
            try {
                result = IN_FORMAT.parse(start_date);
            } catch (ParseException e) {
                Log.e(Ticket.class.getName(), e.getMessage());
            }
        }
        return result;

    }

    public Date getEndDate() {
        Date result = null;
        if (end_date != null) {
            try {
                result = IN_FORMAT.parse(end_date);
            } catch (ParseException e) {
                Log.e(Ticket.class.getName(), e.getMessage());
            }
        }
        return result;

    }

    public EventSource getEventSource() {
        EventSource result = null;
        if (this.eventSource != null && this.eventSource >= 0 && this.eventSource < EventSource.values().length) {
            result = EventSource.values()[this.eventSource];
        }
        return result;

    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }

    public List<EventMedia> getMedias() {
        return medias;
    }

    public String getLocation() {
        return location;
    }
}
