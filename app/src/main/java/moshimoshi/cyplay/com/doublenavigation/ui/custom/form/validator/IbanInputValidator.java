package moshimoshi.cyplay.com.doublenavigation.ui.custom.form.validator;

import android.content.Context;

import org.iban4j.IbanFormatException;
import org.iban4j.IbanUtil;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormDescriptor;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormRow;
import moshimoshi.cyplay.com.doublenavigation.utils.FormHelper;

/**
 * Created by romainlebouc on 29/03/2017.
 */

public class IbanInputValidator implements InputValidator<String> {

    private final Context context;

    public IbanInputValidator(Context context) {
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

        if (!FormHelper.isFormValueEmpty(value) && !isValidIBAN(value.replaceAll("\\s+", ""))) {
            result.setValidated(false);
            result.setMessage(getContext().getString(R.string.form_regex_error));
            return result;
        }


        return result;

    }

    public Context getContext() {
        return context;
    }

    private boolean isValidIBAN(String bic) {
        boolean result;
        try {
            IbanUtil.validate(bic);
            result = true;
        } catch (IbanFormatException e) {
            result = false;
        }
        return result;
    }

}
