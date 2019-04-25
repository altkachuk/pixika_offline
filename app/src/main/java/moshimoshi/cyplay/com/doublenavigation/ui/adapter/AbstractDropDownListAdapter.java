package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.ItemViewHolder;


/**
 * Created by wentongwang on 09/06/2017.
 */

public abstract class AbstractDropDownListAdapter extends ArrayAdapter<String> implements Filterable {

    private int itemLayoutResource;
    private Context context;
    protected List<String> mResultList;

    public AbstractDropDownListAdapter(Context context, int resource) {
        super(context, resource);
        this.itemLayoutResource = resource;
        this.context = context;
        this.mResultList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mResultList.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SelectorViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(itemLayoutResource, null);

            viewHolder = new SelectorViewHolder(convertView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SelectorViewHolder) convertView.getTag();
        }
        viewHolder.setItem(mResultList.get(position));

        return convertView;
    }


    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null) {
                    // Query the autocomplete API for the entered constraint
                    mResultList = getDropDownResults(constraint);

                    if (mResultList != null) {
                        // Results
                        results.values = mResultList;
                        results.count = mResultList.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    notifyDataSetChanged();
                } else {
                    // The API did not return any results, invalidate the data set.
                    notifyDataSetInvalidated();
                }
            }
        };
    }

    protected abstract List<String> getDropDownResults(CharSequence constraint);

    class SelectorViewHolder extends ItemViewHolder<String> {

        @BindView(R.id.selector_value)
        TextView value;

        public SelectorViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setItem(String result) {
            value.setText(result);
            value.setTextColor(ContextCompat.getColor(context, R.color.text_black));
        }
    }

}
