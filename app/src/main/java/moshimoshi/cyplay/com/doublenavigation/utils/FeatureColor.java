package moshimoshi.cyplay.com.doublenavigation.utils;

import android.content.Context;

public enum FeatureColor {
    NEUTRAL_DARK,
    NEUTRAL_LIGHT,
    PRIMARY_LIGHT,
    NEUTRAL_MEDIUM,
    PRIMARY_DARK;

    public static String getColorHex(Context context,FeatureColor color, ConfigHelper configHelper) {
        if (configHelper!=null
                && configHelper.getFeature() != null
                && configHelper.getFeature().getAppearanceConfig() != null
                && configHelper.getFeature().getAppearanceConfig().getColor() != null) {
            switch (color) {
                case NEUTRAL_DARK:
                    return configHelper.getFeature().getAppearanceConfig().getColor().getNeutral_dark();
                case NEUTRAL_LIGHT:
                    return configHelper.getFeature().getAppearanceConfig().getColor().getNeutral_light();
                case PRIMARY_LIGHT:
                    return configHelper.getFeature().getAppearanceConfig().getColor().getPrimary_light();
                case NEUTRAL_MEDIUM:
                    return configHelper.getFeature().getAppearanceConfig().getColor().getNeutral_medium();
                case PRIMARY_DARK:
                    return configHelper.getFeature().getAppearanceConfig().getColor().getPrimary_dark();
            }
        }
        return "#000000";
    }
}
