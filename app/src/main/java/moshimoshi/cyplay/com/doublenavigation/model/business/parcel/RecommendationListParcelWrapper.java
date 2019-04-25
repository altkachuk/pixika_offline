package moshimoshi.cyplay.com.doublenavigation.model.business.parcel;

import org.parceler.Parcel;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.ProductRecommendation;

/**
 * Created by damien on 08/06/16.
 */
@Parcel
public class RecommendationListParcelWrapper {

    List<ProductRecommendation> recommendations;

    public List<ProductRecommendation> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<ProductRecommendation> recommendations) {
        this.recommendations = recommendations;
    }

}
