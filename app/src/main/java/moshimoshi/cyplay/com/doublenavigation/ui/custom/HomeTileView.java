package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_HomeTile;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.ImageUtils;
import moshimoshi.cyplay.com.doublenavigation.utils.StringUtils;

/**
 * Created by romainlebouc on 29/04/2017.
 */

public class HomeTileView extends LinearLayout {

    @Inject
    ConfigHelper configHelper;

    @BindView(R.id.root)
    View root;

    @BindView(R.id.home_tile_icon_image_view)
    public ImageView icon;

    @BindView(R.id.home_tile_title_text_view)
    public TextView title;

    public HomeTileView(Context context) {
        this(context, null);
    }

    public HomeTileView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeTileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.cell_home_tile_item, this, true);
        PlayRetailApp.get(context).inject(this);
        ButterKnife.bind(this, this);

    }


    public void setItem(PR_HomeTile homeTile) {
        if (homeTile != null) {
            String titleText = StringUtils.getStringResourceByName(getContext(), homeTile.getTitle());
            title.setText(titleText);
            // if text color color
            if (homeTile.getTextColor() != null && homeTile.getTextColor().length() > 0) {
                title.setTextColor(Color.parseColor(homeTile.getTextColor()));
            }
            // background touch
            root.setBackgroundResource(R.drawable.ripple_accent_color);
            root.getBackground().setColorFilter(Color.parseColor(homeTile.getBackground().getColor()), PorterDuff.Mode.SRC);
            // icon image

            if (homeTile.getIcon() != null && !homeTile.getIcon().trim().isEmpty()) {
                int id = this.getContext().getResources().getIdentifier(homeTile.getIcon(), "drawable", this.getContext().getPackageName());
                if (id > 0) {
                    Picasso.get()
                            .load(id)
                            .noFade()
                            .into(icon);
                } else {
                    Picasso.get()
                            .load(ImageUtils.getIconUrl(this.getContext(), homeTile.getIcon(), configHelper))
                            .noFade()
                            .into(icon);
                }
            }
        }
    }
}
