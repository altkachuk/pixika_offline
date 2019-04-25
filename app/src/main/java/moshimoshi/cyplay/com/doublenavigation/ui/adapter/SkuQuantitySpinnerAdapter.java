package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.utils.QuantityStock;

/**
 * Created by romainlebouc on 22/11/16.
 */
public class SkuQuantitySpinnerAdapter extends ArrayAdapter<QuantityStock> {

    private LayoutInflater mInflater;
    private Integer value;
    private int textColor;

    public SkuQuantitySpinnerAdapter(Context context, @NonNull List<QuantityStock> objects, Integer value) {
        super(context, 0, objects);
        mInflater = LayoutInflater.from(context);
        this.value = value;
        this.textColor = ContextCompat.getColor(getContext(), R.color.brownish_grey);
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.spinner_sku_quantity_layout,
                    null);
        }
        TextView editText = (TextView) convertView;
        editText.setText(String.valueOf(value));
        editText.setTextColor(textColor);
        return convertView;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.spinner_dropdown_basket_item_quantity,
                null);

        QuantityStock quantityStock = this.getItem(position);

        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        if (quantityStock != null) {
            tv.setText(String.valueOf(quantityStock.getQuantity()));
        } else {
            tv.setText(null);
        }


        View quantityIndicator = convertView.findViewById(R.id.quantity_indicator);
        quantityIndicator.setBackgroundColor(
                ContextCompat.getColor(this.getContext(), quantityStock.isInStock() ? R.color.success_green : R.color.error_red));

        return convertView;

    }

    public void setValue(Integer value) {
        this.value = value;
        this.notifyDataSetChanged();
    }

    public void changeTextColor(int textColor){
        this.textColor = textColor;
        notifyDataSetChanged();
    }
}
