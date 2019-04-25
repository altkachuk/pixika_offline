package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.FormSubView;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.RepetableFormFieldViewHolder;
import moshimoshi.cyplay.com.doublenavigation.utils.FormHelper;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormDescriptor;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormRow;

public class RepetableFormFieldAdapter extends RecyclerView.Adapter<RepetableFormFieldViewHolder> {

    private Context context;

    private PR_FormRow row;
    private PR_FormDescriptor descriptor;

    private List<String> values;

    private List<FormSubView> subViewList;

    public RepetableFormFieldAdapter(Context context, PR_FormRow row, PR_FormDescriptor descriptor) {
        this.context = context;
        this.row = row;
        this.descriptor = descriptor;
        values = new ArrayList<>();
        subViewList = new ArrayList<>();
    }

    public void setValues(List<String> values) {
        if (values != null)
            this.values = values;
        if (values != null && values.size() > 0) {
            for (int i = 0; i < values.size(); i++)
                addOneField();
        }
        // we are on a creation form
        else
            addOneField();
    }

    public List<String> getValues() {
        return values;
    }

    public void addOneField() {
        FormSubView subViewLayout = FormHelper.getViewFromTypeName(context, row.getType());
        if (subViewLayout != null) {
            subViewLayout.setDescriptor(descriptor);
            subViewLayout.setRow(row);
            subViewList.add(subViewLayout);
        }
    }

    public View runValidation() {
        //boolean formValid = true;
        View firstNonValidView = null;
        if (subViewList != null)
            for (int i = 0; i < subViewList.size(); i++) {
                FormSubView subView = subViewList.get(i);
                // id all edited results
                if (values != null && values.size() > i)
                    values.set(i, (String) subView.getValue());
                    // If we pressed Add in the repetable field
                else if (subView.getValue() != null && ((String)subView.getValue()).length() > 0)
                    values.add((String) subView.getValue());
                // Still ic_filter_check the validation
                View viewToValidate = subView.runValidation();
                if (viewToValidate!=null){
                    firstNonValidView = firstNonValidView == null ? viewToValidate : firstNonValidView;
                }

            }
        return firstNonValidView;
    }

    @Override
    public RepetableFormFieldViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_repetable_form_field, parent, false);
        return new RepetableFormFieldViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RepetableFormFieldViewHolder holder, int position) {
        if (holder != null) {
            if (row != null && row.getType() != null) {
                // Extra ic_filter_check for duplicate id
                FormSubView subView = subViewList.get(position);
                ViewGroup parent = (ViewGroup) subView.getParent();
                if (parent != null) {
                    parent.removeView(subView);
                }
                // set value
                if (position < values.size())
                    subView.setValue(values.get(position));
                // remove old view
                holder.container.removeAllViewsInLayout();
                // add field's View
                holder.container.addView(subView);
                // Delete button
                holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // always leave at least one field
                        if (subViewList.size() > 1) {
                            subViewList.remove(holder.getAdapterPosition());
                            notifyDataSetChanged();
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return values != null ? subViewList.size() : 0;
    }

}