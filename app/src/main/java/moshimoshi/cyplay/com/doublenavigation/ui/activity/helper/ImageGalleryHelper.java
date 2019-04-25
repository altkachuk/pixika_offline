package moshimoshi.cyplay.com.doublenavigation.ui.activity.helper;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.ImageProductMedia;
import moshimoshi.cyplay.com.doublenavigation.model.business.Media;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sku;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EMediaType;
import moshimoshi.cyplay.com.doublenavigation.model.events.ProductSelectedSkuChangeEvent;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.GalleryPagerAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.SelectableThumbnailView;

/**
 * Created by romainlebouc on 07/09/16.
 */
public class ImageGalleryHelper {

    //TODO : put that in the conf if we decide to display more than one picture per sku (habitat...)
    public final static boolean THUMBNAIL_VIEW_ENABLE = false;
    public final static boolean PRODUCT_IMAGE = false;
    public final static boolean ONLY_CURRENT_SKU_IMAGE = true;

    private final Context context;
    private ArrayList<ImageProductMedia> imageProductMedias;
    private final GalleryPagerAdapter galleryAdapter;
    private final EventBus bus;
    private final ViewPager productImagesPager;
    private final LinearLayout thumbnails;

    private Product product;
    private Sku sku;

    public ImageGalleryHelper(Context context, EventBus bus, ViewPager productImagesPager, LinearLayout thumbnails ) {
        this.context = context;
        galleryAdapter = new GalleryPagerAdapter(context);
        this.bus = bus;
        this.productImagesPager = productImagesPager;
        this.thumbnails = thumbnails;
    }

    public void createImageGallery(Product product, Sku sku, LinearLayout thumbnails) {
        this.product = product;
        if (product!= null && sku!=null) {
            imageProductMedias = getGalleryImages(PRODUCT_IMAGE, ONLY_CURRENT_SKU_IMAGE);

            if ( THUMBNAIL_VIEW_ENABLE){
                thumbnails.removeAllViews();
                // create all thumbnails
                for (int i = 0; i < imageProductMedias.size(); i++)
                    addThumbnail(imageProductMedias.get(i), i);
            }

            // Create gallery
            galleryAdapter.setImgs(imageProductMedias);
            galleryAdapter.notifyDataSetChanged();
            // select the first one by default
            if ( THUMBNAIL_VIEW_ENABLE){
                selectThumbnailAtPosition(0);
            }

        }
    }

    @Deprecated
    public void selectThumbnailAtPosition(int position) {
        for (int i = 0 ; i < thumbnails.getChildCount() ; i++) {
            // update status
            ((SelectableThumbnailView)thumbnails.getChildAt(i)).setThumbnailSelected(i == position);
        }
    }

    @Deprecated
    private void addThumbnail(ImageProductMedia image, final int position) {
        final SelectableThumbnailView selectableThumbnailView = new SelectableThumbnailView(this.context);
        selectableThumbnailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the pager position when thumbnail clicked
                productImagesPager.setCurrentItem(position);
                // update thumbnail
                selectThumbnailAtPosition(position);
                // get media clicked
                ImageProductMedia imageProductMedia = selectableThumbnailView.getImageProductMedia();
                // notify sku changes
                if (imageProductMedia != null && imageProductMedia.getSku() != null) {
                    bus.post(new ProductSelectedSkuChangeEvent(imageProductMedia.getSku()));
                }
            }
        });
        // add view to the list
        thumbnails.addView(selectableThumbnailView);
        // display image
        selectableThumbnailView.setImageProductMedia(image);
    }

    private ArrayList<ImageProductMedia> getGalleryImages(boolean productPictures, boolean onlyCurrentSkuPictures) {
        ArrayList<ImageProductMedia> imageProductMediaList = new ArrayList<>();
        // Get all images from product
        if ( productPictures){
            if (product.getMedias() != null) {
                List<Media> medias = Media.getMediasOfType(product.getMedias(), EMediaType.PICTURE);
                for (int i = 0 ; i < medias.size() ; i++) {
                    Media media = medias.get(i);
                    imageProductMediaList.add(new ImageProductMedia(null, media, 0));
                }
            }
        }

        // Get all images from each sku of a product
        if (product != null && product.getSkus() != null) {
            for (int i = 0 ; i < product.getSkus().size() ; i++) {
                Sku sku = product.getSkus().get(i);
                if ( sku !=null  && (!onlyCurrentSkuPictures || (onlyCurrentSkuPictures && sku.equals(this.sku)))){
                    if (sku != null) {
                        List<Media> medias = Media.getMediasOfType(sku.getMedias(), EMediaType.PICTURE);
                        for (int j = 0; j < medias.size(); j++) {
                            Media media = medias.get(j);
                            // I because Sku position
                            imageProductMediaList.add(new ImageProductMedia(sku, media, i));
                        }
                    }
                }
            }
        }
        return imageProductMediaList;
    }

    public GalleryPagerAdapter getGalleryAdapter() {
        return galleryAdapter;
    }

    public ArrayList<ImageProductMedia> getImageProductMedias() {
        return imageProductMedias;
    }

    public void setSku(Sku sku) {
        this.sku = sku;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
