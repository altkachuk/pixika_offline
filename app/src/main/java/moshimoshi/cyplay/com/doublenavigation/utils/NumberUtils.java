package moshimoshi.cyplay.com.doublenavigation.utils;

/**
 * Created by romainlebouc on 16/05/16.
 */
public class NumberUtils {

    /**
     * Format a quantity without decimal if not needed
     *
     * @param d
     * @return
     */
    public static String quantityFormat(double d) {
        if (d == (long) d)
            return String.format("%d", (long) d);
        else
            return String.format("%s", d);
    }

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
}
