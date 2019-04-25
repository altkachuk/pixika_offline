package moshimoshi.cyplay.com.doublenavigation.utils;


import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ninja.cyplay.com.apilibrary.utils.LogUtils;

/**
 * Created by romainlebouc on 19/04/16.
 */
public class DateUtils {

    public transient final static SimpleDateFormat IN_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public static String formatDate(String dateFormat, Date date) {
        if (date != null) {
            DateFormat df = new SimpleDateFormat(dateFormat);
            return df.format(date);
        }
        return "";
    }

    public static String formatDate(String dateFormat, String date) {
        return formatDate(dateFormat, parseDate(date));
    }

    public static Date parseDate(String date) {
        Date result = null;
        if (date != null && date.length() > 0) {
            try {
                result = IN_FORMAT.parse(date);
            } catch (ParseException e) {
                Log.e(LogUtils.generateTag(DateUtils.class), e.getMessage());
            }
        }
        return result;
    }

    public static long getTimeStampFromString(String dateString) {
        Date date = parseDate(dateString);
        if (date != null)
            return date.getTime();
        return 0L;
    }

    public static boolean sameDay(Date date1, Date date2) {
        boolean sameDay = false;
        if (date1 != null && date2 != null) {
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(date1);
            cal2.setTime(date2);
            sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

        }
        return sameDay;

    }

    public static Date getFirstDayOfCurrentMonth() {
        Calendar c = Calendar.getInstance();   // this takes current date
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    public static Date getCurrentDay() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date getYearsDifference(Date date, int diff) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.YEAR, c.get(Calendar.YEAR) + diff);
        return c.getTime();

    }

    public static List<Date> getYearlyDateBetweenTwoDate(Date yearlyDate, Date minDate, Date maxDate) {
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
                    c.set(Calendar.YEAR, c.get(Calendar.YEAR) + 1);
                }
            }

        }
        return result;

    }


}
