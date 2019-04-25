package moshimoshi.cyplay.com.doublenavigation.ui.activity;

/**
 * Created by romainlebouc on 02/02/2017.
 */

public class ClickAndCollectWebviewActivity extends WebViewActivity {

    @Override
    protected String getNavDrawerCurrentItem() {
        return EMenuAction.NAVDRAWER_ITEM_WEBVIEW_CLICK_AND_COLLECT.getCode();
    }
}
