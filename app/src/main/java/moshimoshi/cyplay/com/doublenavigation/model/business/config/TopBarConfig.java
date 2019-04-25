package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

/**
 * Created by wentongwang on 10/03/2017.
 */
@Parcel
public class TopBarConfig {

    public TopBarScan scan;
    public TopBarSearch search;

    public TopBarScan getScan() {
        return scan != null ? scan : new TopBarScan();
    }

    public void setScan(TopBarScan scan) {
        this.scan = scan;
    }

    public TopBarSearch getSearch() {
        return search != null ? search : new TopBarSearch();
    }

    public void setSearch(TopBarSearch search) {
        this.search = search;
    }
}
