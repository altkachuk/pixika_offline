package moshimoshi.cyplay.com.doublenavigation.ui.custom.form.validator;

import android.content.Context;

import org.iban4j.BicFormatException;
import org.iban4j.BicUtil;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormDescriptor;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormRow;
import moshimoshi.cyplay.com.doublenavigation.utils.FormHelper;


/**
 * Created by romainlebouc on 29/03/2017.
 */

public class BicInputValidator implements InputValidator<String> {

    private final Context context;

    public BicInputValidator(Context context) {
        this.context = context;
    }

    @Override
    public FormValidation validate(String value, PR_FormDescriptor descriptor, PR_FormRow row) {
        FormValidation result = new FormValidation();

        if (FormHelper.isRequired(descriptor) && FormHelper.isFormValueEmpty(value)) {
            result.setValidated(false);
            result.setMessage(getContext().getString(R.string.form_required_error));
            return result;
        }
        if (!FormHelper.isFormValueEmpty(value) && !FormHelper.testPattern(row, value)) {
            result.setValidated(false);
            result.setMessage(getContext().getString(R.string.form_regex_error));
            return result;
        }

        if (!FormHelper.isFormValueEmpty(value) && !isValidBIC(value)) {
            result.setValidated(false);
            result.setMessage(getContext().getString(R.string.form_regex_error));
            return result;
        }

        return result;

    }

    public Context getContext() {
        return context;
    }

    private boolean isValidBIC(String bic) {
        boolean result;
        try {
            BicUtil.validate(bic);
            result = true;
        } catch (BicFormatException e) {
            result = false;
        }
        return result;
    }
}
