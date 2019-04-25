package moshimoshi.cyplay.com.doublenavigation.model.business;

import android.content.Context;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.config.ProductDefaultSpecificationConfig;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.ProductDefaultSpecificationValuesConfig;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EOfferDiscountType;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EProductFamilyId;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.ESpecSize;
import moshimoshi.cyplay.com.doublenavigation.model.business.filters.ProductFilter;
import ninja.cyplay.com.apilibrary.models.ModelField;
import ninja.cyplay.com.apilibrary.models.ModelResource;
import ninja.cyplay.com.apilibrary.models.ReadOnlyModelField;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AProduct;

@Parcel
@ModelResource
public class Product extends PR_AProduct implements ProductItem {

    //Product projection without skus availabilites
    public transient final static List<String> PRODUCT_PROJECTION = Arrays.asList(
            "id",
            "name",
            "short_desc",
            "long_desc",
            "selectors",
            "brand",
            "substitute_prod_ids",
            "related_prod_ids",
            "related_sku_ids",
            "medias",
            "assortment",
            "skus__id",
            "skus__ean",
            "skus__specs",
            "skus__desc",
            "skus__name",
            "skus__medias",
            "skus__unit",
            "skus__notify_stock",
            "skus__current_shop_availability__special_prices",
            "skus__current_shop_availability__price",
            "skus__current_shop_availability__crossed_price",
            "skus__current_shop_availability__price_per_unit",
            "skus__web_shop_availability__special_prices",
            "skus__web_shop_availability__price",
            "skus__web_shop_availability__crossed_price",
            "skus__web_shop_availability__price_per_unit",
            "skus__related_prod_ids",
            "skus__related_sku_ids",
            "specs",
            "offers",
            "family_ids"
    );

    public transient final static List<String> PRODUCT_PREVIEW_PROJECTION = Arrays.asList("id",
            "name",
            "brand",
            "assortment",
            "medias",
            "skus__medias",
            "offers",
            "family_ids",
            "availabilities");

    public transient final static List<String> PRODUCT_STOCK_PROJECTION = Arrays.asList("id",
            "skus__id",
            "skus__availabilities");

    @ModelField(eventLogging = true)
    String id;

    @ModelField
    String name;

    @ModelField
    String short_desc;

    @ModelField
    String long_desc;

    List<String> family_ids;

    @ModelField
    String url;

    @ModelField
    String assortment;

    @ModelField
    String status;

    @ModelField
    String brand;

    List<ProductSpecification> specs;
    Integer notify_stock;

    List<String> related_prod_ids;

    @ReadOnlyModelField
    List<Product> related_products;

    @ReadOnlyModelField
    List<Product> assortment_products;

    @ReadOnlyModelField
    List<Ska> availabilities;

    List<String> substitute_prod_ids;
    List<Product> substitute_products;

    List<Sku> skus;

    List<ProductSpecSelector> selectors;

    List<Media> medias;
    Date creation_date;

    List<ProductOffer> offers;

    public Product() {
    }

    public PRODUCT_TYPE product_type;


