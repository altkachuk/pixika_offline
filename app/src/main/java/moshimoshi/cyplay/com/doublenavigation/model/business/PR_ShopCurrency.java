package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

@Parcel
public class PR_ShopCurrency {
    public String iso4217;
    public String symbol;
    public String label;

    public String getIso4217() {
        return iso4217;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getLabel() {
        return label;
    }
}
