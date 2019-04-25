package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.R;
import ninja.cyplay.com.apilibrary.models.meta.ResourceFieldSorting;

/**
 * Created by romainlebouc on 18/08/16.
 */

public class ResourceSortSpinnerAdapter<T extends ResourceFieldSorting> extends ArrayAdapter<T> {

    LayoutInflater mInflater;

    public ResourceSortSpinnerAdapter(Context context, @NonNull List<T> objects) {
        super(context, 0, objects);
        mInflater = LayoutInflater.from(context);
    }


    public TextView getTextView(int position, View convertView, ViewGroup parent){
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.spinner_dropdown_resource_sort,
                    null);
        }
        TextView tv = (TextView) convertView;
        tv.setAllCaps(true);
        return tv;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv = getTextView(position, convertView, parent);
        ResourceFieldSorting resourceFieldSorting = this.getItem(position);
        tv.setText(this.getContext().getString(R.string.sort_by, resourceFieldSorting.getLabel(this.getContext())));
        tv.setAllCaps(true);
        return tv;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView tv = getTextView(position, convertView, parent);
        ResourceFieldSorting resourceFieldSorting = this.getItem(position);
        tv.setText(resourceFieldSorting.getLabel(this.getContext()));
        tv.setAllCaps(true);
        return tv;

    }

}