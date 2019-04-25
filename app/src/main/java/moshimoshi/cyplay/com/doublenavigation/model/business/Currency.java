package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

/**
 * Created by romainlebouc on 16/05/16.
 */
@Parcel
public class Currency {

    String symbol;
    String label;
    String iso4217;

    public String getSymbol() {
        return symbol;
    }

    public String getLabel() {
        return label;
    }

    public String getIso4217() {
        return iso4217;
    }
}
