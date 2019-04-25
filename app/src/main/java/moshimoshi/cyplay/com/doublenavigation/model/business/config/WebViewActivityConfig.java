package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

/**
 * Created by romainlebouc on 19/01/2017.
 */
@Parcel
public class WebViewActivityConfig {
    String id;
    String start_url;
    String host_whitelist_regexp;

    public String getId() {
        return id;
    }

    public String getStart_url() {
        return start_url;
    }

    public String getHost_whitelist_regexp() {
        return host_whitelist_regexp;
    }
}


