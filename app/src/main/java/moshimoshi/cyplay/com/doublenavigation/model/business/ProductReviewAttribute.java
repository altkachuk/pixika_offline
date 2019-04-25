package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import java.util.List;

import ninja.cyplay.com.apilibrary.models.ModelResource;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AProductReviewAttribute;


/**
 * Created by romainlebouc on 02/01/2017.
 */
@Parcel
@ModelResource
public class ProductReviewAttribute extends PR_AProductReviewAttribute {

    String title ;
    String description ;
    String rating_type;
    List<String> family_ids ;
    List<String> product_ids ;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getRating_type() {
        return rating_type;
    }

    public List<String> getFamily_ids() {
        return family_ids;
    }

    public List<String> getProduct_ids() {
        return product_ids;
    }

}
