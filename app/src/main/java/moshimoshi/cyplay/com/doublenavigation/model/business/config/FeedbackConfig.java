package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.FeedbackScreen;

/**
 * Created by wentongwang on 29/06/2017.
 */
@Parcel
public class FeedbackConfig {

    List<FeedbackScreen> screens;

    public List<FeedbackScreen> getScreens() {
        return screens;
    }
}
