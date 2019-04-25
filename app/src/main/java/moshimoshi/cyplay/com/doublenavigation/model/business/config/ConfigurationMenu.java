package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import android.app.Activity;

/**
 * Created by romainlebouc on 23/11/16.
 */
public class ConfigurationMenu {

    int drawableId;
    int label;
    Class<? extends Activity> activityClass;

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public void setActivityClass(Class<? extends Activity> activityClass) {
        this.activityClass = activityClass;
    }

    public Class<? extends Activity> getActivityClass() {
        return activityClass;
    }
}
