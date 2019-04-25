package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;


/**
 * Created by romainlebouc on 14/08/16.
 */
@Parcel
public class Modules{
    public PR_AModule scan;
    public PR_AModule autoComplete;
    public PR_AModule basket;
    public PR_AModule beacon;
    public PR_AModule tweets;
    public PR_AModule calendar;
    public PR_AModule mailing;

    public PR_AModule getScan() {
        return scan;
    }

    public PR_AModule getAutoComplete() {
        return autoComplete;
    }

    public PR_AModule getBasket() {
        return basket;
    }

    public PR_AModule getBeacon() {
        return beacon;
    }

    public PR_AModule getTweets() {
        return tweets;
    }

    public PR_AModule getCalendar() {
        return calendar;
    }

    public PR_AModule getMailing() {
        return mailing;
    }
}
