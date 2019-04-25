package moshimoshi.cyplay.com.doublenavigation.ui.custom.form;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.validator.BicInputValidator;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.validator.FormValidation;
import ninja.cyplay.com.apilibrary.utils.ReflectionUtils;

/**
 * Created by romainlebouc on 19/01/2017.
 */

public class FormBicInputLayout extends FormEditTextInputLayoutLayout {

    BicInputValidator bicInputValidator;

    public FormBicInputLayout(Context context) {
        this(context, null);
    }

    public FormBicInputLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FormBicInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bicInputValidator = new BicInputValidator(context);
    }

    protected void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.form_iban_layout, this);
        ButterKnife.bind(this);
        isloaded = true;
        updateInfo();
    }

    @Override
    public View runValidation() {
        if (row == null || descriptor == null)
            return null;

        FormValidation formValidation = bicInputValidator.validate(editText.getText().toString(),descriptor, row );
        if (!formValidation.isValidated()){
            editText.setError(formValidation.getMessage());
            return this;
        }

        if (customer != null && row != null) {
            ReflectionUtils.set(customer, row.getTag(), getValue());
        }
        editText.setError(null);
        return null;
    }

    @Override
    public String getValue() {
        String value = editText.getText().toString().trim();
        return "".equals(value) ? null : value;
    }

}
