package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by romainlebouc on 03/08/16.
 */
public class CustomerSpinnerAdapter<T> extends ArrayAdapter {

    LayoutInflater mInflater;
    private List<? extends Map<String, ?>> dataRecieved;

    public CustomerSpinnerAdapter(Context context, @NonNull List<T> objects) {
        super(context, 0, objects);
        mInflater=LayoutInflater.from(context);
    }

    @SuppressWarnings("unchecked")
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.dropdown_customer_layout_title,
                    null);
        }

        return convertView;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if ( position == 0){
            convertView = mInflater.inflate(R.layout.spinner_dropdown_customer_meta_item,
                    null);
        }else{
            convertView = mInflater.inflate(R.layout.spinner_dropdown_resource_sort,
                    null);
        }

        TextView tv = (TextView) convertView;
        tv.setText((String)this.getItem(position));
        return tv;

    }
}