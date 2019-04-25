package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;
import moshimoshi.cyplay.com.doublenavigation.model.business.PicturedResource;
import moshimoshi.cyplay.com.doublenavigation.utils.DesignUtils;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;

/**
 * Created by romainlebouc on 21/04/16.
 */
public class PicturedBadge extends SquareLayout {

    @Inject
    APIValue apiValue;

    @Nullable
    @BindView(R.id.seller_image_view)
    public CircleImageView profilePicture;

    @Nullable
    @BindView(R.id.seller_initiales_container)
    public RelativeLayout initialesContainer;

    @Nullable
    @BindView(R.id.seller_initiales)
    SquareTextView initiales;

    @Nullable
    @BindView(R.id.badge_loading)
    ProgressBar badgeLoading;

    private int borderColor = Color.TRANSPARENT;
    private boolean loading = false;


    public PicturedBadge(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_profile, this, true);
        // Bind xml
        ButterKnife.bind(this);
        // Inject Dependency
        PlayRetailApp.get(context).inject(this);
        // update design
        updateDesign();
        this.setLoading(false);
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void updateDesign() {
        DesignUtils.setBackgroundColor(initiales, ContextCompat.getColor(this.getContext(), R.color.colorAccent));
    }

    public void setProfile(final PicturedResource picturedBadge) {
        if (picturedBadge != null) {
            if (picturedBadge.getImage() != null) {
                profilePicture.setVisibility(View.VISIBLE);
                initialesContainer.setVisibility(View.GONE);

                Picasso.get()
                        .load(picturedBadge.getImage())
                        .placeholder(R.drawable.empty_img)
                        .error(R.drawable.empty_img)
                        .noFade()
                        .into(profilePicture, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {
                                disPlayInitiales(picturedBadge);
                            }
                        });
            } else
                disPlayInitiales(picturedBadge);
        }

    }

    private void disPlayInitiales(PicturedResource picturedBadge) {
        profilePicture.setVisibility(View.GONE);
        initialesContainer.setVisibility(View.VISIBLE);
        initiales.setText(picturedBadge.getInitiales());
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        profilePicture.setBorderColor(borderColor);
    }

    public void setLoading(boolean loading){
        this.loading = loading;
        badgeLoading.setVisibility(loading?VISIBLE:INVISIBLE);
    }

}
