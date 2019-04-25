package moshimoshi.cyplay.com.doublenavigation.utils;

import android.app.Fragment;
import android.content.Context;
import android.support.v4.content.ContextCompat;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalPosition;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.EFeature;
import moshimoshi.cyplay.com.doublenavigation.model.business.PR_ShopGeoLocation;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.Shop;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.AuthenticationConfig;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.CalendarGroup;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.Config;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.ConfigFeature;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.CustomerConfig;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.CustomerFidelityCardConfig;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.CustomerWishlistConfig;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.CustomerWishlistSharingMode;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.DisplayConfig;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.Home;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormRow;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_MenuItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PaymentConfig;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.ProductAutoCompleteConfig;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.ProductCatalogConfig;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.ProductConfig;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.ProductSearchConfig;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.ProductSearchSortConfig;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.RfmIndicator;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.WebViewConfig;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EChannel;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EConfigOrientation;
import moshimoshi.cyplay.com.doublenavigation.model.meta.ResourceSorting;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.constants.ActivityTab;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.constants.ESearchMode;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.CustomerInfoFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.CustomerPaymentsFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.CustomerSalesCustomNocibeFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.CustomerSalesHistoryFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.CustomerWishlistFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.OffersByTypeFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.OffersFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductAttributeReviewsFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductAvailabilityFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductInfosFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductItemFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductRelatedAssortmentFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductRelatedFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductRetailFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductSkuSkusRelatedFragment;
import ninja.cyplay.com.apilibrary.models.meta.ESortingWay;
import ninja.cyplay.com.apilibrary.models.meta.ResourceFieldSorting;

/**
 * Created by damien on 04/03/16.
 */
public class ConfigHelper {

    //TODO : Create a clean function and save results of function for performances

    private Config config;

    public final static String DEFAULT_CATALOGUE_ROOT_CATEGORY = "0";

    private final static Map<String, ActivityTab> CUSTOMER_TABS = new HashMap<>();
    public final static Map<String, ActivityTab> PRODUCT_TABS = new HashMap<>();

    public final static String PROFILE_TAG = "customerProfileTab";
    public final static String WISHLIST_TAG = "wishlistTab";
    public final static String OFFERS_TAG = "offersTab";
    public final static String OFFERS_BY_TYPE_TAG = "offersByTypeTab";
    public final static String PAYMENTS_TAG = "paymentsTab";
    public final static String SALES_HISTORY_TAG = "salesHistoryTab";
    public final static String SALES_CUSTOM_NOCIBE_TAG = "salesCustomNocibeTab";

    public final static String PRODUCT_PROFILE_TAG = "productProfileTab";
    public final static String PRODUCT_RETAIL_PROFILE_TAG = "productRetailProfileTab";
    public final static String PRODUCT_INFOS_TAG = "productInfosTab";
    public final static String PRODUCT_AVAILABILITIES_TAG = "productAvailabilities";
    // Products related to a product
    public final static String PRODUCT_PRODUCTS_RELATED_TAG = "relatedProducts";
    // Skus related to a sku
    public final static String SKU_SKUS_RELATED_TAG = "skuRelatedSkus";
    // Product opinion
    public final static String PRODUCT_ATTRIBUTE_REVIEWS_TAG = "productAttributeReviewsTab";
    // Product assortment
    public final static String PRODUCT_ASSORTMENT_TAG = "relatedAssortment";

    private static Map<String, PR_FormRow> rowMap;

