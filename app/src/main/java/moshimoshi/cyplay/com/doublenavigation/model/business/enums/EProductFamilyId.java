package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

/**
 * Created by wentongwang on 13/04/2017.
 */

public enum EProductFamilyId {
    NEW_PRODUCT("Nouveaute");

    private String code;

    EProductFamilyId(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
