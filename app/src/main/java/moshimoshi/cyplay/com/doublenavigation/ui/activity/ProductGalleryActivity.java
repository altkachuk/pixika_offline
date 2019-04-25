package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.ImageProductMedia;
import moshimoshi.cyplay.com.doublenavigation.model.business.Media;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sku;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EMediaSize;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EMediaType;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.GalleryPagerAdapter;
import moshimoshi.cyplay.com.doublenavigation.utils.ImageUtils;

/**
 * Created by romainlebouc on 09/01/2017.
 */

public class ProductGalleryActivity extends BaseActivity {

    // Image Gallery
    private GalleryPagerAdapter galleryPagerAdapter;

    @BindView(R.id.product_images_gallery_thumbnails_scrollview)
    HorizontalScrollView thumbnailsScrollView;

    @BindView(R.id.product_images_gallery_thumbnails)
    LinearLayout thumbnails;

    @BindView(R.id.gallery_view_pager)
    ViewPager viewPager;

    Product product;
    Sku sku;
    int selectedPic;

    @Override
    @SuppressWarnings("WrongConstant")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_product_gallery);

        product = Parcels.unwrap(this.getIntent().getParcelableExtra(IntentConstants.EXTRA_PRODUCT));
        sku = Parcels.unwrap(this.getIntent().getParcelableExtra(IntentConstants.EXTRA_SKU));
        selectedPic = this.getIntent().getIntExtra(IntentConstants.EXTRA_SELECTED_PICTURE, 0);

        galleryPagerAdapter = new GalleryPagerAdapter(this, true);
        viewPager.setAdapter(galleryPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                onChooseThumbnail(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        displaySelectedSku(sku);
    }

    @SuppressWarnings("WrongConstant")
    protected void setOrientation(){
        this.setRequestedOrientation(this.getResources().getInteger(R.integer.screen_orientation_gallery));
    }

    private void displaySelectedSku(Sku selectedSku) {
        if (selectedSku != null) {
            // Create list from sku's media
            List<Media> skuMedias = Media.getMediasOfType(selectedSku.getMedias(), EMediaType.PICTURE);
            List<Media> productMedias = Media.getMediasOfType(product.getMedias(), EMediaType.PICTURE);

            List<ImageProductMedia> displayedMedias = new ArrayList<>();
            if (skuMedias != null) {
                for (int i = 0; i < skuMedias.size(); i++)
                    displayedMedias.add(new ImageProductMedia(selectedSku, skuMedias.get(i), i));
            }
            if (productMedias != null) {
                for (int i = 0; i < productMedias.size(); i++) {
                    displayedMedias.add(new ImageProductMedia(selectedSku, productMedias.get(i), i));
                }
            }
            galleryPagerAdapter.setImgs(displayedMedias);
            inflateThumbnails(displayedMedias);
            galleryPagerAdapter.notifyDataSetChanged();
            viewPager.setCurrentItem(selectedPic);
            onChooseThumbnail(selectedPic);
        }
    }

    private void inflateThumbnails(List<ImageProductMedia> displayedMedias) {
        for (int i = 0; i < displayedMedias.size(); i++) {
            View imageLayout = getLayoutInflater().inflate(R.layout.cell_gallery_thumbnail, null);
            final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.img_thumb);
            imageView.setOnClickListener(onChagePageClickListener(imageView, i));
            ImageUtils.loadProductMediaPicture(this, displayedMedias.get(i).getMedia(), imageView, null, EMediaSize.PREVIEW, configHelper);
            //add imageview
            thumbnails.addView(imageLayout);
        }
        if (!displayedMedias.isEmpty()) {
            onChooseThumbnail(0);
        }
    }

    private void onChooseThumbnail(int position) {
        for (int i = 0; i < thumbnails.getChildCount(); i++) {
            View thumbnail = thumbnails.getChildAt(i);
            thumbnail.setAlpha(position == i ? 1.0f : 0.5f);
        }
    }

    private View.OnClickListener onChagePageClickListener(final ImageView imageView, final int i) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(i);
                onChooseThumbnail(i);
            }
        };
    }

    @OnClick(R.id.close_button)
    public void onClose() {
        this.supportFinishAfterTransition();
    }

}
