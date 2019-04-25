package moshimoshi.cyplay.com.doublenavigation.model.business.utils;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPurchaseCollectionMode;

/**
 * Created by romainlebouc on 20/01/2017.
 */
@Parcel
public class PurchaseCollectionModeStock {

    EPurchaseCollectionMode ePurchaseCollectionMode;
    int stock;
    int maxPossibleQuantity;
    List<QuantityStock> quantityStocks = new ArrayList<>();

    PurchaseCollectionModeStock() {

    }

    public PurchaseCollectionModeStock(EPurchaseCollectionMode ePurchaseCollectionMode, int stock) {
        this.ePurchaseCollectionMode = ePurchaseCollectionMode;
        this.stock = stock;
    }

    public EPurchaseCollectionMode getePurchaseCollectionMode() {
        return ePurchaseCollectionMode;
    }

    public int getStock() {
        return stock;
    }

    public static int getPurchaseCollectionModePosition(List<PurchaseCollectionModeStock> purchaseCollectionModeStockList, EPurchaseCollectionMode ePurchaseCollectionMode) {
        int result = -1;
        if (purchaseCollectionModeStockList != null && ePurchaseCollectionMode != null) {
            int count = 0;
            for (PurchaseCollectionModeStock purchaseCollectionModeStock : purchaseCollectionModeStockList) {
                if (ePurchaseCollectionMode.equals(purchaseCollectionModeStock.getePurchaseCollectionMode())) {
                    result = count;
                }
                count++;
            }
        }
        return result;
    }


    public List<QuantityStock> getQuantityStocks() {
        return quantityStocks;
    }

    public int getMaxPossibleQuantity() {
        return maxPossibleQuantity;
    }

    public void setMaxPossibleQuantity(int maxPossibleQuantity) {
        this.maxPossibleQuantity = maxPossibleQuantity;
    }
}
