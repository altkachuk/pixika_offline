package moshimoshi.cyplay.com.doublenavigation.imageloader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import atproj.cyplay.com.dblibrary.util.FileUtil;
import moshimoshi.cyplay.com.doublenavigation.utils.V1V2Converter;

/**
 * Created by andre on 21-Mar-19.
 */

public class ImageLoader {

    private Context context;
    private ImageLoaderListener listener;

    private List<String> images;
    private int processed;
    private String name;

    private int chunkSize = 100;

    private List<ItemLoader> itemLoaders = new ArrayList<>();

    public ImageLoader(Context context, ImageLoaderListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setChunkSize(int chunkSize) {
        if (chunkSize <= 0) chunkSize = 1;
        if (chunkSize > 1000) chunkSize = 1000;
        this.chunkSize = chunkSize;
    }

    public void load(List<String> images) {
        processed = 0;
        this.images = images;

        loadChunk();
    }

    private void loadChunk() {
        int start = processed;
        int end = (processed + chunkSize) <= images.size() ? (processed + chunkSize) : images.size();

        for (int i = start; i < end; i++) {
            String url = images.get(i);
            String name = V1V2Converter.convertUrlToImageName(url);
            if (FileUtil.checkImageFileInAppDir(context, "images", name)) {
                itemLoaderListener.onComplete();
            } else {
                ItemLoader itemLoader = new ItemLoader(name, itemLoaderListener);
                itemLoaders.add(itemLoader);
                itemLoader.load(url);
            }
        }
    }

    private final ItemLoader.ItemLoaderListener itemLoaderListener = new ItemLoader.ItemLoaderListener() {
        @Override
        public void onComplete(Bitmap bitmap, String name) {
            FileUtil.saveBitmapToAppDir(context, bitmap, "images", name);
            processed++;
            listener.onProcess((double) processed/(double) images.size());
            if (processed == images.size()) {
                itemLoaders.clear();
                listener.onComplete();
            } else if ((processed % chunkSize) == 0) {
                itemLoaders.clear();
                loadChunk();
            }
        }

        @Override
        public void onComplete() {
            processed++;
            if (processed == images.size()) {
                itemLoaders.clear();
                listener.onComplete();
            } else if ((processed % chunkSize) == 0) {
                itemLoaders.clear();
                loadChunk();
            }
        }

        @Override
        public void onError() {
            processed++;
            listener.onProcess((double) processed/(double) images.size());
            if (processed == images.size()) {
                itemLoaders.clear();
                listener.onComplete();
            } else if ((processed % chunkSize) == 0) {
                itemLoaders.clear();
                loadChunk();
            }
        }
    };

    public interface ImageLoaderListener {
        void onProcess(double percentage);
        void onComplete();
    }
}
