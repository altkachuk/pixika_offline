package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.ELikeDislike;

/**
 * Created by romainlebouc on 04/01/2017.
 */

public abstract class LikeDislikeButton extends ImageView {

    protected ELikeDislike state;
    private OpinionSelectionListener opinionSelectionListener;

    public interface OpinionSelectionListener {
        void onOpinionSelected(ELikeDislike eLikeDislike);
    }

    boolean active = false;

    public LikeDislikeButton(Context context) {
        super(context);
        init();
    }

    public LikeDislikeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LikeDislikeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void setOpinionSelectionListener(OpinionSelectionListener opinionSelectionListener) {
        this.opinionSelectionListener = opinionSelectionListener;
    }

    private void init() {
        this.setState(getState(active));
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LikeDislikeButton.this.active = !active;
                setState(getState(active));
                if (opinionSelectionListener != null) {
                    opinionSelectionListener.onOpinionSelected(getState(active));
                }
            }
        });
    }

    protected abstract ELikeDislike getState(boolean active);

    public abstract  void setState(ELikeDislike eLikeDislike);

    public ELikeDislike getState() {
        return state;
    }



}
