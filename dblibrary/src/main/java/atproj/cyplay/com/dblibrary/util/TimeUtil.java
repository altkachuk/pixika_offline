package atproj.cyplay.com.dblibrary.util;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by andre on 22-Aug-18.
 */

public class TimeUtil {

    static public String generateUid() {
        DecimalFormat format = new DecimalFormat("00");

        Calendar d = Calendar.getInstance();
        String result = String.valueOf(d.get(Calendar.YEAR))
                + format.format(d.get(Calendar.MONTH))
                + format.format(d.get(Calendar.DAY_OF_MONTH))
                + format.format(d.get(Calendar.HOUR_OF_DAY))
                + format.format(d.get(Calendar.MINUTE))
                + format.format(d.get(Calendar.SECOND));

        return result;
    }

    static public String generateCurrentTimePrefix() {
        DecimalFormat format = new DecimalFormat("00");

        Calendar d = Calendar.getInstance();
        String result = String.valueOf(d.get(Calendar.YEAR))
                + "_" + format.format((d.get(Calendar.MONTH) + 1))
                + "_" + format.format(d.get(Calendar.DAY_OF_MONTH))
                + "_" + format.format(d.get(Calendar.HOUR_OF_DAY))
                + "_" + format.format(d.get(Calendar.MINUTE))
                + "_" + format.format(d.get(Calendar.SECOND));

        return result;
    }
}
