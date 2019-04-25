package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;

/**
 * Created by romainlebouc on 31/01/2017.
 */

public class PhoneSearchField extends TextSearchField {
    public PhoneSearchField(Context context) {
        this(context, null);
    }

    public PhoneSearchField(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhoneSearchField(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.valueField.setInputType(InputType.TYPE_CLASS_PHONE);
    }
}