    static {
        CUSTOMER_TABS.put(PROFILE_TAG, new ActivityTab<Fragment>(R.string.customer_contact_details,
                CustomerInfoFragment.class,
                PROFILE_TAG));
        CUSTOMER_TABS.put(WISHLIST_TAG, new ActivityTab<Fragment>(R.string.customer_wishlist,
                CustomerWishlistFragment.class,
                WISHLIST_TAG));
        CUSTOMER_TABS.put(OFFERS_TAG, new ActivityTab<Fragment>(R.string.customer_offers,
                OffersFragment.class,
                OFFERS_TAG));
        CUSTOMER_TABS.put(OFFERS_BY_TYPE_TAG, new ActivityTab<Fragment>(R.string.customer_offers,
                OffersByTypeFragment.class,
                OFFERS_BY_TYPE_TAG));
        CUSTOMER_TABS.put(PAYMENTS_TAG, new ActivityTab<Fragment>(R.string.customer_payments,
                CustomerPaymentsFragment.class,
                PAYMENTS_TAG));
        CUSTOMER_TABS.put(SALES_HISTORY_TAG, new ActivityTab<Fragment>(R.string.customer_sales_history_title,
                CustomerSalesHistoryFragment.class,
                SALES_HISTORY_TAG));
        CUSTOMER_TABS.put(SALES_CUSTOM_NOCIBE_TAG, new ActivityTab<Fragment>(R.string.customer_sales_history_title,
                CustomerSalesCustomNocibeFragment.class,
                SALES_CUSTOM_NOCIBE_TAG));

        PRODUCT_TABS.put(PRODUCT_PROFILE_TAG, new ActivityTab<Fragment>(R.string.product_tab_product,
                ProductItemFragment.class,
                PRODUCT_PROFILE_TAG));

        // Retail version of the product page
        PRODUCT_TABS.put(PRODUCT_RETAIL_PROFILE_TAG, new ActivityTab<Fragment>(R.string.product_tab_product,
                ProductRetailFragment.class,
                PRODUCT_RETAIL_PROFILE_TAG));
        PRODUCT_TABS.put(PRODUCT_INFOS_TAG, new ActivityTab<Fragment>(R.string.product_tab_infos,
                ProductInfosFragment.class,
                PRODUCT_INFOS_TAG));
        PRODUCT_TABS.put(PRODUCT_AVAILABILITIES_TAG, new ActivityTab<Fragment>(R.string.product_tab_availability_title,
                ProductAvailabilityFragment.class,
                PRODUCT_AVAILABILITIES_TAG));
        PRODUCT_TABS.put(PRODUCT_PRODUCTS_RELATED_TAG, new ActivityTab<Fragment>(R.string.product_tab_related_product_title,
                ProductRelatedFragment.class,
                PRODUCT_PRODUCTS_RELATED_TAG));
        PRODUCT_TABS.put(SKU_SKUS_RELATED_TAG, new ActivityTab<Fragment>(R.string.product_tab_related_product_title,
                ProductSkuSkusRelatedFragment.class,
                SKU_SKUS_RELATED_TAG));
        PRODUCT_TABS.put(PRODUCT_ATTRIBUTE_REVIEWS_TAG, new ActivityTab<Fragment>(
                R.string.product_opinion_title,
                ProductAttributeReviewsFragment.class,
                PRODUCT_ATTRIBUTE_REVIEWS_TAG,
                true));
        PRODUCT_TABS.put(PRODUCT_ASSORTMENT_TAG, new ActivityTab<Fragment>(
                R.string.product_assortment_title,
                ProductRelatedAssortmentFragment.class,
                PRODUCT_ASSORTMENT_TAG));

    }

    public ConfigFeature getFeature() {
        return config != null ? config.getFeature() : null;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config PRConfig) {
        if (rowMap != null) {
            rowMap = null;
        }
        this.config = PRConfig;
    }

    public boolean isFeatureActivated(EFeature f) {
        ConfigFeature feature = config.getFeature();
        switch (f) {

            case SCAN:
                if (feature != null && feature.getModules() != null && feature.getModules().getScan() != null)
                    return feature.getModules().getScan().getEnabled();
                break;

            case BASKET:
                if (feature != null && feature.getModules() != null && feature.getModules().getBasket() != null)
                    return feature.getModules().getBasket().getEnabled();
                break;

            case TWEETS:
                if (feature != null && feature.getModules() != null && feature.getModules().getTweets() != null)
                    return feature.getModules().getTweets().getEnabled();
                break;

            case BEACON:
                if (feature != null && feature.getModules() != null && feature.getModules().getBeacon() != null)
                    return feature.getModules().getBeacon().getEnabled();
                break;
        }
        return false;
    }

    public Shop getCurrentShop() {
        Shop shop = null;
        if (config != null && config.getShop() != null && config.getShop().getId() != null) {
            shop = config.getShop();
        }
        return shop;
    }

    public Double getDistanceFromCurrentShop(Shop shop) {
        GeodeticCalculator geoCalc = new GeodeticCalculator();

        Ellipsoid reference = Ellipsoid.WGS84;

        Double distance = null;
        PR_ShopGeoLocation currentShopGeolocation = this.getCurrentShop().getGeolocation();
        if (currentShopGeolocation != null && shop != null && shop.getGeolocation() != null) {
            GlobalPosition pointA = new GlobalPosition(currentShopGeolocation.getLatitude(), currentShopGeolocation.getLongitude(), 0.0); // Point A
            GlobalPosition userPos = new GlobalPosition(shop.getGeolocation().getLatitude(), shop.getGeolocation().getLongitude(), 0.0); // Point B

            distance = geoCalc.calculateGeodeticCurve(reference, userPos, pointA).getEllipsoidalDistance(); // Distance between Point A and Point B

        }
        return distance;


    }

