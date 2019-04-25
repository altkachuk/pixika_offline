package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import java.util.List;

import ninja.cyplay.com.apilibrary.models.ModelResource;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AProductSuggestion;

/**
 * Created by romainlebouc on 17/08/16.
 */
@Parcel
@ModelResource
public class ProductSuggestion extends PR_AProductSuggestion {

    List<Product> products;
    List<Category> families;
    List<String> brands;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Category> getFamilies() {
        return families;
    }

    public List<String> getBrands() {
        return brands;
    }

}
