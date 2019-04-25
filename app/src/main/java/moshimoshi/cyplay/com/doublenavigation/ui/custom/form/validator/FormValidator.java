package moshimoshi.cyplay.com.doublenavigation.ui.custom.form.validator;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_Form;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormDescriptor;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormRow;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormSection;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.FormHelper;
import ninja.cyplay.com.apilibrary.utils.ReflectionUtils;

/**
 * Created by romainlebouc on 30/03/2017.
 */

public class FormValidator {

    private final Context context;
    private final ConfigHelper configHelper;

    public FormValidator(Context context, ConfigHelper configHelper) {
        this.context = context;
        this.configHelper = configHelper;
    }

    public List<FormValidation> validateEntity(PR_Form form, Object customer) {
        List<FormValidation> invalidFormValidationList = new ArrayList<>();
        if (form != null && form.getSectionsDescriptors() != null) {
            for (PR_FormDescriptor formDescriptor : form.getSectionsDescriptors()) {
                String sectionTag = formDescriptor.getTag();
                PR_FormSection section = FormHelper.getSectionForTag(context, sectionTag, configHelper);
                for (PR_FormDescriptor descriptor : section.getRowsDescriptors()) {

                    if (descriptor != null) {
                        // Get Row 'tagged' by the descriptor
                        String tag = descriptor.getTag();
                        PR_FormRow row = configHelper.getRowForTag(context, tag, configHelper);
                        // Add the correct view from the row's tag
                        if (row != null) {
                            FormValidation formValidation = null;
                            switch (row.getType()) {
                                case "id":
                                    IdInputValidator idInputValidator = new IdInputValidator(context);
                                    formValidation = idInputValidator.validate((String) ReflectionUtils.get(customer, row.getTag()), descriptor, row);
                                    break;
                                case "address_auto_complete":
                                case "address2":
                                case "address3":
                                case "address4":
                                case "city_auto_complete":
                                case "postcode_auto_complete":
                                case "single_selector":
                                case "string":
                                    StringInputValidator stringInputValidator = new StringInputValidator(context);
                                    formValidation = stringInputValidator.validate((String) ReflectionUtils.get(customer, row.getTag()), descriptor, row);
                                    break;
                                case "phone":
                                    StringInputValidator phoneInputValidator = new StringInputValidator(context);
                                    formValidation = phoneInputValidator.validate((String) ReflectionUtils.get(customer, row.getTag()), descriptor, row);
                                    break;
                                case "email":
                                    StringInputValidator emailInputValidator = new StringInputValidator(context);
                                    formValidation = emailInputValidator.validate((String) ReflectionUtils.get(customer, row.getTag()), descriptor, row);
                                    break;
                                case "boolean":
                                case "boolean_checkbox":
                                    BooleanInputValidator booleanInputValidator = new BooleanInputValidator(context);
                                    formValidation = booleanInputValidator.validate((Boolean) ReflectionUtils.get(customer, row.getTag()), descriptor, row);
                                    break;
                                case "uniqueChoice":
                                case "uniqueRadioChoice":
                                    DefaultInputValidator defaultInputValidator = new DefaultInputValidator(context);
                                    formValidation = defaultInputValidator.validate((String) ReflectionUtils.get(customer, row.getTag()), descriptor, row);
                                    break;
                                case "date":
                                case "date_auto_fill":
                                case "date_auto_fill_segment_year":
                                    DateInputValidator dateInputValidator = new DateInputValidator(context);
                                    formValidation = dateInputValidator.validate((Date) ReflectionUtils.get(customer, row.getTag()), descriptor, row);
                                    break;
                                case "iban":
                                    IbanInputValidator ibanInputValidator = new IbanInputValidator(context);
                                    formValidation = ibanInputValidator.validate((String) ReflectionUtils.get(customer, row.getTag()), descriptor, row);
                                    break;
                                case "bic":
                                    BicInputValidator bicInputValidator = new BicInputValidator(context);
                                    formValidation = bicInputValidator.validate((String) ReflectionUtils.get(customer, row.getTag()), descriptor, row);
                                    break;
                                case "double":
                                    DoubleInputValidator doubleInputValidator = new DoubleInputValidator(context);
                                    formValidation = doubleInputValidator.validate(String.valueOf(ReflectionUtils.get(customer, row.getTag())), descriptor, row);
                                    break;
                                default:
                                    formValidation = new FormValidation();
                                    formValidation.setValidated(false);

                            }
                            if (formValidation != null && !formValidation.isValidated()) {
                                invalidFormValidationList.add(formValidation);
                            }


                        }
                    }
                }


            }
        }
        return invalidFormValidationList;

    }

}
