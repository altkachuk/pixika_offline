package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

@Parcel
public class Color {
    String neutral_light;
    String primary_light;
    String neutral_medium;
    String primary_dark;
    String primary_very_light;
    String neutral_dark;

    public String getNeutral_light() {
        return neutral_light;
    }

    public String getPrimary_light() {
        return primary_light;
    }

    public String getNeutral_medium() {
        return neutral_medium;
    }

    public String getPrimary_dark() {
        return primary_dark;
    }

    public String getPrimary_very_light() {
        return primary_very_light;
    }

    public String getNeutral_dark() {
        return neutral_dark;
    }
}
