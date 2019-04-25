package moshimoshi.cyplay.com.doublenavigation.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by wentongwang on 12/06/2017.
 */

public class CountryCodeConverter {

    private static CountryCodeConverter instance = null;

    private Map<String, Locale> localeMap;

    private CountryCodeConverter() {

    }

    private synchronized void init() {
        String[] countries = Locale.getISOCountries();
        localeMap = new HashMap<>(countries.length);
        for (String country : countries) {
            Locale locale = new Locale("", country);
            localeMap.put(locale.getISO3Country().toUpperCase(), locale);
        }
    }

    public static CountryCodeConverter getInstacne() {
        if (instance == null) {
            instance = new CountryCodeConverter();
            instance.init();
        }

        return instance;
    }

    public String iso3CountryCodeToIso2CountryCode(String iso3CountryCode) {
        return localeMap.get(iso3CountryCode).getCountry();
    }

    public String iso2CountryCodeToIso3CountryCode(String iso2CountryCode) {
        Locale locale = new Locale("", iso2CountryCode);
        return locale.getISO3Country();
    }

}
