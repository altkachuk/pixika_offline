package moshimoshi.cyplay.com.doublenavigation.utils;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EAddressType;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.FormAddressAutoCompleteLayout;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.FormBicInputLayout;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.FormBirthDayLayout;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.FormBirthDaySegmentYearLayout;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.FormBooleanLayout;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.FormCheckBoxLayout;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.FormCityAutoCompleteLayout;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.FormDatePickerLayoutLayout;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.FormEditTextDoubleInputLayoutLayout;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.FormEditTextInputLayoutLayout;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.FormFidelityCardNumberLayout;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.FormIbanInputLayout;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.FormImagesBase64Layout;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.FormPostalCodeAutoFillLayout;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.FormRadioLayout;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.FormSelector;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.FormSpinnerLayout;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.FormSubView;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_Form;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormDescriptor;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormRow;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormSection;
import ninja.cyplay.com.apilibrary.utils.Constants;

/**
 * Created by damien on 11/05/16.
 */
public class FormHelper {

    // Return Form (Edit or Create form)
    public static PR_Form getFormFromKey(Context context, String formKey, ConfigHelper configHelper) {
        PR_Form createForm = null;
        if (configHelper.getFeature() != null
                && configHelper.getFeature().getFormConfig() != null
                && configHelper.getFeature().getFormConfig().getForms() != null
                && configHelper.getFeature().getFormConfig().getForms().size() > 0) {
            for (int i = 0; i < configHelper.getFeature().getFormConfig().getForms().size(); i++) {
                PR_Form form = configHelper.getFeature().getFormConfig().getForms().get(i);
                if (form.getTag() != null && form.getTag().equals(formKey)) {
                    createForm = form;
                    break;
                }
            }
        }
        return createForm;
    }

    // Return section for a Given tag
    public static PR_FormSection getSectionForTag(Context context, String sectionTag, ConfigHelper configHelper) {
        PR_FormSection foundSection = null;
        if (configHelper.getFeature() != null
                && configHelper.getFeature().getFormConfig() != null
                && configHelper.getFeature().getFormConfig().getSections() != null
                && configHelper.getFeature().getFormConfig().getSections().size() > 0) {
            for (int i = 0; i < configHelper.getFeature().getFormConfig().getSections().size(); i++) {
                PR_FormSection section = configHelper.getFeature().getFormConfig().getSections().get(i);
                if (section.getTag() != null && section.getTag().equals(sectionTag)) {
                    foundSection = section;
                    break;
                }
            }
        }
        return foundSection;
    }

    // -------------------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------------------


    // -------------------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------------------

