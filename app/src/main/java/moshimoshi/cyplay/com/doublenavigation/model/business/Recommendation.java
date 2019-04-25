package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import java.util.List;

import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ARecommendation;

@Parcel
public class Recommendation extends PR_ARecommendation {

    /*public List<Product> rl;
    public List<Segment> segments;
    public List<Product> rsl;*/

    public String last_update;
    public List<ProductRecommendation> product_recommendations;
    public String id;

    /*
    public Integer rc;
    public Integer rsc;

    public List<Integer> getRl_ids() {

        List<Integer> result = new ArrayList<Integer>();
        if(rl != null) {
            for (Iterator<Product> i = rl.iterator(); i.hasNext(); ) {
                Product item = i.next();
                result.add(item.getSkid());
            }
        }
        return result;
    }
    */

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public List<ProductRecommendation> getProduct_recommendations() {
        return product_recommendations;
    }

    public void setProduct_recommendations(List<ProductRecommendation> product_recommendations) {
        this.product_recommendations = product_recommendations;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
