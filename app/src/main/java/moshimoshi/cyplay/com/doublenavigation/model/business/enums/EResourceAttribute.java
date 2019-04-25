package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

/**
 * Created by romainlebouc on 12/06/2017.
 */

public enum EResourceAttribute {
    DETAILS("details"),
    WISHLIST("wishlist"),
    OFFERS("offers"),
    PAYMENTS("payments"),
    SALES("sales"),
    RELATED_PRODUCTS("related_products"),
    RELATED_ASSORTMENTS("related_assortments"),
    REVIEWS("reviews"),
    SKUS("skus");


    private final String code;

    EResourceAttribute(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
