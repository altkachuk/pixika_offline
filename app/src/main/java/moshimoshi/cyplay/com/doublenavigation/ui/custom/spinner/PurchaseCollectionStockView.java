package moshimoshi.cyplay.com.doublenavigation.ui.custom.spinner;

import android.content.Context;
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

/**
 * Created by romainlebouc on 23/01/2017.
 */

public class PurchaseCollectionStockView extends LinearLayout {

    @BindView(R.id.stock_indicator)
    StockIndicator stockIndicator;

    @BindView(R.id.product_collection_mode)
    TextView productCollectionMode;

    private PurchaseCollectionModeStock purchaseCollectionModeStock;

    public PurchaseCollectionStockView(Context context) {
        this(context, null);
    }

    public PurchaseCollectionStockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PurchaseCollectionStockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.spinner_view_collection_stock, this, true);
        // Bind xml
        ButterKnife.bind(this);
        // Inject Dependency
        PlayRetailApp.get(context).inject(this);
    }

    public void setPurchaseCollectionModeStock(PurchaseCollectionModeStock purchaseCollectionModeStock) {
        this.purchaseCollectionModeStock = purchaseCollectionModeStock;
        if (this.purchaseCollectionModeStock != null) {
            stockIndicator.setStock(purchaseCollectionModeStock.getStock());
            productCollectionMode.setText(purchaseCollectionModeStock.getePurchaseCollectionMode().getLabelId());
        }
    }

    public void updateMinStockThresholds(int minStockError,int minStockWarning ){
        stockIndicator.setMinStockThresholds(minStockError,minStockWarning);
    }

    public void changeTextColor(int color){
        productCollectionMode.setTextColor(color);
    }

}
