package moshimoshi.cyplay.com.doublenavigation.ui.custom.form;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.validator.BooleanInputValidator;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.validator.FormValidation;
import moshimoshi.cyplay.com.doublenavigation.utils.FormHelper;
import ninja.cyplay.com.apilibrary.utils.ReflectionUtils;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormRow;

/**
 * Created by damien on 03/08/16.
 */
public class FormBooleanLayout extends FormSubView {

    @Nullable
    @BindView(R.id.label_text_view)
    TextView labelTextView;

    @Nullable
    @BindView(R.id.toggle_button)
    SwitchCompat toggleButton;

    private boolean isloaded = false;

    BooleanInputValidator booleanInputValidator;

    public FormBooleanLayout(Context context) {
        this(context, null);
    }

    public FormBooleanLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FormBooleanLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        booleanInputValidator = new BooleanInputValidator(context);
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.form_boolean_layout, this);
        ButterKnife.bind(this);
        isloaded = true;
        updateInfo();
    }

    private void updateInfo() {
        if (isloaded && row != null) {
            // is Editable
            toggleButton.setEnabled(!FormHelper.isDisabled(descriptor));
            // label
            labelTextView.setText(row.getLabel() + (FormHelper.isRequired(descriptor) ? "*" : ""));
            // hint
//            editText.setHint(row.getPlaceholder());
            // set default value
            if (row.getDefaultValue() != null && row.getDefaultValue().length() > 0)
                toggleButton.setChecked("TRUE".equals(row.getDefaultValue().toUpperCase()));
            // set value if edition
            Boolean value = ReflectionUtils.get(customer, row.getTag());
            // in doubt we ic_filter_check
            toggleButton.setChecked(value != null ? value : false);
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

        FormValidation formValidation = booleanInputValidator.validate(toggleButton.isChecked(),descriptor, row );
        if (!formValidation.isValidated()){
            toggleButton.setError(formValidation.getMessage());
            return this;
        }

        if (customer != null && row != null)
            ReflectionUtils.set(customer, row.getTag(), toggleButton.isChecked());
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
