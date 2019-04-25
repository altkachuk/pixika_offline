package moshimoshi.cyplay.com.doublenavigation.ui.custom.form;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.utils.FormHelper;
import ninja.cyplay.com.apilibrary.utils.ReflectionUtils;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormRow;

/**
 * Created by damien on 08/01/16.
 */
public class FormDatePickerLayoutLayout extends FormSubView /*implements DatePickerDialog.OnDateSetListener */ {

    @Nullable
    @BindView(R.id.date_text_view)
    EditText dateEditText;

    @Nullable
    @BindView(R.id.label_text_view)
    TextView labelTextView;

    @Nullable
    @BindView(R.id.is_required_text_view)
    TextView requiredTextView;

    private PR_FormRow row;

    private boolean isloaded = false;

    public FormDatePickerLayoutLayout(Context context) {
        super(context);
        init(context);
    }

    public FormDatePickerLayoutLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FormDatePickerLayoutLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.form_date_picker_layout, this);
        ButterKnife.bind(this);
        isloaded = true;
        updateInfo();
    }

    public void setRow(PR_FormRow row) {
        this.row = row;
        updateInfo();
    }

    private void updateInfo() {
        if (isloaded && row != null) {
            dateEditText.setEnabled(!FormHelper.isDisabled(descriptor));
            // Label
            labelTextView.setText(row.getLabel());
            // isRequired
            requiredTextView.setVisibility((FormHelper.isRequired(descriptor) ? VISIBLE : GONE));
            // set hint
            dateEditText.setHint(row.getPlaceholder());
            // set assignedValue from model
            Date value = ReflectionUtils.get(customer, row.getTag());
            if (value != null) {
                String customerDate = dateFormat(value);
                if (customerDate != null && customerDate.length() > 0)
                    dateEditText.setText(customerDate);
            }
        }
    }

    private String dateFormat(Date date) {
        return (getSimpleDateFormat(row).format(date));
    }

    // -------------------------------------------------------------------------------------------
    //                                      View Impl
    // -------------------------------------------------------------------------------------------

    @Override
    public View runValidation() {

        if (row == null || descriptor == null)
            return null;

        if (FormHelper.isRequired(descriptor) && FormHelper.isEditTextEmpty(dateEditText)) {
            dateEditText.setError(getContext().getString(R.string.form_required_error));
            return this;
        }

        Date date = null;
        try {
            date = getSimpleDateFormat(row).parse(dateEditText.getText().toString());
        } catch (ParseException e) {
            if (FormHelper.isRequired(descriptor)) {
                dateEditText.setError(getContext().getString(R.string.form_regex_error));
                return this;
            }
        }

        if (customer != null && row != null)
            ReflectionUtils.set(customer, row.getTag(), date);
        dateEditText.setError(null);
        return null;
    }

    @Override
    public void setValue(Object val) {

    }

    @Override
    public String getValue() {
        return dateEditText.getText().toString();
    }

    private SimpleDateFormat getSimpleDateFormat(PR_FormRow row) {
        return new SimpleDateFormat(row.getValidator() != null && !row.getValidator().isEmpty() ? row.getValidator() : "ddMMyyyy");
    }
}