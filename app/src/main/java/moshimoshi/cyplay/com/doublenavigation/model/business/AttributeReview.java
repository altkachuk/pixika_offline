package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.ELikeDislike;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.ERatingType;
import ninja.cyplay.com.apilibrary.models.ReadOnlyModelField;

/**
 * Created by romainlebouc on 02/01/2017.
 */
@Parcel
public class AttributeReview {

    private transient boolean updated = false;

    String attribute_id;
    Integer rating;
    String comment;
    String rating_type;

    @ReadOnlyModelField
    ProductReviewAttribute productReviewAttribute;

    public AttributeReview() {

    }

    public AttributeReview(ProductReviewAttribute productReviewAttribute) {
        this.attribute_id = productReviewAttribute.getId();
        this.rating_type = productReviewAttribute.getRating_type();
    }

    public String getAttribute_id() {
        return attribute_id;
    }

    public Integer getRating() {
        return rating;
    }

    public ELikeDislike getELikeDislikeRating() {
        ELikeDislike result = ELikeDislike.NONE;
        if (ERatingType.LIKE == getERatingType() && rating != null) {
            result = ELikeDislike.valueOfELikeDislikeForRating(rating);
        }
        return result;
    }


    public ERatingType getERatingType() {
        return ERatingType.valueOfERatingTypeForTag(rating_type);
    }

    public void setRating(ELikeDislike eLikeDislike) {
        updated = true;
        this.rating = eLikeDislike.getRating();
    }

    public void setComment(String comment) {
        updated = true;
        this.comment = comment;

    }

    public String getComment() {
        return comment;
    }

    public String getRating_type() {
        return rating_type;
    }

    public void setAttribute_id(String attribute_id) {
        this.attribute_id = attribute_id;
    }

    public void setRating_type(String rating_type) {
        this.rating_type = rating_type;
    }

    public ProductReviewAttribute getProductReviewAttribute() {
        return productReviewAttribute;
    }

    public void setProductReviewAttribute(ProductReviewAttribute productReviewAttribute) {
        this.productReviewAttribute = productReviewAttribute;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void resetUpdated() {
        this.updated = false;
    }


}