    /**
     * Retrieve all the groups of the calendar
     *
     * @return
     */
    public List<CalendarGroup> getCalendarGroups() {
        ConfigFeature feature = config.getFeature();
        if (feature != null && feature.getCalendar() != null) {
            return feature.getCalendar().getGroups();
        }
        return null;
    }

    /**
     * Retrieve the key for the group
     *
     * @return
     */
    public CalendarGroup getAllCalendarGroup() {
        CalendarGroup result = null;
        List<CalendarGroup> groups = this.getCalendarGroups();
        if (groups != null) {
            for (CalendarGroup group : groups) {
                if (group.getAll() != null && group.getAll()) {
                    result = group;
                    break;
                }
            }
        }
        return result;
    }


    public Home getHome() {
        Home result = null;
        ConfigFeature feature = config.getFeature();
        if (feature != null) {
            result = feature.getPRAHome();
        }
        return result;
    }

    public String formatPrice(Double amount) {
        return formatPrice(getCurrentShop(), amount);
    }

    public String formatPrice(Shop shop, Double amount) {
        if (amount != null && shop != null
                && shop.getCurrency() != null
                && shop.getCurrency().getIso4217() != null) {
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
            currencyFormatter.setCurrency(Currency.getInstance(shop.getCurrency().getIso4217()));
            return currencyFormatter.format(amount);
        }
        return "";
    }

    public List<ActivityTab<Fragment>> getCustomerActivityTabs(Context context) {
        List<ActivityTab<Fragment>> result = new ArrayList<>();
        DisplayConfig displayConfig = this.getFeature().getDisplayConfig();
        if (displayConfig != null) {
            List<PR_MenuItem> pr_menuItems = displayConfig.getCustomerTabs();
            for (PR_MenuItem pr_menuItem : pr_menuItems) {
                if (EConfigOrientation.orientationsMatch(pr_menuItem.getEConfigOrientation(), context.getResources().getConfiguration().orientation)) {
                    ActivityTab activityTab = CUSTOMER_TABS.get(pr_menuItem.getTag());
                    if (activityTab != null) {
                        activityTab.setTitle(pr_menuItem.getLabel() != null ? pr_menuItem.getLabel() : activityTab.getTitle(context));
                        result.add(activityTab);
                    }
                }

            }

        }
        return result;
    }

    public List<ActivityTab<Fragment>> getProductActivityTabs(Context context, CustomerContext customerContext) {
        List<ActivityTab<Fragment>> result = new ArrayList<>();
        DisplayConfig displayConfig = this.getFeature().getDisplayConfig();
        if (displayConfig != null) {
            List<PR_MenuItem> pr_menuItems = displayConfig.getProductTabs();
            for (PR_MenuItem pr_menuItem : pr_menuItems) {
                if (EConfigOrientation.orientationsMatch(pr_menuItem.getEConfigOrientation(), context.getResources().getConfiguration().orientation)) {
                    ActivityTab activityTab = PRODUCT_TABS.get(pr_menuItem.getTag());
                    if (activityTab != null) {
                        if (!activityTab.isShouldCustomerBeIdentified() || (activityTab.isShouldCustomerBeIdentified() && customerContext.isCustomerIdentified())) {
                            activityTab.setTitle(pr_menuItem.getLabel() != null ? pr_menuItem.getLabel() : activityTab.getTitle(context));
                            result.add(activityTab);
                        }
                    }
                }
            }

        }
        return result;
    }


    public boolean shouldGetStockAsynchronously() {
        // By default, stock are not loaded asynchronously
        boolean result = false;
        ProductConfig productConfig = getProductConfig();
        if (productConfig != null && productConfig.getStock().getAsynchronous() != null) {
            result = productConfig.getStock().getAsynchronous();
        }
        return result;
    }

    public double getMinStockDisplay() {
        double result = 0;
        ProductConfig productConfig = getProductConfig();
        if (productConfig != null && productConfig.getStock().getMin_stock_display() != null) {
            result = productConfig.getStock().getMin_stock_display();
        }
        return result;
    }


    public boolean shouldLogOutOnSleepMode() {
        boolean result = false;
        if (getFeature() != null
                && getFeature().getAuthenticationConfig() != null
                && getFeature().getAuthenticationConfig().getAutomatic_log_out() != null
                && getFeature().getAuthenticationConfig().getAutomatic_log_out().getOn_sleep() != null) {
            result = getFeature().getAuthenticationConfig().getAutomatic_log_out().getOn_sleep();
        }
        return result;
    }

