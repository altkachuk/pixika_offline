package moshimoshi.cyplay.com.doublenavigation.model.business.utils;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.HomeDelivery;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.ProductStockChoiceConfig;
import moshimoshi.cyplay.com.doublenavigation.model.business.PurchaseCollection;
import moshimoshi.cyplay.com.doublenavigation.model.business.ShopDelivery;
import moshimoshi.cyplay.com.doublenavigation.model.business.ShopNow;
import moshimoshi.cyplay.com.doublenavigation.model.business.Ska;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sku;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.StoreTypeConfig;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPurchaseCollectionMode;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EShopType;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;

/**
 * Created by romainlebouc on 25/01/2017.
 */

@Parcel
public class ThreeSixtyStocks {

    private transient ConfigHelper configHelper;
    List<PurchaseCollectionModeStock> purchaseCollectionModeStocks = new ArrayList<>();

    PurchaseCollectionModeStock shopNowStocks;
    PurchaseCollectionModeStock shopDeliveryStocks;
    PurchaseCollectionModeStock homeDeliveryStocks;

    ShopNow shopNow;
    HomeDelivery homeDelivery;
    ShopDelivery shopDelivery;

    ThreeSixtyStocks() {
    }

    public ThreeSixtyStocks(Sku sku, ConfigHelper configHelper) {
        this.configHelper = configHelper;
        fillPurchaseCollectionModeStocks(sku);
    }

    private void fillPurchaseCollectionModeStocks(
            Sku sku) {
        fillShopNow(sku);
        fillShopDelivery(sku);
        fillHomeDelivery(sku);
    }

    private void fillShopNow(Sku sku) {
        //test if this delivery mode is in the configuration
        if (!configHelper.getPaymentConfig().getPurchase_collection()
                .getAvailable_modes().contains(EPurchaseCollectionMode.SHOP_NOW)) {
            //if not do nothing
            return;
        }


        Ska currentShopSka = sku.getSkaForShop(configHelper.getCurrentShop().getId());
        if (currentShopSka != null) {
            shopNowStocks = new PurchaseCollectionModeStock(
                    EPurchaseCollectionMode.SHOP_NOW,
                    currentShopSka.getStock() != null ? currentShopSka.getStock().intValue() : 0
            );

            fillPossibleQuantityStock(shopNowStocks, EPurchaseCollectionMode.SHOP_NOW);
            purchaseCollectionModeStocks.add(shopNowStocks);
            shopNow = new ShopNow(currentShopSka.getShop());
        }
    }

    private void fillShopDelivery(Sku sku) {

        //test if this delivery mode is in the configuration
        if (!configHelper.getPaymentConfig().getPurchase_collection()
                .getAvailable_modes().contains(EPurchaseCollectionMode.SHOP_DELIVERY)) {
            //if not do nothing
            return;
        }


        StoreTypeConfig storeTypeConfig = configHelper.getPaymentConfig().getPurchase_collection_to_store().getHome();
        List<EShopType> eShopTypes = storeTypeConfig.getStore_type();

        fillRemoteDelivery(sku, eShopTypes, EPurchaseCollectionMode.SHOP_DELIVERY);

    }


    private void fillHomeDelivery(Sku sku) {

        //test if this delivery mode is in the configuration
        if (!configHelper.getPaymentConfig().getPurchase_collection()
                .getAvailable_modes().contains(EPurchaseCollectionMode.HOME_DELIVERY)) {
            //if not do nothing
            return;
        }

        StoreTypeConfig storeTypeConfig = configHelper.getPaymentConfig().getPurchase_collection_to_store().getHome();
        List<EShopType> eShopTypes = storeTypeConfig.getStore_type();

        fillRemoteDelivery(sku, eShopTypes, EPurchaseCollectionMode.HOME_DELIVERY);
    }


