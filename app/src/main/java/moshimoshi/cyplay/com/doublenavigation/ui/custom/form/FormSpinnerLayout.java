package moshimoshi.cyplay.com.doublenavigation.ui.custom.form;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.utils.FormHelper;
import ninja.cyplay.com.apilibrary.utils.ReflectionUtils;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormRow;

/**
 * Created by damien on 08/01/16.
 */
public class FormSpinnerLayout extends FormSubView {

    @Nullable
    @BindView(R.id.label_text_view)
    TextView label;

    @Nullable
    @BindView(R.id.form_spinner)
    Spinner spinner;

    @Nullable
    @BindView(R.id.is_required_text_view)
    TextView requiredTextView;

    private PR_FormRow row;

    private List<String> displayedList;

    private boolean isloaded = false;

    public FormSpinnerLayout(Context context) {
        super(context);
        init(context);
    }

    public FormSpinnerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FormSpinnerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.form_spinner_layout, this);
        ButterKnife.bind(this);

        displayedList = new ArrayList<>();
        isloaded = true;
        updateInfo();
    }

    private void updateInfo() {
        if (row != null && row.getValues() != null
                && displayedList != null && displayedList.size() == 0)
            for (int i = 0; i < row.getValues().size(); i++)
                displayedList.add(row.getValues().get(i).getLabel());

        if (isloaded && row != null) {
            // set adapter
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), R.layout.form_layout_simple_spinner_item, displayedList);
            dataAdapter.setDropDownViewResource(R.layout.form_layout_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
            // Label
            label.setText(row.getLabel());
            // isRequired
            requiredTextView.setVisibility((FormHelper.isRequired(descriptor) ? VISIBLE : GONE));
            // set assignedValue from model
            String value = ReflectionUtils.get(customer, row.getTag());
            if (value != null && row.getValues().contains(value))
                spinner.setSelection(row.getValues().indexOf(value));
                // select the default assignedValue
            else if (row.getValues() != null && row.getValues().contains(row.getDefaultValue()))
                spinner.setSelection(row.getValues().indexOf(row.getDefaultValue()));
        }
    }

    public void setRow(PR_FormRow row) {
        this.row = row;
        updateInfo();
    }

    // -------------------------------------------------------------------------------------------
    //                                      View Impl
    // -------------------------------------------------------------------------------------------

    @Override
    public View runValidation() {
        if (row == null || descriptor == null)
            return null;
        if (customer != null)
            ReflectionUtils.set(customer, row.getTag(), getValue());
        return null;
    }

    @Override
    public void setValue(Object val) {

    }

    @Override
    public String getValue() {
        if (row != null && row.getValues() != null)
            return row.getValues().get(spinner.getSelectedItemPosition()).getKey();
        return "";
    }
}