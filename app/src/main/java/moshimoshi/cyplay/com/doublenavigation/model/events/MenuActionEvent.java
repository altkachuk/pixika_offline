package moshimoshi.cyplay.com.doublenavigation.model.events;

/**
 * Created by wentongwang on 21/03/2017.
 */

public class MenuActionEvent {
    private String tag;

    public MenuActionEvent(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