    public ProductConfig getProductConfig() {
        return getFeature().getProductConfig();
    }

    public AuthenticationConfig getAuthenticationConfig() {
        return getFeature().getAuthenticationConfig();
    }

    public PaymentConfig getPaymentConfig() {
        PaymentConfig result = null;
        if (getFeature() != null) {
            result = getFeature().getPaymentConfig();
        }
        return result;

    }

    public String getCatalogRootCategoryId() {
        String result = DEFAULT_CATALOGUE_ROOT_CATEGORY;
        ProductConfig productConfig = getProductConfig();
        if (productConfig != null) {
            ProductCatalogConfig productCatalogConfig = productConfig.getCatalog();
            if (productCatalogConfig.getRoot_category_id() != null) {
                result = productCatalogConfig.getRoot_category_id();
            }
        }
        return result;
    }


    public ResourceFieldSorting getCatalogSortConfig(ESearchMode eSearchMode) {
        ProductConfig productConfig = getProductConfig();
        if (productConfig != null) {
            List<ProductSearchSortConfig> catalogSortConfig;
            switch (eSearchMode) {
                case TEXT:
                    ProductCatalogConfig productSearchConfig = productConfig.getCatalog();
                    catalogSortConfig = productSearchConfig.getSort_by();
                    break;
                case CATEGORY:
                case BRAND:
                default:
                    ProductCatalogConfig productCatalogConfig = productConfig.getCatalog();
                    catalogSortConfig = productCatalogConfig.getSort_by();
            }
            if (catalogSortConfig != null) {
                if (!catalogSortConfig.isEmpty()) {
                    return new ResourceFieldSorting(catalogSortConfig.get(0).getField(),
                            catalogSortConfig.get(0).getLabel(),
                            ESortingWay.getESortingWayFromCode(catalogSortConfig.get(0).getWay()));
                }
            }
        }
        return ResourceSorting.PRODUCT_SKUS__POPULARITY_SORTING;
    }


    public CustomerConfig getCustomerConfig() {
        return getFeature().getCustomerConfig();
    }

    public WebViewConfig getWebViewConfig() {
        return getFeature().getWebviewConfig();
    }

    public String getCustomerFidelityCardRegExp() {
        CustomerConfig customerConfig = getCustomerConfig();
        if (customerConfig != null) {
            CustomerFidelityCardConfig customerFidelityCardConfig = customerConfig.getFidelity_card();
            if (customerFidelityCardConfig != null) {
                return customerFidelityCardConfig.getReg_exp_card_number();
            }
        }
        return null;
    }

    public Integer getCustomerRfmColor(String rfm) {
        if (rfm != null) {
            CustomerConfig customerConfig = getCustomerConfig();
            if (customerConfig != null) {
                List<RfmIndicator> rfmIndicators = customerConfig.getRfm_indicators();
                if (rfmIndicators != null) {
                    for (RfmIndicator rfmIndicator : rfmIndicators) {
                        if (rfm.equals(rfmIndicator.getRfm())) {
                            return rfmIndicator.getColor();
                        }
                    }

                }
            }
        }
        return null;
    }

