package moshimoshi.cyplay.com.doublenavigation.model.business.history;

import org.parceler.Parcel;

import java.util.Date;

import moshimoshi.cyplay.com.doublenavigation.model.business.Product;

/**
 * Created by romainlebouc on 12/05/2017.
 */
@Parcel
public class ProductHistory implements Comparable<ProductHistory> {

    String sellerId;
    Product product;
    Date date;
    String productId;

    public ProductHistory() {

    }

    public ProductHistory(String sellerId, Product product, Date date) {
        this.sellerId = sellerId;
        this.product = product;
        this.date = date;
        this.productId = product.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductHistory that = (ProductHistory) o;

        return productId != null ? productId.equals(that.productId) : that.productId == null;

    }

    @Override
    public int hashCode() {
        return productId != null ? productId.hashCode() : 0;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int compareTo(ProductHistory another) {
        return - this.getDate().compareTo(another.getDate());
    }
}
