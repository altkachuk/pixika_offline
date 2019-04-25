package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sku;

/**
 * Created by romainlebouc on 19/12/2016.
 */

public class SpecificationSelector extends LinearLayout {

    private int size;
    private final List<SpecSelectorView> selectorViewList = new ArrayList<>();

    public SpecificationSelector(Context context) {
        this(context, null);
    }

    public SpecificationSelector(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpecificationSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void fill(Product product, Sku selectedSku) {

        this.removeAllViews();
        // add selector objects
        size = product.getSelectors().size();
        selectorViewList.clear();

        for (int i = 0; i < product.getSelectors().size(); i++) {
            // Create a selector
            SpecSelectorView selectorView = new SpecSelectorView(getContext());
            //selectorView.setBus(bus);
            selectorView.setLevel(i);

            // add the selector to the view
            this.addView(selectorView, i);
            selectorViewList.add(selectorView);
            // fillStocks views
            selectorView.initViews();

            // set Weight to 1, that way selector with share space equally
            if (this.getOrientation() == LinearLayout.VERTICAL) {
                selectorView.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            } else {
                selectorView.setLayoutParams(new LinearLayout.LayoutParams(0, FrameLayout.LayoutParams.WRAP_CONTENT, 1));
            }

        }


        // manually set the first one
        if (this.getChildCount() > 0) {
            ((SpecSelectorView) this.getChildAt(0)).setSpecifications(product.getSpecsForLevel(0));
            ((SpecSelectorView) this.getChildAt(0)).setDefaultSelection(product, selectedSku);
        }
    }

    public int getSize() {
        return size;
    }

    public SpecSelectorView getSpecSelectorViewAt(int i) {
        return selectorViewList.get(i);
    }
}
