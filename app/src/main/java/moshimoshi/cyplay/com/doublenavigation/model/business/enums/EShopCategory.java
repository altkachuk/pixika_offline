package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import java.util.HashMap;
import java.util.Map;

import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by romainlebouc on 13/12/2016.
 */

public enum EShopCategory{
    CURRENT_SHOP(-1,"current_shop"), OTHER_REGULAR_SHOP(R.string.close_shops, "close_shops"), WEB_SHOP(R.string.shop_type_web,"web_shop");

    private final int labelId;
    private final String code;


    private final static Map<String,EShopCategory > CODE_2_ESHOPCATEGORY = new HashMap<>();

    static {
        for (EShopCategory eShopCategory : EShopCategory.values() ){
            CODE_2_ESHOPCATEGORY.put(eShopCategory.getCode(), eShopCategory);
        }
    }
    EShopCategory(int labelId, String code) {
        this.labelId = labelId;
        this.code = code;
    }

    public int getLabelId() {
        return labelId;
    }

    public String getCode() {
        return code;
    }

    public static EShopCategory valueOfEShopCategorFromCode(String code){
        return CODE_2_ESHOPCATEGORY.get(code);
    }

}
