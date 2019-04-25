package moshimoshi.cyplay.com.doublenavigation.ui.custom.spinner;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;
import moshimoshi.cyplay.com.doublenavigation.model.business.utils.PurchaseCollectionModeStock;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.StockIndicator;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.StockLabel;

/**
 * Created by romainlebouc on 23/01/2017.
 */

public class PurchaseCollectionStockDropDown extends LinearLayout {

    @Nullable
    @BindView(R.id.stock_indicator)
    StockIndicator stockIndicator;

    @BindView(R.id.product_collection_mode)
    TextView productCollectionMode;

    @BindView(R.id.stock_label)
    StockLabel stockLabel;

    @BindView(R.id.stock_quantity)
    TextView stockQuantity;

    private PurchaseCollectionModeStock purchaseCollectionModeStock;

    public PurchaseCollectionStockDropDown(Context context) {
        this(context, null);
    }

    public PurchaseCollectionStockDropDown(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PurchaseCollectionStockDropDown(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.spinner_dropdown_collection_stock, this, true);
        // Bind xml
        ButterKnife.bind(this);
        // Inject Dependency
        PlayRetailApp.get(context).inject(this);
    }

    public void setPurchaseCollectionModeStock(PurchaseCollectionModeStock purchaseCollectionModeStock) {
        this.purchaseCollectionModeStock = purchaseCollectionModeStock;
        if (this.purchaseCollectionModeStock != null) {
            if (stockIndicator != null) {
                stockIndicator.setStock(purchaseCollectionModeStock.getStock());
            }
            stockLabel.setStock(purchaseCollectionModeStock.getStock());
            productCollectionMode.setText(purchaseCollectionModeStock.getePurchaseCollectionMode().getLabelId());
            stockQuantity.setText(String.valueOf(purchaseCollectionModeStock.getStock()));
        }
    }


}