    public static FormSubView getViewFromTypeName(Context context, String type) {
        FormSubView subViewLayout = null;
        switch (type) {
            case "id":
                subViewLayout = new FormFidelityCardNumberLayout(context);
                break;
            case "string":
                subViewLayout = new FormEditTextInputLayoutLayout(context);
                // TYPE_TEXT_VARIATION_VISIBLE_PASSWORD ==> Hack to turn off autocorrect on samsung devices...
                ((FormEditTextInputLayoutLayout) subViewLayout).setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                break;
            case "phone":
                subViewLayout = new FormEditTextInputLayoutLayout(context);
                ((FormEditTextInputLayoutLayout) subViewLayout).setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            case "email":
                subViewLayout = new FormEditTextInputLayoutLayout(context);
                ((FormEditTextInputLayoutLayout) subViewLayout).setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
            case "boolean":
                subViewLayout = new FormBooleanLayout(context);
                break;
            case "boolean_checkbox":
                subViewLayout = new FormCheckBoxLayout(context);
                break;
            case "uniqueChoice":
                subViewLayout = new FormSpinnerLayout(context);
                break;
            case "uniqueRadioChoice":
                subViewLayout = new FormRadioLayout(context);
                break;
            case "single_selector":
                subViewLayout = new FormSelector(context);
                break;
            case "date":
                subViewLayout = new FormDatePickerLayoutLayout(context);
                break;
            case "date_auto_fill":
                subViewLayout = new FormBirthDayLayout(context);
                break;
            case "date_auto_fill_segment_year":
                subViewLayout = new FormBirthDaySegmentYearLayout(context);
                break;
            case "iban":
                subViewLayout = new FormIbanInputLayout(context);
                break;
            case "bic":
                subViewLayout = new FormBicInputLayout(context);
                break;
            case "double":
                subViewLayout = new FormEditTextDoubleInputLayoutLayout(context);
                ((FormEditTextInputLayoutLayout) subViewLayout).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                break;
            case "address_auto_complete":
                subViewLayout = new FormAddressAutoCompleteLayout(context);
                break;
            case "address2":
                subViewLayout = new FormEditTextInputLayoutLayout(context);
                ((FormEditTextInputLayoutLayout) subViewLayout).setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ((FormEditTextInputLayoutLayout) subViewLayout).setAddressType(EAddressType.ADDRESS2);
                break;
            case "address3":
                subViewLayout = new FormEditTextInputLayoutLayout(context);
                ((FormEditTextInputLayoutLayout) subViewLayout).setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ((FormEditTextInputLayoutLayout) subViewLayout).setAddressType(EAddressType.ADDRESS3);
                break;
            case "address4":
                subViewLayout = new FormEditTextInputLayoutLayout(context);
                ((FormEditTextInputLayoutLayout) subViewLayout).setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ((FormEditTextInputLayoutLayout) subViewLayout).setAddressType(EAddressType.ADDRESS4);
                break;
            case "city_auto_complete":
                subViewLayout = new FormCityAutoCompleteLayout(context);
                break;
            case "postcode_auto_complete":
                subViewLayout = new FormPostalCodeAutoFillLayout(context);
                break;
        }
        return subViewLayout;
    }

    public static FormSubView getViewFromTypeName(Activity context, String type) {
        FormSubView subViewLayout = null;
        switch (type) {
            case "images_base64":
                subViewLayout = new FormImagesBase64Layout(context);
                break;
            default:
                subViewLayout = getViewFromTypeName((Context)context, type);
                break;
        }

        return subViewLayout;

    }

    // -------------------------------------------------------------------------------------------
    //                                      Right(s)
    // -------------------------------------------------------------------------------------------

    public static Boolean isRequired(PR_FormDescriptor descriptor) {
        return descriptor != null && descriptor.getPermissions() != null
                && ((descriptor.getPermissions().intValue() & Constants.FORM_MASK_REQUIRED) == Constants.FORM_MASK_REQUIRED);
    }

    public static Boolean isEditableOnce(PR_FormDescriptor descriptor) {
        if (descriptor != null && descriptor.getPermissions() != null)
            return ((descriptor.getPermissions().intValue() & Constants.FORM_MASK_EDITONCE) == Constants.FORM_MASK_EDITONCE);
        return false;
    }

    public static Boolean isDisabled(PR_FormDescriptor descriptor) {
        if (descriptor != null && descriptor.getPermissions() != null)
            return ((descriptor.getPermissions().intValue() & Constants.FORM_MASK_DISABLED) == Constants.FORM_MASK_DISABLED);
        return false;
    }

    // -------------------------------------------------------------------------------------------
    //                                      Validator(s)
    // -------------------------------------------------------------------------------------------

    public static boolean isEditTextEmpty(EditText editText) {
        if (editText != null)
            return editText.getText().toString().trim().length() == 0;
        return false;
    }

    public static boolean isFormValueEmpty(String string) {
        return string == null || string.trim().length() == 0;
    }

    public static boolean testPattern(PR_FormRow row, String val) {

        if (row != null && row.getValidator() != null && row.getValidator().length() > 0) {
            Pattern p = Pattern.compile(row.getValidator());
            Matcher m = p.matcher(val);
            return m.matches();
        }
        return true;
    }

}
