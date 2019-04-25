package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by romainlebouc on 19/01/2017.
 */
@Parcel
public class WebViewConfig {

    List<WebViewActivityConfig> webviews;


    public WebViewActivityConfig getWebViewActivityConfig(String id) {
        WebViewActivityConfig result = null;
        if (webviews != null && id != null) {
            for (WebViewActivityConfig webViewActivityConfig : webviews) {
                if (id.equals(webViewActivityConfig.getId())) {
                    result = webViewActivityConfig;
                    break;
                }
            }
        }
        return result;

    }

}
