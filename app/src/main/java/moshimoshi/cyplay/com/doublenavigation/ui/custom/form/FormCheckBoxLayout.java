package moshimoshi.cyplay.com.doublenavigation.ui.custom.form;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormRow;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.validator.BooleanInputValidator;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.validator.FormValidation;
import moshimoshi.cyplay.com.doublenavigation.utils.FormHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.StringUtils;
import ninja.cyplay.com.apilibrary.utils.ReflectionUtils;

/**
 * Created by wentongwang on 30/05/2017.
 */

public class FormCheckBoxLayout extends FormSubView {

    @Nullable
    @BindView(R.id.label_text_view)
    TextView labelTextView;

    @Nullable
    @BindView(R.id.check_box)
    CheckBox checkBox;

    private boolean isloaded = false;

    BooleanInputValidator booleanInputValidator;

    public FormCheckBoxLayout(Context context) {
        this(context, null);
    }

    public FormCheckBoxLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FormCheckBoxLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        booleanInputValidator = new BooleanInputValidator(context);
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.form_boolean_checkbox_layout, this);
        ButterKnife.bind(this);
        isloaded = true;
        updateInfo();
    }

    private void updateInfo() {
        if (isloaded && row != null) {
            // is Editable
            checkBox.setEnabled(!FormHelper.isDisabled(descriptor));
            // label
            String labelText = StringUtils.getStringResourceByName(getContext(), row.getLabel());
            labelTextView.setText(labelText + (FormHelper.isRequired(descriptor) ? "*" : ""));
            // hint
//            editText.setHint(row.getPlaceholder());
            // set default value
            if (row.getDefaultValue() != null && row.getDefaultValue().length() > 0)
                checkBox.setChecked("TRUE".equals(row.getDefaultValue().toUpperCase()));
            // set value if edition
            Boolean value = ReflectionUtils.get(customer, row.getTag());
            // in doubt we ic_filter_check
            if (value != null) {
                checkBox.setChecked(value);
            }
        }
    }

    @Override
    public void setRow(PR_FormRow row) {
        super.setRow(row);
        updateInfo();
    }

    // -------------------------------------------------------------------------------------------
    //                                      View Impl
    // -------------------------------------------------------------------------------------------

    @Override
    public View runValidation() {
        if (row == null || descriptor == null)
            return null;

        FormValidation formValidation = booleanInputValidator.validate(checkBox.isChecked(), descriptor, row);
        if (!formValidation.isValidated()) {
            checkBox.setError(formValidation.getMessage());
            return this;
        }

        if (customer != null && row != null)
            ReflectionUtils.set(customer, row.getTag(), checkBox.isChecked());
        return null;
    }

    @Override
    public void setValue(Object val) {
        assignedValue = (String) val;
        updateInfo();
    }

    @Override
    public String getValue() {
        return "";
    }
}
