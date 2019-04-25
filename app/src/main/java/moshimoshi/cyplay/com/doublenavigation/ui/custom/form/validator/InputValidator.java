package moshimoshi.cyplay.com.doublenavigation.ui.custom.form.validator;

import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormDescriptor;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormRow;

/**
 * Created by romainlebouc on 29/03/2017.
 */

public interface InputValidator<T> {


    FormValidation validate(T value, PR_FormDescriptor descriptor,PR_FormRow row);


}
