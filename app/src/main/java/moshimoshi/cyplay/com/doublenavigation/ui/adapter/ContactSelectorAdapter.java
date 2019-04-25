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
import moshimoshi.cyplay.com.doublenavigation.model.business.FeedbackScreen;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.ItemViewHolder;


/**
 * Created by wentongwang on 23/06/2017.
 */

public class ContactSelectorAdapter extends ArrayAdapter<FeedbackScreen> {

    private List<FeedbackScreen> values;
    private Context context;

    public ContactSelectorAdapter(Context context, List<FeedbackScreen> values) {
        super(context, 0, values);
        this.context = context;
        this.values = values;
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

        viewHolder.setItem(values.get(position).getName());

        return convertView;
    }


    class SelectorViewHolder extends ItemViewHolder<String> {

        @BindView(R.id.selector_value)
        TextView value;

        public SelectorViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setItem(String string) {

            if (string != null && !string.isEmpty()) {
                value.setText(string);
                value.setTextColor(ContextCompat.getColor(context, R.color.text_black));
            } else {
                value.setText(context.getString(R.string.problem_list_title));
                value.setTextColor(ContextCompat.getColor(context, R.color.text_hint));
            }


        }
    }
}
