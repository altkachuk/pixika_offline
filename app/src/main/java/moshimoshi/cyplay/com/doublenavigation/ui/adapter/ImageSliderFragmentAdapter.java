package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.Media;
import moshimoshi.cyplay.com.doublenavigation.model.business.parcel.MediaListWrapper;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ImageForGalleryFragment;

public class ImageSliderFragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<Media> images;

    public ImageSliderFragmentAdapter(FragmentManager fm, ArrayList<Media> images) {
        super(fm);
        this.images = images;
    }

    @Override
    public Fragment getItem(int position) {
        ImageForGalleryFragment fragment = new ImageForGalleryFragment();

        Bundle bundle = new Bundle();

        MediaListWrapper mediaListWrapper = new MediaListWrapper();
        mediaListWrapper.setMedias(images);

        List<String> imageUrls = new ArrayList<>();
        for ( Media media : images){
            imageUrls.add(media.getHash());
        }

        bundle.putStringArrayList(IntentConstants.EXTRA_PRODUCT_IMGS, (ArrayList<String>) imageUrls);
        bundle.putInt(IntentConstants.EXTRA_PRODUCT_IMGS_POS, position);

//        bundle.putString(ImageForGalleryFragment.ARG_IMG_URL, images.id(position));
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount() {
        return images != null ? images.size() : 0;
    }

}
