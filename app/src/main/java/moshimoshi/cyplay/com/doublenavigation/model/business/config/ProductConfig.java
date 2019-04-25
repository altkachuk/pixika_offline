package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import android.support.annotation.NonNull;

import org.parceler.Parcel;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EShopCategory;

/**
 * Created by romainlebouc on 25/08/16.
 */
@Parcel
public class ProductConfig {

    ProductStockConfig stock;
    ProductMediasConfig medias;
    ProductPricesConfig prices;
    ProductSearchConfig search;
    ProductCatalogConfig catalog;
    ProductAutoCompleteConfig autocomplete;
    ProductSpecificationsConfig specifications;
    ProductReviewsConfig reviews;
    ProductShippingConfig shipping;
    ProductFeesConfig fees;
    ProductThumbnailConfig thumbnail;

    public
    @NonNull
    ProductStockConfig getStock() {
        if (stock == null) {
            stock = new ProductStockConfig();
        }
        return stock;
    }

    public
    @NonNull
    ProductMediasConfig getMedias() {
        if (medias == null) {
            medias = new ProductMediasConfig();
        }
        return medias;
    }

    public
    @NonNull
    ProductPricesConfig getPrices() {
        if (prices == null) {
            prices = new ProductPricesConfig();
        }
        return prices;
    }

    public
    @NonNull
    ProductSearchConfig getSearch() {
        if (search == null) {
            search = new ProductSearchConfig();
        }
        return search;
    }

    public
    @NonNull
    ProductCatalogConfig getCatalog() {
        if (catalog == null) {
            catalog = new ProductCatalogConfig();
        }
        return catalog;
    }

    public
    @NonNull
    ProductAutoCompleteConfig getAutocomplete() {
        if (autocomplete == null) {
            autocomplete = new ProductAutoCompleteConfig();
        }
        return autocomplete;
    }

    public
    @NonNull
    ProductSpecificationsConfig getSpecifications() {
        if (specifications == null) {
            specifications = new ProductSpecificationsConfig();
        }
        return specifications;
    }

    public
    @NonNull
    ProductReviewsConfig getReviews() {
        if (reviews == null) {
            reviews = new ProductReviewsConfig();
        }
        return reviews;
    }

    public
    @NonNull
    ProductShippingConfig getShipping() {
        if (shipping == null) {
            shipping = new ProductShippingConfig();
        }
        return shipping;
    }
    @NonNull
    public ProductFeesConfig getFees() {
        if (fees == null) {
            fees = new ProductFeesConfig();
        }
        return fees;
    }

    public List<EShopCategory> getEShopCategoryDisplay() {
        List<EShopCategory> result;
        if (getStock() != null) {
            result = getStock().getEShopCategoryDisplay();
        } else {
            result = null;
        }
        return result;
    }

    public ProductThumbnailConfig getThumbnail() {
        return thumbnail != null ? thumbnail : new ProductThumbnailConfig();
    }
}
