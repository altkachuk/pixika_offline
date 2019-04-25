package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by romainlebouc on 06/01/2017.
 */
@Parcel
public class ProductSpecialPriceConfig {
    String program;
    List<ProductSpecialPriceTypeConfig> price_types;

    public String getProgram() {
        return program;
    }

    public List<ProductSpecialPriceTypeConfig> getPrice_types() {
        return price_types;
    }
}
