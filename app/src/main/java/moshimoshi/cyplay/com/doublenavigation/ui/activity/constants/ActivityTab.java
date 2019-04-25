package moshimoshi.cyplay.com.doublenavigation.ui.activity.constants;

import android.content.Context;

/**
 * Created by romainlebouc on 05/08/16.
 */

public class ActivityTab<T> {

    private final int titleResourceId;
    private String title;
    private final Class fragmentClass;
    private final String confTag;
    private final boolean shouldCustomerBeIdentified;

    public ActivityTab(int titleResourceId, Class fragmentClass, String confTag) {
        this(titleResourceId, fragmentClass, confTag, false);
    }

    public ActivityTab(int titleResourceId, Class fragmentClass, String confTag, boolean shouldCustomerBeIdentified) {
        this.titleResourceId = titleResourceId;
        this.fragmentClass = fragmentClass;
        this.confTag = confTag;
        this.shouldCustomerBeIdentified = shouldCustomerBeIdentified;
    }

    public Class<T> getFragmentClass() {
        return fragmentClass;
    }

    public String getConfTag() {
        return confTag;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle(Context context) {
        if (title != null) {
            return title;
        } else {
            return context.getString(titleResourceId);
        }
    }

    public boolean isShouldCustomerBeIdentified() {
        return shouldCustomerBeIdentified;
    }
}