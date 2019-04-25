package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

/**
 * Created by romainlebouc on 06/01/2017.
 */
@Parcel
public class SpecialPrice {

    String tag;
    Double price;

    public String getTag() {
        return tag;
    }

    public Double getPrice() {
        return price;
    }
}
