package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by romainlebouc on 30/01/2017.
 */

public class StockLabel extends TextView {

    private int minStockWarning = 0;

    public StockLabel(Context context) {
        super(context);
    }

    public StockLabel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StockLabel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMinStockWarning(int minStockWarning) {
        this.minStockWarning = minStockWarning;
    }

    public void setStock(int stock) {
        if (stock > minStockWarning) {
            this.setTextColor(ContextCompat.getColor(this.getContext(), R.color.success_green));
        } else if (stock > 0 && stock < minStockWarning) {
            this.setTextColor(ContextCompat.getColor(this.getContext(), R.color.warning_yellow));
        } else {
            this.setTextColor(ContextCompat.getColor(this.getContext(), R.color.error_red));
        }
    }
}