    private void fillRemoteDelivery(Sku sku,
                                    List<EShopType> eShopTypes,
                                    EPurchaseCollectionMode ePurchaseCollectionMode) {
        int stock = 0;
        Ska deliveryShopSka = null;
        for (EShopType eShopType : eShopTypes) {
            switch (eShopType) {
                case WAREHOUSE:
                    stock += sku.getWarehouseSka() != null ? sku.getWarehouseSka().getSafeStock() : 0;
                    deliveryShopSka = sku.getWarehouseSka() != null ? sku.getWarehouseSka() : deliveryShopSka;
                    break;
                case WEB:
                    stock += sku.getWebShopSka() != null ? sku.getWebShopSka().getSafeStock() : 0;
                    deliveryShopSka = sku.getWebShopSka() != null ? sku.getWebShopSka() : deliveryShopSka;
            }

        }
        if (stock > 0) {
            switch (ePurchaseCollectionMode) {
                case SHOP_DELIVERY:
                    shopDeliveryStocks = new PurchaseCollectionModeStock(
                            EPurchaseCollectionMode.SHOP_DELIVERY,
                            stock
                    );
                    fillPossibleQuantityStock(shopDeliveryStocks, ePurchaseCollectionMode);
                    purchaseCollectionModeStocks.add(shopDeliveryStocks);
                    shopDelivery = new ShopDelivery(deliveryShopSka.getShop());
                    break;
                case HOME_DELIVERY:
                    homeDeliveryStocks = new PurchaseCollectionModeStock(
                            ePurchaseCollectionMode,
                            stock);
                    fillPossibleQuantityStock(homeDeliveryStocks, ePurchaseCollectionMode);
                    purchaseCollectionModeStocks.add(homeDeliveryStocks);
                    homeDelivery = new HomeDelivery(deliveryShopSka.getShop());
                    break;
            }
        }
    }


    private void fillPossibleQuantityStock(PurchaseCollectionModeStock purchaseCollectionModeStock,
                                           EPurchaseCollectionMode ePurchaseCollectionMode) {
        ProductStockChoiceConfig productStockChoiceConfig = configHelper.getProductConfig().getStock().getProductStockChoiceConfig(ePurchaseCollectionMode);
        List<QuantityStock> quantityStocks = QuantityStock.getQuantityStocks(
                purchaseCollectionModeStock.getStock(),
                productStockChoiceConfig.getExtra(),
                productStockChoiceConfig.getMax(),
                false
        );
        purchaseCollectionModeStock.getQuantityStocks().addAll(quantityStocks);
        if (!quantityStocks.isEmpty()) {
            purchaseCollectionModeStock.setMaxPossibleQuantity(quantityStocks.get(quantityStocks.size() - 1).getQuantity());
        }

    }

    public List<PurchaseCollectionModeStock> getPurchaseCollectionModeStocks() {
        return purchaseCollectionModeStocks;
    }

    public PurchaseCollectionModeStock getShopNowStocks() {
        return shopNowStocks;
    }

    public ShopNow getShopNow() {
        return shopNow;
    }

    public HomeDelivery getHomeDelivery() {
        return homeDelivery;
    }

    public ShopDelivery getShopDelivery() {
        return shopDelivery;
    }

    public PurchaseCollectionModeStock getPurchaseCollectionModeStock(EPurchaseCollectionMode ePurchaseCollectionMode) {
        PurchaseCollectionModeStock result = null;
        if (ePurchaseCollectionMode != null) {
            switch (ePurchaseCollectionMode) {
                case SHOP_NOW:
                    result = shopNowStocks;
                    break;
                case SHOP_DELIVERY:
                    result = shopDeliveryStocks;
                    break;
                case HOME_DELIVERY:
                    result = homeDeliveryStocks;
                    break;
            }
        }
        return result;
    }

    public PurchaseCollection getPurchaseCollection(EPurchaseCollectionMode ePurchaseCollectionMode) {
        PurchaseCollection result = null;

        if (ePurchaseCollectionMode != null) {
            switch (ePurchaseCollectionMode) {
                case SHOP_NOW:
                    result = shopNow;
                    break;
                case SHOP_DELIVERY:
                    result = shopDelivery;
                    break;
                case HOME_DELIVERY:
                    result = homeDelivery;
                    break;
            }
        }
        return result;
    }

}