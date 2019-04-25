package moshimoshi.cyplay.com.doublenavigation.utils;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.util.Locale;

/**
 * Created by romainlebouc on 14/10/16.
 */
public class PhoneUtils {

    public static String formatPhoneNumber(String phoneNumberToFormat){

        String formattedPhoneNumber;
        try {
            formattedPhoneNumber = PhoneNumberUtil.getInstance().format(PhoneNumberUtil.getInstance().parse(phoneNumberToFormat, Locale.getDefault().getCountry()),
                    PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
        } catch (Exception e) {
            formattedPhoneNumber =phoneNumberToFormat;
        }
        return formattedPhoneNumber;

    }
}
