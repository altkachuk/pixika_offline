package moshimoshi.cyplay.com.doublenavigation.imageloader;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import atproj.cyplay.com.dblibrary.util.FileUtil;
import moshimoshi.cyplay.com.doublenavigation.utils.V1V2Converter;

/**
 * Created by andre on 26-Mar-19.
 */

public class ItemLoader {

    private String name;
    private ItemLoaderListener listener;

    public ItemLoader(String name, ItemLoaderListener listener) {
        this.name = name;
        this.listener = listener;
    }

    public void load(String url) {
        Picasso.get()
                .load(url)
                .networkPolicy(NetworkPolicy.NO_STORE)
                .into(target);
    }

    private final Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            listener.onComplete(bitmap, name);
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            listener.onError();
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            ;
        }
    };


    public interface ItemLoaderListener {
        void onComplete(Bitmap bitmap, String name);
        void onComplete();
        void onError();
    }
}
