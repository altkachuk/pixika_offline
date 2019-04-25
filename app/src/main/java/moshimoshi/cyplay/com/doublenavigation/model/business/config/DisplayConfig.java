package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by damien on 11/03/16.
 */
@Parcel
public class DisplayConfig {

    public List<PR_MenuItem> customerTabs;
    public List<PR_MenuItem> sideBarItems;
    public List<PR_MenuItem> productTabs;

    public TopBarConfig topBar;

    public List<PR_MenuItem> getCustomerTabs() {
        return customerTabs;
    }

    public List<PR_MenuItem> getSideBarItems() {
        return sideBarItems;
    }

    public List<PR_MenuItem> getProductTabs() {
        return productTabs;
    }

    public TopBarConfig getTopBar() {
        return topBar != null ? topBar : new TopBarConfig();
    }
}
