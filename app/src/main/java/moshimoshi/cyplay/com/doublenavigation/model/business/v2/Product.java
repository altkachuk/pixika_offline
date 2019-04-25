package moshimoshi.cyplay.com.doublenavigation.model.business.v2;

import java.util.List;

/**
 * Created by andre on 15-Mar-19.
 */

public class Product {

    String id;
    List<ProductDescription> descriptions;
    List<ProductVariant> variants;
    List<ProductCategory> category;
    String reference;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ProductDescription> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<ProductDescription> descriptions) {
        this.descriptions = descriptions;
    }

    public ProductDescription getDescriptionByLanguage(String language) {
        if (descriptions != null) {
            for (ProductDescription description : descriptions) {
                if (description.getLanguage().equals(language))
                    return description;
            }
        }

        return null;
    }

    public List<ProductVariant> getVariants() {
        return variants;
    }

    public void setVariants(List<ProductVariant> variants) {
        this.variants = variants;
    }

    public List<ProductCategory> getCategory() {
        return category;
    }

    public void setCategory(List<ProductCategory> category) {
        this.category = category;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
