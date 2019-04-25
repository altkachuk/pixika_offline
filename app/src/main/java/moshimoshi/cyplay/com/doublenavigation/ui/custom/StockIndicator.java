package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by romainlebouc on 30/01/2017.
 */

public class StockIndicator extends ImageView {

    private int minStockWarning = 1;
    private int minStockError = 1;
    private int stock;

    public StockIndicator(Context context) {
        this(context, null);
    }

    public StockIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StockIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setStock(int stock) {
        this.stock = stock;
        displayPicture();
    }

    private void displayPicture() {
        if (stock >= minStockWarning) {
            Picasso.get().load(R.drawable.ic_in_stock).into(this);
        } else if (stock >= minStockError && stock < minStockWarning) {
            Picasso.get().load(R.drawable.ic_low_stock).into(this);
        } else {
            Picasso.get().load(R.drawable.ic_out_of_stock).into(this);
        }
    }


    public void setMinStockThresholds(int minStockError, int minStockWarning) {
        this.minStockError = minStockError;
        this.minStockWarning = minStockWarning;
        displayPicture();
    }
}
