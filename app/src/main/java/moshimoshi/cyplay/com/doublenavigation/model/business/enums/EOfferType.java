package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import java.util.HashMap;
import java.util.Map;

import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by damien on 16/09/16.
 */
public enum EOfferType {
    CUSTOMER("customer", R.string.offer_type_customer, R.drawable.ic_customer_offer, 1),
    REGULAR("regular", R.string.offer_type_regular, R.drawable.ic_shop_offer, 2),
    WEB("web", R.string.offer_type_web, R.drawable.ic_web_offer, 3),
    BASKET("basket", R.string.offer_type_basket, 0, 4),
    PRODUCT("product", R.string.offer_type_product, 0, 5);


    private final String code;
    private final int labelId;
    private final int iconId;
    private final int relevance;

    EOfferType(String code, int labelId, int iconId, int relevance) {
        this.code = code;
        this.labelId = labelId;
        this.iconId = iconId;
        this.relevance = relevance;
    }

    public String getCode() {
        return code;
    }

    public int getLabelId() {
        return labelId;
    }

    public int getIconId() {
        return iconId;
    }

    public static Map<String, EOfferType> CODE_TO_OFFERTYPE = new HashMap<>();

    static {
        for (EOfferType type : EOfferType.values()) {
            CODE_TO_OFFERTYPE.put(type.code, type);
        }
    }

    public static EOfferType getEOfferTypeFromCode(String code) {
        return CODE_TO_OFFERTYPE.get(code);
    }

    public int getRelevance() {
        return relevance;
    }
}
