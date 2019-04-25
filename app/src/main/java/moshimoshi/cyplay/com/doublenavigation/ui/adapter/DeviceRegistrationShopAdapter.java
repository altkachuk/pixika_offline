package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.Shop;

/**
 * Created by romainlebouc on 11/08/16.
 */
public class DeviceRegistrationShopAdapter extends ArrayAdapter<Shop> {

    LayoutInflater mInflater = LayoutInflater.from(this.getContext());
    LayoutInflater  mDropDownInflater = LayoutInflater.from(this.getContext());

    public DeviceRegistrationShopAdapter(Context context, int resource, List<Shop> objects) {
        super(context, resource, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(mInflater, position, convertView, parent,  android.R.layout.simple_dropdown_item_1line);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = mDropDownInflater == null ? mInflater : mDropDownInflater;
        return createViewFromResource(inflater, position, convertView, parent,  android.R.layout.simple_dropdown_item_1line);
    }

    private View createViewFromResource(LayoutInflater inflater, int position, View convertView,
                                        ViewGroup parent, int resource) {

        if (convertView == null) {
            convertView = inflater.inflate(resource, parent, false);
        }
        Shop item = getItem(position);
        ((TextView) convertView).setText(item.toString());
        return convertView;
    }

}