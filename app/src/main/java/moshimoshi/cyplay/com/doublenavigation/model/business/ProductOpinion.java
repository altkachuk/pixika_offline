package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AProductOpinion;

/**
 * Created by damien on 18/05/15.
 */
@Parcel
public class ProductOpinion extends PR_AProductOpinion {

    public ProductOpinion() {
    }

    public ProductOpinion(Integer opinion) {
        this.opinion = opinion;
    }

    public ProductOpinion(Double opinion) {
        this.opinion = opinion.intValue();
    }

    public Integer opinion;

}
