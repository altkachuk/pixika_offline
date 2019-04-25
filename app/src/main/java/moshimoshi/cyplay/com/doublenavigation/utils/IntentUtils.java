package moshimoshi.cyplay.com.doublenavigation.utils;

import android.content.Intent;

import org.parceler.Parcels;

/**
 * Created by damien on 28/04/16.
 */
public class IntentUtils {

    public static <T> T getExtraFromIntent(Intent intent, String extraKey) {
        if (intent.hasExtra(extraKey))
            return Parcels.unwrap(intent.getParcelableExtra(extraKey));
        return null;
    }
}
