package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by romainlebouc on 03/06/16.
 */
@Parcel
public class ProductRecommendation implements Serializable {

    String product_id;

    Product product;

    Double score;
    String type;
    List<Product> explanations_products;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Product> getExplanations_products() {
        return explanations_products;
    }

    public void setExplanations_products(List<Product> explanations_products) {
        this.explanations_products = explanations_products;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
