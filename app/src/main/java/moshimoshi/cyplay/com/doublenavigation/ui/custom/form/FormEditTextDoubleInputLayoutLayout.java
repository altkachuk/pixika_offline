package moshimoshi.cyplay.com.doublenavigation.ui.custom.form;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.validator.DoubleInputValidator;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.validator.FormValidation;
import moshimoshi.cyplay.com.doublenavigation.utils.FormHelper;
import ninja.cyplay.com.apilibrary.utils.ReflectionUtils;

/**
 * Created by damien on 07/01/16.
 */
public class FormEditTextDoubleInputLayoutLayout extends FormEditTextInputLayoutLayout {

    DoubleInputValidator doubleInputValidator;

    public FormEditTextDoubleInputLayoutLayout(Context context) {
        super(context);
        doubleInputValidator = new DoubleInputValidator(context);
    }

    public FormEditTextDoubleInputLayoutLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        doubleInputValidator = new DoubleInputValidator(context);
    }

    public FormEditTextDoubleInputLayoutLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        doubleInputValidator = new DoubleInputValidator(context);
    }

    // -------------------------------------------------------------------------------------------
    //                                      View Impl
    // -------------------------------------------------------------------------------------------

    protected void updateInfo() {
        if (isloaded && row != null) {
            // is Editable
            editText.setEnabled(!FormHelper.isDisabled(descriptor));
            // Label
            labelTextView.setText(row.getLabel());
            // isRequired
            requiredTextView.setVisibility((FormHelper.isRequired(descriptor) ? VISIBLE : GONE));
            // hint
            editText.setHint(row.getPlaceholder());
            // set default assignedValue
            if (assignedValue != null)
                editText.setText(assignedValue);
            else {
                try {
                    Double value = ReflectionUtils.get(customer, row.getTag());
                    if (value != null)
                        editText.setText(value != null ? value.toString() : null);
                } catch (Exception e) {
                    Log.e(FormEditTextInputLayoutLayout.class.getName(), e.getMessage(), e);
                }
            }
        }
    }

    @Override
    public View runValidation() {
        if (row == null || descriptor == null)
            return null;

        FormValidation formValidation = doubleInputValidator.validate(editText.getText().toString(), descriptor, row);
        if (!formValidation.isValidated()) {
            editText.setError(formValidation.getMessage());
            return this;
        }

        if (customer != null && row != null) {
            ReflectionUtils.set(customer, row.getTag(), getValue().isEmpty() ? null : Double.parseDouble(getValue()));
        }
        editText.setError(null);
        return null;
    }

    @Override
    public String getValue() {
        return editText.getText().toString().trim();
    }

}