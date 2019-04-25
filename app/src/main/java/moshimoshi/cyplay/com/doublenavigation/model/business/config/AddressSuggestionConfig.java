package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EAddressSuggestionPartner;

/**
 * Created by wentongwang on 05/06/2017.
 */
@Parcel
public class AddressSuggestionConfig {
    Boolean enable;
    String partner;
    String url_prefix;

    public Boolean getEnable() {
        return enable != null ? enable : false;
    }

    public EAddressSuggestionPartner getPartner() {
        return EAddressSuggestionPartner.getEPartnerFromCode(partner);
    }

    public String getUrlPrefix() {
        return url_prefix;
    }
}
