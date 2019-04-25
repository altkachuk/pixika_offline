package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

/**
 * Created by damien on 11/03/16.
 */
@Parcel
public class Appearance {

    public Color color;
    public Fonts fonts;
    public Backgrounds backgrounds;

    public Color getColor() {
        return color;
    }

    public Fonts getFonts() {
        return fonts;
    }

    public Backgrounds getBackgrounds() {
        return backgrounds;
    }
}
