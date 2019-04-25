package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPurchaseCollectionMode;
import moshimoshi.cyplay.com.doublenavigation.model.business.utils.PurchaseCollectionModeStock;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.spinner.PurchaseCollectionStockDropDown;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.spinner.PurchaseCollectionStockView;

/**
 * Created by romainlebouc on 20/01/2017.
 */

public class PurchaseCollectionStockSpinnerAdapter extends ArrayAdapter<PurchaseCollectionModeStock> {

    EPurchaseCollectionMode ePurchaseCollectionMode;

    private int textColor;

    public PurchaseCollectionStockSpinnerAdapter(Context context, @NonNull List<PurchaseCollectionModeStock> objects, EPurchaseCollectionMode ePurchaseCollectionMode) {
        super(context, 0, objects);
        this.ePurchaseCollectionMode = ePurchaseCollectionMode;
        this.textColor = ContextCompat.getColor(getContext(), R.color.brownish_grey);
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = new PurchaseCollectionStockView(this.getContext());

        PurchaseCollectionStockView purchaseCollectionStockView = (PurchaseCollectionStockView) convertView;
        PurchaseCollectionModeStock purchaseCollectionModeStock = this.getItem(position);
        purchaseCollectionStockView.setPurchaseCollectionModeStock(purchaseCollectionModeStock);
        purchaseCollectionStockView.changeTextColor(textColor);
        return convertView;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        convertView = new PurchaseCollectionStockDropDown(this.getContext());

        PurchaseCollectionStockDropDown purchaseCollectionStockDropDown = (PurchaseCollectionStockDropDown) convertView;
        PurchaseCollectionModeStock purchaseCollectionModeStock = this.getItem(position);
        purchaseCollectionStockDropDown.setPurchaseCollectionModeStock(purchaseCollectionModeStock);

        return convertView;

    }


    public void changeTextColor(int textColor){
        this.textColor = textColor;
        notifyDataSetChanged();
    }
}
