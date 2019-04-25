package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import java.util.List;

import ninja.cyplay.com.apilibrary.models.ModelResource;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ACatalogueLevel;

/**
 * Created by romainlebouc on 09/08/16.
 */
@Parcel
@ModelResource
public class CatalogueLevel  extends PR_ACatalogueLevel {

    Category category;
    List<Product> top_products;
    List<Category> sub_categories;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    
    public List<Product> getTop_products() {
        return top_products;
    }

    public void setTop_products(List<Product> top_products) {
        this.top_products = top_products;
    }

    public List<Category> getSub_categories() {
        return sub_categories;
    }

    public void setSub_categories(List<Category> sub_categories) {
        this.sub_categories = sub_categories;
    }
}
