package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Patterns;

import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by romainlebouc on 31/01/2017.
 */

public class EmailSearchField extends TextSearchField {
    public EmailSearchField(Context context) {
        this(context, null);
    }

    public EmailSearchField(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmailSearchField(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.valueField.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    }

    public boolean isValidEmail( boolean acceptEmpty) {
        Boolean ret = Patterns.EMAIL_ADDRESS.matcher(valueField.getText()).matches();
        if (acceptEmpty) {
            ret = ret || valueField.getText().toString().isEmpty();
        }
        valueField.setError(ret ? null : this.getContext().getString(R.string.invalid_email));
        return ret;
    }
}
