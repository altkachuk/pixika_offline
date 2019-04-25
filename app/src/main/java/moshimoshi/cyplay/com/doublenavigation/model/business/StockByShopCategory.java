package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EShopCategory;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EShopType;


/**
 * Created by romainlebouc on 02/12/2016.
 */
@Parcel
public class StockByShopCategory {

    EShopCategory eShopCategory;
    double stock;

    public StockByShopCategory() {
    }

    public StockByShopCategory(EShopCategory eShopCategory) {
        this.eShopCategory = eShopCategory;
    }

    public EShopCategory geteShopCategory() {
        return eShopCategory;
    }

    public Double getStock() {
        return stock;
    }


    public static List<StockByShopCategory> getStockByShopCategory(Product productWithStock,
                                                                   String skuId,
                                                                   Shop currentShop,
                                                                   List<EShopCategory> filter) {
        List<StockByShopCategory> stockByShopCategoryList = new ArrayList<>();

        StockByShopCategory currentShopCategory = null;
        if (filter == null || filter.contains(EShopCategory.CURRENT_SHOP)) {
            currentShopCategory = new StockByShopCategory(EShopCategory.CURRENT_SHOP);
            stockByShopCategoryList.add(currentShopCategory);
        }

        StockByShopCategory otherRegularShop = null;
        if (filter == null || filter.contains(EShopCategory.OTHER_REGULAR_SHOP)) {
            otherRegularShop = new StockByShopCategory(EShopCategory.OTHER_REGULAR_SHOP);
            stockByShopCategoryList.add(otherRegularShop);
        }

        StockByShopCategory webShop = null;
        if (filter == null || filter.contains(EShopCategory.WEB_SHOP)) {
            webShop = new StockByShopCategory(EShopCategory.WEB_SHOP);
            stockByShopCategoryList.add(webShop);
        }

        Sku sku = productWithStock.getSku(skuId);
        if (sku != null && sku.getAvailabilities() != null) {
            for (Ska ska : sku.getAvailabilities()) {
                if (ska.getShop() != null && ska.getShop().getEShopType() != null) {
                    double stock = ska.getStock() != null ? ska.getStock() : 0d;
                    if (ska.getShop().getEShopType() == EShopType.WEB) {
                        if (webShop != null) {
                            webShop.incStock(stock);
                        }
                    } else if (ska.getShop().getEShopType() ==  EShopType.REGULAR) {
                        if (ska.getShop().getId().equals(currentShop.getId())) {
                            if (currentShopCategory != null) {
                                currentShopCategory.incStock(stock);
                            }
                        } else {
                            if (otherRegularShop != null) {
                                otherRegularShop.incStock(stock);
                            }
                        }
                    } else if (ska.getShop().getEShopType() ==  EShopType.WAREHOUSE) {
                    }
                }

            }
        }
        return stockByShopCategoryList;

    }

    public double incStock(double stock) {
        return this.stock += stock;
    }

}
