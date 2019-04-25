package moshimoshi.cyplay.com.doublenavigation.model.business.utils;

import android.support.annotation.NonNull;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import moshimoshi.cyplay.com.doublenavigation.model.business.PurchaseCollection;

/**
 * Created by romainlebouc on 27/01/2017.
 */
@Parcel
public class PurchaseCollectionBasketItems {

    PurchaseCollection purchaseCollection;
    List<BasketItemWithStock> basketItemWithStocks;

    PurchaseCollectionBasketItems() {
    }

    public PurchaseCollectionBasketItems(PurchaseCollection purchaseCollection, List<BasketItemWithStock> basketItemWithStocks) {
        this.purchaseCollection = purchaseCollection;
        this.basketItemWithStocks = basketItemWithStocks;
    }

    public static List<PurchaseCollectionBasketItems> getPurchaseCollectionBasketItems(@NonNull List<BasketItemWithStock> notSortedBasketItemWithStocks) {
        Map<PurchaseCollection, List<BasketItemWithStock>> sortedBasketItemWithStocks = new HashMap<>();
        for (BasketItemWithStock basketItemWithStock : notSortedBasketItemWithStocks) {
            List<BasketItemWithStock> purchaseCollectionBasketItemWithStock = sortedBasketItemWithStocks.get(basketItemWithStock.getBasketItem().getPurchaseCollection());
            if (purchaseCollectionBasketItemWithStock == null) {
                purchaseCollectionBasketItemWithStock = new ArrayList<>();
                sortedBasketItemWithStocks.put(basketItemWithStock.getBasketItem().getPurchaseCollection(), purchaseCollectionBasketItemWithStock);
            }
            purchaseCollectionBasketItemWithStock.add(basketItemWithStock);

        }

        List<PurchaseCollectionBasketItems> result = new ArrayList<>();
        for (Map.Entry<PurchaseCollection, List<BasketItemWithStock>> entry : sortedBasketItemWithStocks.entrySet()) {
            result.add(new PurchaseCollectionBasketItems(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    public PurchaseCollection getPurchaseCollection() {
        return purchaseCollection;
    }

    public List<BasketItemWithStock> getBasketItemWithStocks() {
        return basketItemWithStocks;
    }
}
