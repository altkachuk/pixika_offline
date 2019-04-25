package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import ninja.cyplay.com.apilibrary.models.ModelResource;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ABasketComment;

/**
 * Created by damien on 20/04/15.
 */

@Parcel
@ModelResource
public class BasketComment extends PR_ABasketComment {

    public String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    @Override
    public String toString() {
        return  getComment();
    }
}