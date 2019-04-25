package moshimoshi.cyplay.com.doublenavigation.ui.custom.form;

import android.view.View;

/**
 * Created by damien on 11/05/16.
 */
public interface IFormFieldValidable {

    View runValidation();

    void setValue(Object val);

    Object getValue();

}
