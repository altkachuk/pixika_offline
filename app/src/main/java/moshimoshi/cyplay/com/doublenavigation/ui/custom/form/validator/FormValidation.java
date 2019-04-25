package moshimoshi.cyplay.com.doublenavigation.ui.custom.form.validator;

/**
 * Created by romainlebouc on 29/03/2017.
 */

public class FormValidation{
    boolean validated = true;
    String message;

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}