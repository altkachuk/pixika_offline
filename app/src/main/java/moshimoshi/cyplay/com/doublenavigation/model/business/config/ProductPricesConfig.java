package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPriceOfferType;

/**
 * Created by romainlebouc on 28/09/16.
 */
@Parcel
public class ProductPricesConfig {

    public final static boolean CROSSED_PRICE = true;
    public final static boolean NORMAL_PRICE = false;

    Boolean webprice;
    Boolean crossed_price;
    List<ProductSpecialPriceConfig> price_mapping;

    transient Map<String, String> tagToPrograms;
    transient Map<String, List<ProductSpecialPriceTypeConfig>> programToProductSpecialPriceTypeConfigs ;

    public Boolean getWebprice() {
        return webprice;
    }

    public List<ProductSpecialPriceConfig> getPrice_mapping() {
        return price_mapping;
    }

    public String getProgramForTag(String tag) {
        if (tagToPrograms == null) {
            tagToPrograms = new HashMap<>();
            for (ProductSpecialPriceConfig productSpecialPriceConfig : price_mapping) {
                String program = productSpecialPriceConfig.getProgram();
                if (productSpecialPriceConfig.getPrice_types() != null) {
                    for (ProductSpecialPriceTypeConfig price_types : productSpecialPriceConfig.getPrice_types()) {
                        tagToPrograms.put(price_types.getTag(), program);
                    }
                }
            }
        }
        return tagToPrograms.get(tag);
    }

    public List<ProductSpecialPriceTypeConfig> getProductSpecialPriceConfigs(String program) {
        if (programToProductSpecialPriceTypeConfigs == null) {
            programToProductSpecialPriceTypeConfigs = new HashMap<>();
            for (ProductSpecialPriceConfig productSpecialPriceConfig : this.price_mapping) {
                programToProductSpecialPriceTypeConfigs.put(productSpecialPriceConfig.getProgram(), productSpecialPriceConfig.getPrice_types());
            }
        }

        return programToProductSpecialPriceTypeConfigs.get(program);
    }

    public EPriceOfferType getPriceOfferType(String tag) {
        EPriceOfferType result = null;

        String program = getProgramForTag(tag);

        List<ProductSpecialPriceTypeConfig> productSpecialPriceTypeConfigs = getProductSpecialPriceConfigs(program);
        for (ProductSpecialPriceTypeConfig productSpecialPriceTypeConfig : productSpecialPriceTypeConfigs) {
            if (productSpecialPriceTypeConfig.getTag().equals(tag)) {
                result = productSpecialPriceTypeConfig.getPriceOfferType();
                break;
            }
        }

        return result;
    }

}
