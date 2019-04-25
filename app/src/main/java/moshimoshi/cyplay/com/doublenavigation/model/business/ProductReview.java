package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import ninja.cyplay.com.apilibrary.models.ModelResource;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AProductReview;


/**
 * Created by romainlebouc on 02/01/2017.
 */
@Parcel
@ModelResource
public class ProductReview extends PR_AProductReview {

    public transient final static List<String> PRODUCT_REVIEW_PROJECTION = Arrays.asList("id",
            "customer_id",
            "product_id",
            "sku_id",
            "attribute_reviews",
            "last_update",
            "lang");

    private transient boolean updated = false;

    String customer_id;
    String product_id;
    String sku_id;
    Date last_update;
    String lang;
    List<AttributeReview> attribute_reviews;

    public String getId() {
        return id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getSku_id() {
        return sku_id;
    }

    public Date getLast_update() {
        return last_update;
    }

    public String getLang() {
        return lang;
    }

    public List<AttributeReview> getAttribute_reviews() {
        return attribute_reviews;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setSku_id(String sku_id) {
        this.sku_id = sku_id;
    }

    public void setLast_update(Date last_update) {
        this.last_update = last_update;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setAttribute_reviews(List<AttributeReview> attribute_reviews) {
        this.attribute_reviews = attribute_reviews;
    }

    public AttributeReview getAttributeReviewForAttributeId(String attributeId) {
        AttributeReview result = null;
        if (attribute_reviews != null && attributeId != null) {
            for (AttributeReview attributeReview : attribute_reviews) {
                if (attributeId.equals(attributeReview.attribute_id)) {
                    result = attributeReview;
                    break;
                }
            }
        }
        return result;
    }

    public boolean isUpdated() {
        if (attribute_reviews != null) {
            for (AttributeReview attributeReview : attribute_reviews) {
                updated = updated || attributeReview.isUpdated();
            }
        }
        return updated;
    }


    public void resetUpdated() {
        if (attribute_reviews != null) {
            for (AttributeReview attributeReview : attribute_reviews) {
                attributeReview.resetUpdated();
            }
        }
        this.updated = false;
    }


}