    public CustomerWishlistSharingMode getWishListSharingModeSelection(EChannel eChannel) {
        CustomerWishlistSharingMode result = null;
        CustomerConfig customerConfig = getCustomerConfig();
        if (customerConfig != null) {
            CustomerWishlistConfig customerWishlistConfig = customerConfig.getWishlist();
            if (customerWishlistConfig != null) {
                List<CustomerWishlistSharingMode> customerWishlistSharingModes = customerWishlistConfig.getSharing_modes();
                if (customerWishlistSharingModes != null) {
                    for (CustomerWishlistSharingMode customerWishlistSharingMode : customerWishlistSharingModes) {
                        if (eChannel != null && eChannel.equals(customerWishlistSharingMode.getEChannel()) && customerWishlistSharingMode.getSelection() != null) {
                            result = customerWishlistSharingMode;
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }


    public List<ResourceFieldSorting> getCustomerSalesHistorySorting() {
        List<ResourceFieldSorting> result = new ArrayList<>();
        CustomerConfig customerConfig = getCustomerConfig();
        if (customerConfig != null) {
            List<ProductSearchSortConfig> sort_by = customerConfig.getSales_history().getSort_by();
            if (sort_by != null) {
                for (final ProductSearchSortConfig productSearchSortConfig : sort_by) {
                    result.add(new ResourceFieldSorting(productSearchSortConfig.getField(),
                            productSearchSortConfig.getLabel(),
                            ESortingWay.getESortingWayFromCode(productSearchSortConfig.getWay()),
                            new ReflexiveComparator(productSearchSortConfig.getField(),
                                    productSearchSortConfig.getWay())));
                }
            }
        }
        return result;
    }

    public List<ResourceFieldSorting> getProductSearchSorting(ESearchMode eSearchMode) {
        ProductConfig productConfig = getProductConfig();
        if (productConfig != null) {
            List<ProductSearchSortConfig> productSearchSortConfigs = null;
            switch (eSearchMode) {
                case TEXT:
                    ProductSearchConfig productSearchConfig = productConfig.getSearch();
                    productSearchSortConfigs = productSearchConfig.getSort_by();
                    break;
                case CATEGORY:
                case BRAND:
                default:
                    ProductCatalogConfig productCatalogConfig = productConfig.getCatalog();
                    productSearchSortConfigs = productCatalogConfig.getSort_by();
            }

            if (productSearchSortConfigs != null && !productSearchSortConfigs.isEmpty()) {
                List<ResourceFieldSorting> result = new ArrayList<>();
                for (ProductSearchSortConfig productSearchSortConfig : productSearchSortConfigs) {

                    result.add(new ResourceFieldSorting(productSearchSortConfig.getField(),
                            productSearchSortConfig.getLabel(),
                            ESortingWay.getESortingWayFromCode(productSearchSortConfig.getWay())));


                }
                return result;
            }

        }
        return ResourceSorting.PRODUCT_SORTING;
    }

    // Délire Dior/Chanel
    private final static String[] SPECIAL_COLOR_BRANDS = {"CHANEL", "DIOR"};

    public int getBrandColor(Context context, Product product) {
        if (product != null && product.getBrand() != null && Arrays.asList(SPECIAL_COLOR_BRANDS).contains(product.getBrand().toUpperCase())) {
            return ContextCompat.getColor(context, R.color.special_brands_color);
        } else {
            return ContextCompat.getColor(context, R.color.default_brands_color);
        }
    }

    public ProductAutoCompleteConfig getProductAutoCompleteConfig() {
        ProductAutoCompleteConfig result = null;
        ProductConfig productConfig = getProductConfig();
        if (productConfig != null) {
            result = productConfig.getAutocomplete();
        }
        return result;

    }

    /**
     * Determine if basket features should be displayed (add to basket button, basket in the action bar, etc...
     *
     * @param sellerContext
     * @param customerContext
     * @return
     */
    public boolean shouldDisplayBasketFeatures(SellerContext sellerContext, CustomerContext customerContext) {
        boolean result;
        // Basket
        PaymentConfig paymentConfig = this.getPaymentConfig();

        if (paymentConfig == null) {
            result = sellerContext != null
                    && sellerContext.isSellerIdentified()
                    && this.isFeatureActivated(EFeature.BASKET);
        } else {
            boolean sellerBasketVisible = paymentConfig.getSeller_basket()
                    && this.isFeatureActivated(EFeature.BASKET)
                    && sellerContext != null
                    && sellerContext.isSellerIdentified();

            boolean customerBasketVisible = paymentConfig.getCustomer_basket()
                    && this.isFeatureActivated(EFeature.BASKET)
                    && sellerContext != null
                    && sellerContext.isSellerIdentified()
                    && customerContext != null
                    && customerContext.isCustomerIdentified();

            result = sellerBasketVisible || customerBasketVisible;

        }
        return result;

    }

    private static void initMap(Context context, ConfigHelper configHelper) {
        rowMap = new HashMap<>();
        if (configHelper.getFeature() != null
                && configHelper.getFeature().getFormConfig() != null
                && configHelper.getFeature().getFormConfig().getRows() != null
                && configHelper.getFeature().getFormConfig().getRows().size() > 0)
            for (int i = 0; i < configHelper.getFeature().getFormConfig().getRows().size(); i++) {
                PR_FormRow row = configHelper.getFeature().getFormConfig().getRows().get(i);
                rowMap.put(row.tag, row);
            }
    }

    public PR_FormRow getRowForTag(Context context, String tag, ConfigHelper configHelper) {
        if (rowMap == null) {
            initMap(context, configHelper);
        }
        if (rowMap.containsKey(tag))
            return rowMap.get(tag);
        return null;
    }

    public boolean isCorrectFidelityCardNumber(String barCode) {
        String regExp = getCustomerFidelityCardRegExp();
        if (regExp != null) {
            Pattern p = Pattern.compile(regExp);
            Matcher m = p.matcher(barCode);
            return m.matches();
        } else {
            return true;
        }
    }

}
