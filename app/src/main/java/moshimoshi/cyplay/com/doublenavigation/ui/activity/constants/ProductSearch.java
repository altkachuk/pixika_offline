package moshimoshi.cyplay.com.doublenavigation.ui.activity.constants;

import org.parceler.Parcel;

/**
 * Created by romainlebouc on 21/08/16.
 */
@Parcel
public class ProductSearch {
    String value;
    ESearchMode mode;
    String text;

    public ProductSearch() {

    }

    public ProductSearch(ESearchMode mode, String value) {
        this.value = value;
        this.mode = mode;
    }

    public ProductSearch(ESearchMode mode, String value, String text) {
        this.value = value;
        this.mode = mode;
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public ESearchMode getMode() {
        return mode;
    }

    public String getText() {
        return text;
    }
}
