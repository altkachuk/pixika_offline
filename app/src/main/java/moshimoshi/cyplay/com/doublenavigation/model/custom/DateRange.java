package moshimoshi.cyplay.com.doublenavigation.model.custom;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.utils.DateUtils;

/**
 * Created by romainlebouc on 28/04/16.
 */
public class DateRange {
    // Min date of the range
    public Date minDate = null;
    // Max date of the range
    public Date maxDate = null;

    public DateRange() {

    }

    public DateRange(Date minDate, Date maxDate) {
        this.minDate = minDate;
        this.maxDate = maxDate;
    }

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    public boolean isValid() {
        return this.minDate != null && this.maxDate != null && (this.minDate.equals(this.maxDate) || this.minDate.before(this.maxDate));
    }

    public DateRange setOneDateRange(Date date) {
        this.minDate = date;
        this.maxDate = date;
        return this;
    }

    public DateRange extendsRange(int year) {
        if (isValid()) {
            this.minDate = DateUtils.getYearsDifference(this.minDate, -year);
            this.maxDate = DateUtils.getYearsDifference(this.maxDate, year);
        }
        return this;
    }

    public DateRange extendsRangeInTheFuture(int year) {
        DateRange extension = null;
        if (isValid()) {
            Date newMaxDate = DateUtils.getYearsDifference(this.maxDate, year);
            extension = new DateRange((Date) this.maxDate.clone(), newMaxDate);
            this.maxDate = newMaxDate;
        }
        return extension;
    }

    public DateRange extendsRangeInThePast(int year) {
        DateRange extension = null;
        if (isValid()) {
            Date newMinDate = DateUtils.getYearsDifference(this.minDate, -year);
            extension = new DateRange(newMinDate, (Date) this.minDate.clone());
            this.minDate = newMinDate;
        }
        return extension;
    }


    /**
     * Check if a date is in the range
     *
     * @param date
     * @return
     */
    public boolean isInRange(Date date) {
        return this.isValid() && (date.equals(this.getMinDate()) || date.after(this.getMinDate()))
                && date.before(this.getMaxDate());
    }

    /**
     * Get a list of date within the range for a yearly recurrent date
     *
     * @param yearlyDate
     * @return
     */
    public List<Date> getYearlyDateInRange(Date yearlyDate) {
        List<Date> result = new ArrayList<>();
        if (yearlyDate != null && minDate != null && maxDate != null && minDate.before(maxDate)) {
            Calendar minCalendar = Calendar.getInstance();
            minCalendar.setTime(minDate);

            Calendar maxCalendar = Calendar.getInstance();
            maxCalendar.setTime(maxDate);

            Calendar c = Calendar.getInstance();
            c.setTime(yearlyDate);
            c.set(Calendar.YEAR, minCalendar.get(Calendar.YEAR) - 1);

            while (c.before(maxCalendar)) {
                if (c.equals(minCalendar) || c.after(minCalendar)) {
                    result.add(c.getTime());

                }
                c.set(Calendar.YEAR, c.get(Calendar.YEAR) + 1);
            }

        }
        return result;
    }


}
