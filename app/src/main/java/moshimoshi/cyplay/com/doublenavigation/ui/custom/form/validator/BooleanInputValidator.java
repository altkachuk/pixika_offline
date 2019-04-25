package moshimoshi.cyplay.com.doublenavigation.ui.custom.form.validator;

import android.content.Context;

import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormDescriptor;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormRow;

/**
 * Created by romainlebouc on 30/03/2017.
 */

public class BooleanInputValidator implements InputValidator<Boolean> {

    private final Context context;

    public BooleanInputValidator(Context context) {
        this.context = context;
    }

    @Override
    public FormValidation validate(Boolean value, PR_FormDescriptor descriptor, PR_FormRow row) {
        FormValidation result = new FormValidation();
        return result;
    }

}
