package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import moshimoshi.cyplay.com.doublenavigation.model.ComplexPrice;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPriceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EShopType;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.ProductPricesConfig;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPriceOfferType;

/**
 * Created by damien on 25/02/15.
 */
@Parcel
public class Ska {

    String shop_id;
    Double stock;
    Double price;
    List<SpecialPrice> special_prices;
    Double crossed_price;
    PricePerUnit price_per_unit;
    List<SkaRestocking> restocking;
    Shop shop;

    public String getShopId() {
        return shop_id;
    }

    public void setShopId(String shopId) {
        this.shop_id = shopId;
    }

    public Double getStock() {
        return stock;
    }

    public Double getSafeStock() {
        return stock != null ? stock : 0.0d;
    }


    public Double getPrice() {
        return price;
    }

    public Double getCrossed_price() {
        return crossed_price;
    }

    public String getShop_id() {
        return shop_id;
    }

    public Shop getShop() {
        return shop;
    }

    public PricePerUnit getPrice_per_unit() {
        return price_per_unit;
    }

    public List<SkaRestocking> getRestocking() {
        if (restocking != null) {
            Collections.sort(restocking);
        }
        return restocking;
    }

    public List<SpecialPrice> getSpecial_prices() {
        return special_prices;
    }

    public String getStringStock() {
        return " " + (this.getStock() != null ? String.valueOf(this.getStock().intValue()) : "");
    }

    public static Shop getWebShop(List<Ska> skas, EShopType eShopType) {
        Shop result = null;
        if (skas != null && eShopType != null) {
            for (Ska ska : skas) {
                if (ska.getShop() != null && eShopType == ska.getShop().getEShopType()) {
                    result = ska.getShop();
                    break;
                }
            }
        }
        return result;
    }

    private transient Map<String, ComplexPrice> complexPrices;

    private Map<String, ComplexPrice> getComplexPrices(ProductPricesConfig productPricesConfig) {
        if (complexPrices == null) {
            complexPrices = new HashMap<>();

            if (special_prices != null) {
                for (SpecialPrice specialPrice : special_prices) {
                    String prog = productPricesConfig.getProgramForTag(specialPrice.getTag());
                    ComplexPrice complexPrice = complexPrices.get(prog);
                    if (complexPrice == null) {
                        complexPrice = new ComplexPrice();
                        complexPrices.put(prog, complexPrice);
                    }

                    EPriceOfferType ePriceOfferType = productPricesConfig.getPriceOfferType(specialPrice.getTag());
                    switch (ePriceOfferType) {
                        case DISCOUNT:
                            complexPrice.setDiscountPrice(specialPrice.getPrice());
                            break;
                        case NORMAL:
                            complexPrice.setNormalPrice(specialPrice.getPrice());
                            break;
                    }

                    //}
                }
            }
        }
        return complexPrices;
    }

    public Double getPrice(ProductPricesConfig productPricesConfig, EPriceType ePriceType, String program) {
        Map<String, ComplexPrice> complexPrices = getComplexPrices(productPricesConfig);

        Double result = null;
        if (special_prices == null || special_prices.isEmpty()) {
            switch (ePriceType) {
                case BEFORE_DISCOUNT:
                    result = crossed_price;
                    break;
                case FINAL:
                    result = price;
                    break;
            }
        } else {
            // Complexe price with a program
            ComplexPrice programComplexPrice = complexPrices.get(program);

            // Complexe price with no program
            ComplexPrice noProgramComplexPrice = complexPrices.get(null);


            if (noProgramComplexPrice != null ) {
                switch (ePriceType) {
                    case FINAL:
                        if (program == null || programComplexPrice == null) {
                            result = noProgramComplexPrice.getFinalPrice();
                        } else {
                            result = programComplexPrice.getFinalPrice();
                        }

                        break;
                    case BEFORE_DISCOUNT:
                        if (program == null ) {
                            result = noProgramComplexPrice.getPriceBeforeDiscount();
                        }else if (program !=null && programComplexPrice == null ){
                            result = null;
                        } else {
                            result = noProgramComplexPrice.getNormalPrice();
                        }
                        break;
                }
            }
        }
        return result;
    }

    public void setStock(Double stock) {
        this.stock = stock;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
