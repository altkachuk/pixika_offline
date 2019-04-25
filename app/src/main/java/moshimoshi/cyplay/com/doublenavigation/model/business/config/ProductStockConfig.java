package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import android.support.annotation.NonNull;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPurchaseCollectionMode;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EShopCategory;

/**
 * Created by romainlebouc on 25/08/16.
 */
@Parcel
public class ProductStockConfig {

    public Boolean asynchronous;
    public Double min_stock_display;
    public List<String> shortcuts_display;
    public List<ProductStockChoiceConfig> order_choices;

    public Boolean getAsynchronous() {
        return asynchronous;
    }

    public Double getMin_stock_display() {
        return min_stock_display;
    }

    public List<EShopCategory> getEShopCategoryDisplay() {
        List<EShopCategory> eShopCategories = null;
        if (shortcuts_display != null) {
            eShopCategories = new ArrayList<>();
            for (String code : shortcuts_display) {
                eShopCategories.add(EShopCategory.valueOfEShopCategorFromCode(code));
            }
        }
        return eShopCategories;
    }

    public List<ProductStockChoiceConfig> getOrder_choices() {
        return order_choices;
    }

    public @NonNull ProductStockChoiceConfig getProductStockChoiceConfig(EPurchaseCollectionMode purchaseCollectionMode){
        ProductStockChoiceConfig result = null;
        if ( order_choices !=null && purchaseCollectionMode !=null){
            for (ProductStockChoiceConfig productStockChoiceConfig: order_choices){
                if (purchaseCollectionMode.equals(productStockChoiceConfig.getEPurchaseCollectionMode())
                        && productStockChoiceConfig.getExtra() !=null){
                    result =productStockChoiceConfig;
                    break;
                }
            }
        }
        if ( result==null){
            result = new ProductStockChoiceConfig(purchaseCollectionMode);
        }
        return result;
    }


}
