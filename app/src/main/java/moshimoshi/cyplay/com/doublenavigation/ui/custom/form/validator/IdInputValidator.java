package moshimoshi.cyplay.com.doublenavigation.ui.custom.form.validator;

import android.content.Context;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormDescriptor;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormRow;
import moshimoshi.cyplay.com.doublenavigation.utils.FormHelper;

/**
 * Created by romainlebouc on 30/03/2017.
 */

public class IdInputValidator implements InputValidator<String> {

    private final Context context;

    public IdInputValidator(Context context) {
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
        return result;

    }

    public Context getContext() {
        return context;
    }
}
