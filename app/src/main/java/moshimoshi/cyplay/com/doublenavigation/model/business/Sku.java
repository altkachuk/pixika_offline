package moshimoshi.cyplay.com.doublenavigation.model.business;

import android.content.Context;

import org.parceler.Parcel;

import java.util.Iterator;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EShopType;
import moshimoshi.cyplay.com.doublenavigation.model.business.filters.ProductFilter;
import moshimoshi.cyplay.com.doublenavigation.model.business.filters.ProductFilterValue;
import moshimoshi.cyplay.com.doublenavigation.R;
import ninja.cyplay.com.apilibrary.models.filters.ResourceFilter;

/**
 * Created by romainlebouc on 08/07/2014.
 */
@Parcel
public class Sku {


    String id;

    String desc;
    String name;

    List<Ska> availabilities;
    Ska current_shop_availability;
    Ska web_shop_availability;

    Integer quantity;
    List<Media> medias;

    List<String> related_sku_ids;
    List<Product> related_product_skus;
    List<ProductSpecification> specs;
    String unit;
    Double notify_stock;

    public transient boolean skuAvailabilitesFilled = false;

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sku sku = (Sku) o;
        return id.equals(sku.id);
    }

    public void setSkuAvailabilitesFilled(boolean skuAvailabilitesFilled) {
        this.skuAvailabilitesFilled = skuAvailabilitesFilled;
    }

    public boolean isSkuAvailabilitesFilled() {
        return skuAvailabilitesFilled;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public List<Media> getMedias() {
        return medias;
    }

    public Ska getSkaForShop(String shopId) {
        Ska result = null;
        if (shopId != null) {
            if (this.getAvailabilities() != null) {
                for (Ska ska : this.getAvailabilities()) {
                    if (shopId.equals(ska.getShop_id())) {
                        result = ska;
                        break;
                    }
                }
            }
        }
        return result;
    }

    public Ska getWebShopSka() {
        return getFistSkaForShopType(EShopType.WEB);
    }

    public Ska getWarehouseSka() {
        return getFistSkaForShopType(EShopType.WAREHOUSE);
    }


    public Ska getFistSkaForShopType(EShopType eShopType) {
        Ska result = null;
        if (this.getAvailabilities() != null && eShopType != null) {
            for (Ska ska : this.getAvailabilities()) {
                if (ska.getShop() != null && eShopType == ska.getShop().getEShopType()) {
                    result = ska;
                }
            }
        }
        return result;
    }


    public List<ProductSpecification> getSpecs() {
        return specs;
    }

    public void setSpecs(List<ProductSpecification> specs) {
        this.specs = specs;
    }

    public List<String> getRelated_sku_ids() {
        return related_sku_ids;
    }

    public List<Product> getRelated_product_skus() {
        return related_product_skus;
    }

    public void setRelated_product_skus(List<Product> related_product_skus) {
        this.related_product_skus = related_product_skus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }

    public List<Ska> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(List<Ska> availabilities) {
        this.availabilities = availabilities;
    }

    public Double getNotify_stock() {
        return notify_stock;
    }

    public Ska getCurrent_shop_availability() {
        return current_shop_availability;
    }

    public Ska getWeb_shop_availability() {
        return web_shop_availability;
    }

    public String getUnit() {
        return unit;
    }

    public void filterAvailabilities(List<Shop> notFilterableShops, double minStockDisplay) {
        if (availabilities != null) {
            for (Iterator<Ska> iterator = availabilities.iterator(); iterator.hasNext(); ) {
                Ska ska = iterator.next();
                if (ska.getStock() != null && ska.getStock() < minStockDisplay) {
                    // Remove the current element from the iterator and the list.
                    if (ska.getShop() == null || notFilterableShops.indexOf(ska.getShop()) < 0) {
                        iterator.remove();
                    }
                }
            }
        }

    }

    public int getActiveFilterCount(Context context,
                                    List<ProductFilter> productFilters) {
        int result = 0;
        if (productFilters != null) {
            for (ProductFilter productFilter : productFilters) {
                if (productFilter.getLevel() != null && ResourceFilter.TYPE_LEVEL_DICT_VALUE == productFilter.getLevel()) {
                    if (this.getSpecs() != null) {
                        for (ProductSpecification productSpecification : this.getSpecs()) {
                            if (productSpecification.getSpec() != null && productSpecification.getSpec().equals(productFilter.getLabel(context))) {
                                for (ProductFilterValue productFilterValue : productFilter.getValues()) {
                                    if (productSpecification.getValue() != null && productSpecification.getValue().equals(productFilterValue.getLabel())) {
                                        result++;
                                    }
                                }

                            }

                        }
                    }

                }
            }
        }
        return result;
    }

    public ProductSpecification getSpecificationForId(String id) {
        ProductSpecification foundSpecification = null;
        if (id != null && specs != null) {
            for (int i = 0; i < specs.size(); i++) {
                ProductSpecification testedSpecification = specs.get(i);
                if (id.equals(testedSpecification.getId())) {
                    foundSpecification = testedSpecification;
                    break;
                }
            }
        }
        return foundSpecification;
    }

    public String getRef(Context context, Product product) {
        return getRef(context,
                product != null ? product.getId() : null,
                this.getId());
    }

    public static String getRef(Context context,
                                String productId,
                                String skuId) {
        boolean productIdVisible = context.getResources().getBoolean(R.bool.product_ref_product_id);
        boolean skuIdVisible = context.getResources().getBoolean(R.bool.product_ref_sku_id);
        return context.getString(R.string.product_id, (productIdVisible && productId != null ? productId : "")
                + " " + (skuIdVisible && skuId != null ? skuId : ""));
    }

}
