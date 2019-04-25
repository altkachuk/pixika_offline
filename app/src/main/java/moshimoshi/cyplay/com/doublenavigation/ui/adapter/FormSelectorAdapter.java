package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormRowValue;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.ItemViewHolder;

/**
 * Created by wentongwang on 26/04/2017.
 */

public class FormSelectorAdapter extends ArrayAdapter<PR_FormRowValue> {

    private List<PR_FormRowValue> values;
    private Context context;
    private String placeHolder;


    public FormSelectorAdapter(Context context, List<PR_FormRowValue> values) {
        super(context, 0, values);
        this.context = context;
        PlayRetailApp.get(context).inject(this);
        this.values = values;
    }

    public void setPlaceHolder(String placeHolder) {
        this.placeHolder = placeHolder;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, R.layout.cell_selected_form_selector);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, R.layout.cell_form_selector);
    }

    private View initView(int position, View convertView, int layoutResource) {
        SelectorViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layoutResource, null);

            viewHolder = new SelectorViewHolder(convertView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SelectorViewHolder) convertView.getTag();
        }

        viewHolder.setItem(values.get(position));

        return convertView;
    }


    class SelectorViewHolder extends ItemViewHolder<PR_FormRowValue> {

        @BindView(R.id.selector_value)
        TextView value;

        public SelectorViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setItem(PR_FormRowValue pr_formRowValue) {
            if (pr_formRowValue != null && pr_formRowValue.key != null && pr_formRowValue.label != null) {
                value.setText(pr_formRowValue.getLabel());
                value.setTextColor(ContextCompat.getColor(context, R.color.text_black));
            } else {
                value.setText(placeHolder != null ? placeHolder : context.getString(R.string.selector_default_place_holder));
                value.setTextColor(ContextCompat.getColor(context, R.color.text_hint));
            }
        }
    }
}
