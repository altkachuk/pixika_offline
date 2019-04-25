package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;
import moshimoshi.cyplay.com.doublenavigation.model.business.ImageProductMedia;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EMediaSize;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.ImageUtils;
import uk.co.senab.photoview.PhotoView;

public class GalleryPagerAdapter extends PagerAdapter {

    @Inject
    ConfigHelper configHelper;

    private Context context;
    private List<ImageProductMedia> imgs;
    private LayoutInflater inflater;

    private boolean zoomable = false;

    // constructor
    public GalleryPagerAdapter(Context context) {
        PlayRetailApp.get(context).inject(this);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    public GalleryPagerAdapter(Context context, boolean zoomable) {
        this(context);
        this.zoomable = zoomable;
    }

    public void setImgs(List<ImageProductMedia> imgs) {
        this.imgs = imgs;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View viewLayout, progressView;
        ImageView imageView;
        ImageProductMedia imageProductMedia = imgs.get(position);
        if (zoomable) {
            viewLayout = inflater.inflate(R.layout.fragment_full_screen_zoom_image_view, container, false);
            imageView = (PhotoView) viewLayout.findViewById(R.id.image_view);

        } else {
            viewLayout = inflater.inflate(R.layout.fragment_full_screen_image_view, container, false);
            imageView =  (ImageView) viewLayout.findViewById(R.id.image_view);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.open();
                    }
                }
            });

        }

        progressView = viewLayout.findViewById(R.id.product_item_picture_loading);
        ImageUtils.loadProductMediaPicture(context, imageProductMedia.getMedia(), imageView, progressView, EMediaSize.FULL, configHelper);
        container.addView(viewLayout);
        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return imgs != null ? imgs.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public interface OpenGalleryListener {
        void open();
    }

    private OpenGalleryListener listener = null;

    public void setOpenGalleryListener(OpenGalleryListener listener) {
        this.listener = listener;
    }

}
