package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.AttributeReview;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductReviewAttribute;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.ELikeDislike;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.FullScreenEditTextActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.DislikeButton;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.LikeButton;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.LikeDislikeButton;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.abs.CommentableFragment;

/**
 * Created by romainlebouc on 03/01/2017.
 */

public class AttributeReviewViewHolder extends ItemViewHolder<AttributeReview> {

    @Nullable
    @BindView(R.id.attribute_review_title)
    TextView title;

    @Nullable
    @BindView(R.id.attribute_review_description)
    TextView description;

    @BindView(R.id.attribute_like)
    LikeButton like;

    @BindView(R.id.attribute_dislike)
    DislikeButton dislike;

    @Nullable
    @BindView(R.id.attribute_review_comment)
    TextView comment;

    private AttributeReview attributeReview;
    private String fragmentTag;

    public AttributeReviewViewHolder(View itemView, final String fragmentTag) {
        super(itemView);
        this.fragmentTag = fragmentTag;
    }

    @Override
    public void setItem(final AttributeReview attributeReview) {
        this.attributeReview = attributeReview;
        if (attributeReview != null) {

            if (comment != null) {
                if (attributeReview.getComment() != null) {
                    comment.setText(attributeReview.getComment());
                    comment.setEnabled(true);
                } else {
                    comment.setText(R.string.review_comment_hint);
                    comment.setEnabled(false);
                }
            }


            like.setState(attributeReview.getELikeDislikeRating());
            dislike.setState(attributeReview.getELikeDislikeRating());

            like.setOpinionSelectionListener(new LikeDislikeButton.OpinionSelectionListener() {
                @Override
                public void onOpinionSelected(ELikeDislike eLikeDislike) {
                    attributeReview.setRating(eLikeDislike);
                    dislike.setState(dislike.getState().getELikeDislike(eLikeDislike));
                }
            });
            dislike.setOpinionSelectionListener(new LikeDislikeButton.OpinionSelectionListener() {
                @Override
                public void onOpinionSelected(ELikeDislike eLikeDislike) {
                    attributeReview.setRating(eLikeDislike);
                    like.setState(like.getState().getELikeDislike(eLikeDislike));
                }
            });
        }
        final ProductReviewAttribute productReviewAttribute = attributeReview.getProductReviewAttribute();
        if (productReviewAttribute != null) {
            title.setText(attributeReview.getProductReviewAttribute().getTitle());
            description.setText(productReviewAttribute.getDescription());
        }

    }

    @OnClick({R.id.attribute_review_container})
    public void onEditComment() {
        Intent intent = new Intent(context, FullScreenEditTextActivity.class);
        intent.putExtra(IntentConstants.EXTRA_EDIT_TEXT, attributeReview.getComment());
        if (attributeReview != null) {
            intent.putExtra(IntentConstants.EXTRA_EDIT_ID, attributeReview.getAttribute_id());
        }
        if (context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(fragmentTag);
            if (fragment != null && fragment.isVisible() && fragment instanceof CommentableFragment) {
                ((CommentableFragment)fragment).triggerWriteComment(attributeReview!=null ? attributeReview.getAttribute_id():null,
                        attributeReview.getComment());
                //fragment.startActivityForResult(intent, IntentConstants.EDIT_TEXT_RESULT);
            }
        }
    }

}
