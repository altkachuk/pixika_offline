package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Media;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.ImageUtils;

/**
 * Created by damien on 20/04/16.
 */
public class CustomerInfoImageViewHolder extends ItemViewHolder<Media> {

    @Inject
    ConfigHelper configHelper;

    @Nullable
    @BindView(R.id.image_view)
    public ImageView imageView;

    @Nullable
    @BindView(R.id.delete_button)
    public ImageView deleteButton;

    @Nullable
    @BindView(R.id.add_button)
    public ImageView addButton;

    public CustomerInfoImageViewHolder(View view) {
        super(view);
    }

    @Override
    public void setItem(Media media) {
        deleteButton.setVisibility(View.GONE);
        addButton.setVisibility(View.GONE);

        ImageUtils.loadCustomerImage(imageView, media);
    }

}