    public Sku getSku(String skuId) {
        Sku result = null;
        if (skus != null && skuId != null) {
            for (Sku sku : skus) {
                if (sku.getId().equals(skuId)) {
                    result = sku;
                    break;
                }
            }
        }
        return result;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public List<Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLong_desc() {
        return long_desc;
    }

    public void setLong_desc(String long_desc) {
        this.long_desc = long_desc;
    }

    public String getShort_desc() {
        return short_desc;
    }

    public String getAssortment() {
        return assortment;
    }

    public void setShort_desc(String short_desc) {
        this.short_desc = short_desc;
    }

    public List<String> getRelated_prod_ids() {
        return related_prod_ids;
    }

    public List<String> getSubstitute_prod_ids() {
        return substitute_prod_ids;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public String getUrl() {
        return url;
    }

    public String getStatus() {
        return status;
    }

    public Integer getNotify_stock() {
        return notify_stock;
    }

    public List<ProductSpecification> getSpecs() {
        return specs;
    }

    public void setProduct_type(PRODUCT_TYPE product_type) {
        this.product_type = product_type;
    }

    public boolean isRecoProduct() {
        return this.product_type == PRODUCT_TYPE.PRODUCT_TYPE_RECO;
    }

    public boolean isRecurringProduct() {
        return this.product_type == PRODUCT_TYPE.PRODUCT_TYPE_RECURING;
    }

    public boolean isTopSalesProduct() {
        return this.product_type == PRODUCT_TYPE.PRODUCT_TYPE_TOPSALES;
    }

    public List<Media> getMedias() {
        return medias;
    }

    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }

    public List<Product> getSubstitute_products() {
        return substitute_products;
    }

    public void setSubstitute_products(List<Product> substitute_products) {
        this.substitute_products = substitute_products;
    }

    public List<Product> getRelated_products() {
        return related_products;
    }

    public void setRelated_products(List<Product> related_products) {
        this.related_products = related_products;
    }

    public List<Product> getAssortment_products() {
        return assortment_products;
    }

    public void setAssortment_products(List<Product> assortment_products) {
        this.assortment_products = assortment_products;
    }

    public Double getPrice() {
        if (skus != null) {
            if (skus.size() > 0) {
                return skus.get(0).current_shop_availability != null ?
                        skus.get(0).current_shop_availability.getPrice() : null;
            }
        }

        if (availabilities == null) {
            return null;
        }
        if (availabilities.size() == 0) {
            return null;
        }
        return availabilities.get(0).getPrice();
    }

    public Double getCrossedPrice() {
        if (skus != null) {
            if (skus.size() > 0) {
                return skus.get(0).current_shop_availability != null ?
                        skus.get(0).current_shop_availability.getCrossed_price() : null;
            }
        }

        if (availabilities == null) {
            return null;
        }
        if (availabilities.size() == 0) {
            return null;
        }
        return availabilities.get(0).getCrossed_price();
    }

    public List<ProductSpecSelector> getSelectors() {
        return selectors;
    }

    public void setSelectors(List<ProductSpecSelector> selectors) {
        this.selectors = selectors;
    }

    public ProductOffer getOfferByDiscountType(EOfferDiscountType type) {
        if (offers != null) {
            for (ProductOffer offer : offers) {
                if (type.equals(offer.getDiscount_type())) {
                    return offer;
                }
            }
        }
        return null;
    }

    public boolean isNewProduct() {
        return family_ids != null && family_ids.contains(EProductFamilyId.NEW_PRODUCT.getCode());
    }

    public List<String> getFamilyIds() {
        return family_ids;
    }

    public void setFamilyIds(List<String> familyIds) {
        this.family_ids = familyIds;
    }

    @Override
    public Product getProduct() {
        return this;
    }

    @Override
    public Date getDate() {
        return null;
    }

    // Enum for product types
    public enum PRODUCT_TYPE {
        PRODUCT_TYPE_TOPSALES,
        PRODUCT_TYPE_RECO,
        PRODUCT_TYPE_RECURING
    }

    /**
     * Update current product with a product having only stocks
     *
     * @param productWithStock
     */
    public void updateStock(Product productWithStock) {
        if (productWithStock != null && this.getId().equals(productWithStock.getId())) {
            List<Sku> skusWithStock = productWithStock.getSkus();
            List<Sku> currentSkus = this.getSkus();
            if (skus != null && currentSkus != null) {
                // Loop on the Skus
                for (Sku skuWithStock : skusWithStock) {
                    int skuPos = currentSkus.indexOf(skuWithStock);
                    Sku currentSku = skuPos >= 0 ? currentSkus.get(skuPos) : null;
                    if (currentSku != null) {
                        currentSku.setAvailabilities(skuWithStock.getAvailabilities());
                    }
                    currentSku.setSkuAvailabilitesFilled(true);
                }
            }
        }
    }


    public boolean isSkuAvailabilitesFilled(Sku skuToCheck) {
        return skuToCheck != null ? skuToCheck.isSkuAvailabilitesFilled() : false;
    }

    // Product Specification

    private ProductSpecification findSpecFromSelectorSpecId(Sku sku, final String selectorSpecId) {
        ProductSpecification specificationFound = null;
        if (sku != null && sku.getSpecs() != null && selectorSpecId != null) {
            try {
                specificationFound = Iterables.find(sku.getSpecs(), new Predicate<ProductSpecification>() {
                    public boolean apply(ProductSpecification specification) {
                        return specification != null && specification.getSpec() != null && specification.getId().equals(selectorSpecId);
                    }
                });
            } catch (Exception e) {
                // Do nothing element not found
            }
        }
        return specificationFound;
    }

    public List<ProductSpecification> getSpecificationsFromSelector(List<Sku> originSkus, String selectorId) {
        List<ProductSpecification> foundSpecs = new ArrayList<>();
        if (selectorId != null && originSkus != null) {
            // Check all SKUs
            for (int i = 0; i < originSkus.size(); i++) {
                Sku sku = originSkus.get(i);
                ProductSpecification extractedSpec = findSpecFromSelectorSpecId(sku, selectorId);
                // avoid Duplication
                if (extractedSpec != null && !foundSpecs.contains(extractedSpec))
                    foundSpecs.add(extractedSpec);
            }
        }
        return foundSpecs;
    }

    public String getSpecificationSelectorForLevel(int level) {
        String selector = null;
        if (selectors != null && selectors.size() > level) {
            ProductSpecSelector productSpecSelector = selectors.get(level);
            if (productSpecSelector != null)
                selector = productSpecSelector.getSpec_id();
        }
        return selector;
    }


    public List<ProductSpecification> getSpecsForLevel(int level) {
        return getSpecificationsFromSelector(getSkus(), getSpecificationSelectorForLevel(level));
    }

    public List<ProductSpecification> getSpecsForLevelFromSkus(List<Sku> skus, int level) {
        return getSpecificationsFromSelector(skus, getSpecificationSelectorForLevel(level));
    }

    private Boolean checkIfSkuHasSpecifiation(Sku sku, ProductSpecification specification) {
        return sku != null && specification != null && (sku.getSpecs() != null && sku.getSpecs().contains(specification));
    }

    public List<Sku> getSkusWithSpecifications(ProductSpecification specification) {
        List<ProductSpecification> specifications = new ArrayList<>();
        specifications.add(specification);
        return getSkusWithSpecifications(specifications);
    }

    public List<Sku> getSkusWithSpecifications(List<ProductSpecification> specifications) {
        List<Sku> skusMatchingCriteria = new ArrayList<>();

        if (skus != null) {
            for (int i = 0; i < skus.size(); i++) {
                Sku sku = skus.get(i);

                // check if the Sku has ALL the specifications required
                boolean skuHasAllSpec = true;
                for (ProductSpecification testSpecification : specifications) {
                    if (!checkIfSkuHasSpecifiation(sku, testSpecification)) {
                        skuHasAllSpec = false;
                        break;
                    }
                }
                // Sku has all the required specification
                if (skuHasAllSpec)
                    skusMatchingCriteria.add(sku);
            }
        }
        return skusMatchingCriteria;
    }

    public int getFilteredSkuPosition(Context context, String skuId, List<ProductFilter> productFilters) {
        int position = -1;
        int score = 0;
        int currentPosition = 0;
        if (this.getSkus() != null) {
            for (Sku sku : this.getSkus()) {
                if (skuId != null) {
                    if (skuId.equals(sku.getId())) {
                        position = currentPosition;
                        break;
                    }
                } else if (productFilters != null) {
                    int currentScore = sku.getActiveFilterCount(context, productFilters);
                    if (currentScore > score) {
                        score = currentScore;
                        position = currentPosition;
                    }
                }
                currentPosition++;
            }
        }

        return position;
    }

    @Override
    public String getSkuId() {
        if (skus != null && !skus.isEmpty()) {
            return skus.get(0).getId();
        }
        return null;
    }

    public static Sku getFirstSkuMatchingSpecification(Product product, List<ProductDefaultSpecificationConfig> productDefaultSpecificationConfigs) {
        Sku result = null;
        List<Sku> skus = product.getSkus();
        for (ProductDefaultSpecificationConfig productDefaultSpecificationConfig : productDefaultSpecificationConfigs) {
            ProductDefaultSpecificationValuesConfig source = productDefaultSpecificationConfig.getSource();
            ProductDefaultSpecificationValuesConfig target = productDefaultSpecificationConfig.getTarget();
            if (source != null
                    && source.getValue() != null
                    && source.getSpec_id() != null
                    && target != null
                    && target.getValue() != null
                    && target.getSpec_id() != null) {
                boolean found = false;
                for (Sku sku : skus) {
                    ProductSpecification sourceProductSpecification = product.getSpecificationForId(source.getSpec_id());
                    ProductSpecification targetProductSpecification = sku.getSpecificationForId(target.getSpec_id());
                    if (sourceProductSpecification != null
                            && targetProductSpecification != null
                            && source.getValue().equals(sourceProductSpecification.getValue_id())
                            && target.getValue().equals(targetProductSpecification.getValue())) {
                        result = sku;
                        found = true;
                        break;
                    }
                }
                if (found) {
                    break;
                }
            }
        }
        return result;
    }

    public static Sku getFirstSkuMatchingCustomer(List<Sku> skus, Customer customer) {
        Sku result = null;

        if (skus != null) {
            for (Sku sku : skus) {
                boolean match = true;
                List<ProductSpecification> specs = sku.getSpecs();
                if (specs != null) {
                    for (ProductSpecification productSpecification : specs) {
                        if (ESpecSize.SPEC_SIZE_CODE.contains(productSpecification.getId())) {
                            ESpecSize eSpecSize = ESpecSize.getSpecSizeFromCode(productSpecification.getId());
                            if (eSpecSize != null && customer != null) {
                                match &= eSpecSize.matchCustomer(customer, productSpecification);
                                if (!match) {
                                    break;
                                }
                            } else {
                                match = false;
                                break;
                            }
                        }
                    }
                }
                if (match) {
                    result = sku;
                    break;
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

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Product)) return false;
        Product otherProduct = (Product) other;
        return this.getId() != null && this.getId().equals(otherProduct.getId());
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}