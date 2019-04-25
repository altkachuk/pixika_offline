package moshimoshi.cyplay.com.doublenavigation.ui.component;

import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by romainlebouc on 19/01/2017.
 */

public class PlayRetailViewClient extends WebViewClient {

    private String whiteListRegexp;

    public PlayRetailViewClient() {
    }

    public void setWhiteListRegexp(String whiteListRegexp) {
        this.whiteListRegexp = whiteListRegexp;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        if (isAuthorizedAddress(url)) {
            view.loadUrl(url);
        }

        return true;
    }

    public boolean isAuthorizedAddress(String url) {
        try {
            URL url1 = new URL(url);
            String path = url1.getHost();

            if (whiteListRegexp != null) {
                Pattern p = Pattern.compile(whiteListRegexp);
                Matcher m = p.matcher(path);
                return m.matches();
            } else {
                return true;
            }


        } catch (MalformedURLException e) {
            Log.e(PlayRetailViewClient.class.getName(), e.getMessage(), e);
        }
        return false;
    }

}