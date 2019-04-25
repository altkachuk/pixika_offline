package moshimoshi.cyplay.com.doublenavigation.utils;

import android.content.Context;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by damien on 30/03/16.
 */
public class StringUtils {

    public final static String EMPTY_STRING = "";
    /*private static Context context;

    private static String packageName;

    public static void setContext(Context ctx) {
        context = ctx;
        packageName = ctx.getPackageName();
    }*/

    public static String getStringResourceByName(Context context,String aString) {
        try {
            int resId = context.getResources().getIdentifier(aString, "string", context.getPackageName());
            return context.getString(resId);
        } catch (Exception e) {
            Log.e(StringUtils.class.getName(),e.getMessage(),e);
        }
        return aString;
    }

    public static String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = "";
        }

        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    public static String getEmptyIfNull(String input) {
        return input != null ? input : "";
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }

        if (str.isEmpty()) {
            return false;
        }

        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public static String getPrecentStr(String input) {
        try {
            double value = Double.parseDouble(input);
            DecimalFormat df = new DecimalFormat("0%");
            return df.format(value);
        } catch (Exception e) {
            return "";
        }
    }

    public static Double getDoubleFromStr(String input) {
        try {
            return Double.parseDouble(input);
        } catch (Exception e) {
            return null;
        }
    }

}
