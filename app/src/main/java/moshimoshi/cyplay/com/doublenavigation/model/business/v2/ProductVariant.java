package moshimoshi.cyplay.com.doublenavigation.model.business.v2;

import java.util.List;

/**
 * Created by andre on 15-Mar-19.
 */

public class ProductVariant {

    List<ProductDescription> descriptions;
    List<Media> medias;
    String reference;
    String weight;
    Double price;
    Double stock;

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

    public List<Media> getMedias() {
        return medias;
    }

    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getStock() {
        return stock;
    }

    public void setStock(Double stock) {
        this.stock = stock;
    }
}
