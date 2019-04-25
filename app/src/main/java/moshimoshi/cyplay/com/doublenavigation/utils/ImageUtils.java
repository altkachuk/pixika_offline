package moshimoshi.cyplay.com.doublenavigation.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Media;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.ProductConfig;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EMediaSize;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EMediaSource;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EMediaType;

public class ImageUtils {

    public static String getIconUrl(Context context, String imageName, ConfigHelper configHelper) {
        StringBuilder builder = new StringBuilder();
        if (configHelper.getFeature() != null
                && configHelper.getFeature().getAdditional_parameters() != null
                && configHelper.getFeature().getAdditional_parameters().getImage_service_prefix() != null) {
            String prefix = configHelper.getFeature().getAdditional_parameters().getImage_service_prefix();
            builder.append(prefix);
            builder.append("icon/");
            builder.append(getImageSizeFromDensity( context));
            builder.append("/");
            builder.append(imageName);
        }
        return builder.toString();
    }


    public static String getProductImageUrl(Context context,
                                            EMediaSize eMediaSize,
                                            EMediaSource eMediaSource,
                                            String imageHash,
                                            ConfigHelper configHelper) {
        switch (eMediaSource) {
            case HASH:
                return getImageUrl(context, imageHash, eMediaSize, configHelper);
            case SOURCE:
                return "";
        }
        return null;

    }

    /**
     * Return the complete ic_customer_offer of an Image
     *
     * @param imageHash  hash of the image
     * @param eMediaSize assignedValue must be EMediaSize.FULL or EMediaSize.PREVIEW
     * @return
     */
    public static String getImageUrl(Context context, String imageHash, EMediaSize eMediaSize, ConfigHelper configHelper) {
        StringBuilder builder = new StringBuilder();
        if (configHelper.getFeature() != null
                && configHelper.getFeature().getAdditional_parameters() != null
                && configHelper.getFeature().getAdditional_parameters().getImage_service_prefix() != null) {
            String prefix = configHelper.getFeature().getAdditional_parameters().getImage_service_prefix();
            builder.append(prefix);
            builder.append(eMediaSize.getCode());
            builder.append("/");
            builder.append(imageHash);
        }
        return builder.toString();
    }

    private static String getImageSizeFromDensity(Context context) {
        String imageSize = "3x";
        switch (DisplayUtils.getDpiDensity(context)) {
            case DisplayMetrics.DENSITY_LOW:
                imageSize = "1x";
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                imageSize = "1x";
                break;
            case DisplayMetrics.DENSITY_HIGH:
                imageSize = "2x";
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                imageSize = "3x";
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                imageSize = "4x";
                break;
        }
        return imageSize;
    }


    public static void loadProductMediaPicture(Context context,
                                               Media media,
                                               final ImageView imageView,
                                               EMediaSize eMediaSize,
                                               ConfigHelper configHelper) {
        loadProductMediaPicture(context, media, imageView, null, eMediaSize, configHelper);
    }

    public static void loadProductMediaPicture(Context context,
                                               Media media,
                                               final ImageView imageView,
                                               final View loadingView,
                                               EMediaSize eMediaSize,
                                               ConfigHelper configHelper) {
        if (media != null && context != null && imageView != null) {
            if (loadingView != null) {
                loadingView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
            }
            loadPicture(context, imageView, loadingView, media, eMediaSize, configHelper);

        } else {
            if (loadingView != null) {
                loadingView.setVisibility(View.GONE);
            }

            imageView.setVisibility(View.VISIBLE);
            switch (eMediaSize) {
                case FULL:
                    imageView.setImageResource(R.drawable.ic_no_picture);
                    break;
                default:
                    imageView.setImageResource(R.drawable.ic_no_preview_picture);
            }

        }
    }


    public static void loadProductPicture(Context context,
                                          Product product,
                                          final ImageView imageView,
                                          EMediaSize eMediaSize,
                                          ConfigHelper configHelper) {
        loadProductPicture(context, product, imageView, null, eMediaSize, configHelper);
    }

    public static void loadProductPicture(final Context context,
                                          Product product,
                                          final ImageView imageView,
                                          final View loadingView,
                                          EMediaSize eMediaSize,
                                          ConfigHelper configHelper) {
        Media media = getMediaWithPictureForProduct(context, product, eMediaSize, configHelper);
        loadProductMediaPicture(context, media, imageView, loadingView, eMediaSize, configHelper);

    }


    private static Media getMediaWithPictureForProduct(Context context,
                                                       Product product,
                                                       EMediaSize eMediaSize,
                                                       ConfigHelper configHelper) {
        Media media = null;

        if (product != null) {
            EMediaSource eMediaSource = getProductConfigPictureSource(context, configHelper);
            if (eMediaSource != null && eMediaSource.equals(EMediaSource.SOURCE)) {
                media = Media.getFirstMediaOfTypeForProduct(product, eMediaSize.geteMediaTypeForPicture());
            } else {
                media = Media.getFirstMediaOfTypeForProduct(product, EMediaType.PICTURE);
            }
        }

        return media;
    }

