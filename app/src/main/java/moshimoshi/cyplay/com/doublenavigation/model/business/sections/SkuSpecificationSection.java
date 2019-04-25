package moshimoshi.cyplay.com.doublenavigation.model.business.sections;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductSpecification;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sku;


/**
 * Created by romainlebouc on 09/12/2016.
 */
@Parcel
public class SkuSpecificationSection {

    String id;

    List<ProductSpecification> productSpecifications;

    public SkuSpecificationSection() {

    }

    public String getId() {
        return id;
    }

    public SkuSpecificationSection(String id, List<ProductSpecification> productSpecifications) {
        this.id = id;
        this.productSpecifications = productSpecifications;
    }

    public List<ProductSpecification> getProductSpecifications() {
        return productSpecifications;
    }

    public static List<SkuSpecificationSection> getProductSpecificationSections(Product product, Sku sku) {
        List<SkuSpecificationSection> skuSpecificationSections = new ArrayList<>();

        if (sku != null) {
            Map<String, List<ProductSpecification>> productSpecificationMap = new HashMap<>();
            List<ProductSpecification> productSpecifications = new ArrayList<>();
            if (sku.getSpecs() != null) {
                productSpecifications.addAll(sku.getSpecs());
            }
            if (product.getSpecs() != null) {
                productSpecifications.addAll(product.getSpecs());
            }

            for (ProductSpecification productSpecification : productSpecifications) {
                List<String> sections = productSpecification.getSections();
                if (sections != null) {
                    for (String section : sections) {
                        List<ProductSpecification> productSpecificationList = productSpecificationMap.get(section);
                        if (productSpecificationList == null) {
                            productSpecificationList = new ArrayList<>();
                            productSpecificationMap.put(section, productSpecificationList);
                        }
                        productSpecificationList.add(productSpecification);
                    }
                }
            }

            for (Map.Entry<String, List<ProductSpecification>> entry : productSpecificationMap.entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    skuSpecificationSections.add(new SkuSpecificationSection(entry.getKey(),
                            entry.getValue()));
                }
            }
        }


        return skuSpecificationSections;

    }
}
