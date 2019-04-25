package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.util.AttributeSet;

import com.squareup.picasso.Picasso;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.ELikeDislike;

/**
 * Created by romainlebouc on 04/01/2017.
 */

public class LikeButton extends LikeDislikeButton {
    public LikeButton(Context context) {
        super(context);
    }

    public LikeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LikeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setState(ELikeDislike eLikeDislike) {
        if ( eLikeDislike !=null){
            state = eLikeDislike;
            active = eLikeDislike==ELikeDislike.LIKE &&  eLikeDislike.isActive();
            Picasso.get().load(getIconId(eLikeDislike)).into(this);
        }
    }

    protected ELikeDislike getState(boolean active) {
        return active ? ELikeDislike.LIKE : ELikeDislike.NONE;
    }
    public int getIconId(ELikeDislike eLikeDislike){
        return ELikeDislike.LIKE.equals(eLikeDislike) ? R.drawable.ic_like_checked : R.drawable.ic_like_unchecked ;
    }

}
