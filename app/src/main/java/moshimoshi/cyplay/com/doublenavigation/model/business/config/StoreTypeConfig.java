package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EShopType;

/**
 * Created by wentongwang on 16/03/2017.
 */
@Parcel
public class StoreTypeConfig {

    List<String> store_type;

    public List<EShopType> getStore_type() {
        List<EShopType> eShopType = null;
        if (store_type != null) {
            eShopType = new ArrayList<>();
            for (String code : store_type) {
                eShopType.add(EShopType.getEShopTypeFromCode(code));
            }
        }else {
            eShopType =Arrays.asList(EShopType.values());
        }
        return eShopType;
    }
}
