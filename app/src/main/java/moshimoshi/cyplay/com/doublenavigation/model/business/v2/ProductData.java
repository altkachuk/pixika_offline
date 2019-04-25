package moshimoshi.cyplay.com.doublenavigation.model.business.v2;

import java.util.List;

/**
 * Created by andre on 22-Mar-19.
 */

public class ProductData {
    List<ProductCategory> categories;
    List<Product> list;

    public List<ProductCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<ProductCategory> categories) {
        this.categories = categories;
    }

    public List<Product> getList() {
        return list;
    }

    public void setList(List<Product> list) {
        this.list = list;
    }
}
