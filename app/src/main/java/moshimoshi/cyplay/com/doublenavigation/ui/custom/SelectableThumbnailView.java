package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;
import moshimoshi.cyplay.com.doublenavigation.model.business.ImageProductMedia;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EMediaSize;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.ImageUtils;

/**
 * Created by damien on 24/08/16.
 */
public class SelectableThumbnailView extends RelativeLayout {

    @Inject
    ConfigHelper configHelper;

    @BindView(R.id.root)
    View root;

    @BindView(R.id.image_view)
    ImageView imageView;

    private ImageProductMedia imageProductMedia;

    public SelectableThumbnailView(Context context) {
        super(context);
        PlayRetailApp.get(context).inject(this);
        init();
    }

    public SelectableThumbnailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SelectableThumbnailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_selectable_thumbnail_view, this, true);
        // Bind xml
        ButterKnife.bind(this);
    }

    public void setImageProductMedia(ImageProductMedia imageProductMedia) {
        this.imageProductMedia = imageProductMedia;
        if (imageProductMedia != null && imageProductMedia.getMedia() != null) {
            // Thumbnail
            Picasso.get()
                    .load(ImageUtils.getImageUrl(this.getContext(), imageProductMedia.getMedia().getHash(), EMediaSize.PREVIEW, configHelper))
                    .noFade()
                    .fit().centerCrop()
                    .into(imageView);

        }
    }

    public ImageProductMedia getImageProductMedia() {
        return imageProductMedia;
    }

    public void setThumbnailSelected(Boolean selected) {
        if (selected) {
            ShapeDrawable sd = new ShapeDrawable();
            // Specify the ic_shop_offer of ShapeDrawable
            sd.setShape(new RectShape());
            // Specify the border color of ic_shop_offer
            sd.getPaint().setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
            // Set the border width
            int borderSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
            sd.getPaint().setStrokeWidth(borderSize);
            // Specify the style is a Stroke
            sd.getPaint().setStyle(Paint.Style.STROKE);
            // Finally, add the drawable background to TextView
            root.setBackground(sd);
        } else
            root.setBackground(null);
    }

}
