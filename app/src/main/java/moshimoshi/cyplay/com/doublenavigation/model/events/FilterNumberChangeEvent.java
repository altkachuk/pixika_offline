package moshimoshi.cyplay.com.doublenavigation.model.events;

/**
 * Created by wentongwang on 11/04/2017.
 */

public class FilterNumberChangeEvent {
    private int filterNumber;

    public FilterNumberChangeEvent(int filterNumber){
        this.filterNumber = filterNumber;
    }

    public int getFilterNumber() {
        return filterNumber;
    }
}
