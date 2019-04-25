package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import java.util.List;

import ninja.cyplay.com.apilibrary.models.ModelResource;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EShopType;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AShop;

/**
 * Created by damien on 20/04/15.
 */

@Parcel
@ModelResource
public class Shop extends PR_AShop {

    public String short_name;
    public String long_name;
    public PR_ShopCurrency currency;
    public Mail mail;
    public ShopContact contact;
    public String opened_date;
    public String closed_date;
    public Boolean active;
    public String shop_type;

    public PR_ShopGeoLocation geolocation;

    public String getId() {
        return id;
    }

    public String getShort_name() {
        return short_name;
    }

    public String getLong_name() {
        return long_name;
    }

    public String getName(boolean shortNameFirst){
        if (shortNameFirst ){
            return short_name!=null ? short_name : long_name;
        }else{
            return long_name!=null ? long_name : short_name;
        }

    }


    public PR_ShopCurrency getCurrency() {
        return currency;
    }

    public Mail getMail() {
        return mail;
    }

    public ShopContact getContact() {
        return contact;
    }

    public String getOpened_date() {
        return opened_date;
    }

    public String getClosed_date() {
        return closed_date;
    }

    public Boolean getActive() {
        return active;
    }

    public PR_ShopGeoLocation getGeolocation() {
        return geolocation;
    }

    @Override
    public String toString() {
        return  getId() + " "+  (long_name != null ? long_name : short_name);
    }

    public String getShop_type() {
        return shop_type;
    }

    public void setShop_type(String shop_type) {
        this.shop_type = shop_type;
    }

    public EShopType getEShopType(){
        return EShopType.getEShopTypeFromCode(this.shop_type);
    }

    public static int getEShopTypeCount(List<ShopsItem> shopList, EShopType eShopType) {
        int result = 0;
        if (shopList != null && eShopType != null) {
            for (ShopsItem shopsItem : shopList) {
                boolean isShopType = false;
                if (shopsItem.getShops() != null) {
                    for (Shop shop : shopsItem.getShops()) {
                        if (eShopType == shop.getEShopType()) {
                            isShopType = true;
                            break;
                        }
                    }
                    result += isShopType ? 1 : 0;
                }
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Shop shop = (Shop) o;

        return id != null ? id.equals(shop.id) : shop.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}