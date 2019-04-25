package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.util.AttributeSet;

import com.squareup.picasso.Picasso;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.ELikeDislike;

/**
 * Created by romainlebouc on 04/01/2017.
 */

public class DislikeButton extends LikeDislikeButton {
    public DislikeButton(Context context) {
        super(context);
    }

    public DislikeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DislikeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setState(ELikeDislike eLikeDislike) {
        if ( eLikeDislike !=null){
            state = eLikeDislike;
            active = eLikeDislike==ELikeDislike.DISLIKE &&  eLikeDislike.isActive();
            Picasso.get().load(getIconId(eLikeDislike)).into(this);
        }
    }

    protected ELikeDislike getState(boolean active) {
        return active ? ELikeDislike.DISLIKE : ELikeDislike.NONE;
    }

    public int getIconId(ELikeDislike eLikeDislike){
        return ELikeDislike.DISLIKE.equals(eLikeDislike) ? R.drawable.ic_dislike_checked : R.drawable.ic_dislike_unchecked ;
    }
}
