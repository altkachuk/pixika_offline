package moshimoshi.cyplay.com.doublenavigation.model.business.utils;


import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by romainlebouc on 20/01/2017.
 */

@Parcel
public class QuantityStock {

    int quantity;
    boolean inStock;

    public QuantityStock() {

    }

    public QuantityStock(int quantity, boolean inStock) {
        this.quantity = quantity;
        this.inStock = inStock;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isInStock() {
        return inStock;
    }

    public static List<QuantityStock> getQuantityStocks(int realStock, int extraStock, int maxStock, boolean emptyEnable) {

        final List<QuantityStock> quantityStocks = new ArrayList<>();

        int possibleStockQuantity;

        if (emptyEnable) {
            quantityStocks.add(new QuantityStock(0, true));
        }

        for (possibleStockQuantity = 0; possibleStockQuantity < realStock; possibleStockQuantity++) {
            if (possibleStockQuantity + 1 <= maxStock) {
                quantityStocks.add(new QuantityStock(possibleStockQuantity + 1, true));
            }

        }
        for (int i = 0; i < extraStock; i++) {
            if (possibleStockQuantity + i + 1 <= maxStock) {
                quantityStocks.add(new QuantityStock(possibleStockQuantity + i + 1, false));
            }
        }

        return quantityStocks;

    }

    public static int getPositionForQuantity(List<QuantityStock> quantityStocks, Integer quantity) {
        int count = -1;
        if (quantityStocks != null && quantity != null) {
            for (QuantityStock quantityStock : quantityStocks) {
                if (quantityStock.getQuantity() == quantity) {
                    count++;
                    break;
                }
                count++;
            }
        }
        return count;
    }


}