    private static EMediaSource getProductConfigPictureSource(Context context, ConfigHelper configHelper) {
        ProductConfig productConfig = configHelper.getProductConfig();
        if (productConfig != null
                && productConfig.getMedias() != null) {
            return productConfig.getMedias().getPictureSource();
        } else {
            return null;
        }
    }


    private static void loadPicture(Context context,
                                    final ImageView imageView,
                                    final View loadingView,
                                    Media media,
                                    EMediaSize eMediaSize,
                                    ConfigHelper configHelper) {
        if (media != null) {
            String url;
            EMediaSource eMediaSource = getProductConfigPictureSource(context, configHelper);

            if (eMediaSource != null && eMediaSource.equals(EMediaSource.SOURCE)) {
                url = media.getSource();
            } else {
                url = ImageUtils.getImageUrl(context, media.getHash(), eMediaSize, configHelper);
            }
            if (url != null) {
                if (url.toLowerCase().contains("http://") || url.toLowerCase().contains("https://")) {
                    Picasso.get()
                            .load(url)
                            .into(imageView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    if (loadingView != null) {
                                        loadingView.setVisibility(View.GONE);
                                        imageView.setVisibility(View.VISIBLE);
                                    }
                                }

                                @Override
                                public void onError(Exception e) {
                                    if (loadingView != null) {
                                        loadingView.setVisibility(View.GONE);
                                        imageView.setVisibility(View.VISIBLE);
                                    }
                                    imageView.setImageResource(R.drawable.ic_no_picture);
                                }
                            });
                } else {
                    Picasso.get()
                            .load(new File(url))
                            .into(imageView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    if (loadingView != null) {
                                        loadingView.setVisibility(View.GONE);
                                        imageView.setVisibility(View.VISIBLE);
                                    }
                                }

                                @Override
                                public void onError(Exception e) {
                                    if (loadingView != null) {
                                        loadingView.setVisibility(View.GONE);
                                        imageView.setVisibility(View.VISIBLE);
                                    }
                                    imageView.setImageResource(R.drawable.ic_no_picture);
                                }
                            });
                }
            } else if (media.getBase64() != null) {
                try {
                    byte[] decodedString = Base64.decode(media.getHash(), Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    imageView.setImageBitmap(bitmap);
                } catch (Exception e) {
                    if (loadingView != null) {
                        loadingView.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                    }
                    imageView.setImageResource(R.drawable.ic_no_picture);
                }
            } else {
                if (loadingView != null) {
                    loadingView.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                }
                imageView.setImageResource(R.drawable.ic_no_picture);
            }
        }

    }


    public static void loadCustomerImage(final ImageView imageView, Media media) {
        if (media != null) {
            if (media.getSource() != null) {
                String url = media.getSource();
                if (url.toLowerCase().contains("http://") || url.toLowerCase().contains("https://")) {
                    Picasso.get()
                            .load(url)
                            .into(imageView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    imageView.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onError(Exception e) {
                                    imageView.setVisibility(View.VISIBLE);
                                    imageView.setImageResource(R.drawable.ic_no_picture);
                                }
                            });
                } else {
                    Picasso.get()
                            .load(new File(url))
                            .into(imageView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    imageView.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onError(Exception e) {
                                    imageView.setVisibility(View.VISIBLE);
                                    imageView.setImageResource(R.drawable.ic_no_picture);
                                }
                            });
                }
            } else if (media.getBase64() != null) {
                String base64 = media.getBase64();
                try {
                    byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    imageView.setImageBitmap(bitmap);
                    imageView.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.ic_no_picture);
                }
            } else {
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.ic_no_picture);
            }
        }
    }

    public static boolean checkValidCustomerMedia(Media media) {
        if (media != null) {
            if (media.getSource() != null) {
                String url = media.getSource();
                if (url.toLowerCase().contains("http://") || url.toLowerCase().contains("https://")) {
                    return true;
                } else {
                    File file = new File(url);
                    return file.exists();
                }
            } else if (media.getBase64() != null) {
                String base64 = media.getBase64();
                try {
                    byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    if (bitmap != null)
                        return true;
                } catch (Exception e) {
                    return false;
                }
            }
        }

        return false;
    }

    public static Media createCustomerMedia(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        Media media = new Media();
        media.setType(EMediaType.PICTURE.toString());
        media.setBase64(encoded);
        return media;
    }

    public static Media createCustomerMedia(File file) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        Media media = new Media();
        media.setType(EMediaType.PICTURE.toString());
        media.setBase64(encoded);
        return media;
    }
}
