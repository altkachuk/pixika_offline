package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import java.util.HashMap;
import java.util.Map;

import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by romainlebouc on 31/08/16.
 */
public enum EShopType {
    REGULAR("regular", R.string.shop_type_regular),
    WEB("web", R.string.shop_type_web),
    WAREHOUSE("warehouse", R.string.shop_type_warehouse);

    private final String code;
    private final int labelId;

    EShopType(String code, int labelId) {
        this.code = code;
        this.labelId = labelId;
    }

    public String getCode() {
        return code;
    }

    public int getLabelId() {
        return labelId;
    }

    public static Map<String, EShopType> CODE_TO_SHOPTYPE = new HashMap<>();

    static {
        for ( EShopType eShopType : EShopType.values()){
            CODE_TO_SHOPTYPE.put(eShopType.code, eShopType);
        }
    }

    public static EShopType getEShopTypeFromCode(String code){
        return CODE_TO_SHOPTYPE.get(code);
    }

}
