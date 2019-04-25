package moshimoshi.cyplay.com.doublenavigation.ui.custom.form;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormDescriptor;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormRow;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.abs.AbstractFormActivity;

/**
 * Created by damien on 11/05/16.
 */
public abstract class FormSubView extends FrameLayout implements IFormFieldValidable {

    protected Object customer = null;
    protected PR_FormDescriptor descriptor = null;
    protected PR_FormRow row = null;
    protected String assignedValue;

    public FormSubView(Context context) {
        this(context, null);
    }

    public FormSubView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FormSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        PlayRetailApp.get(context).inject(this);
        subview_initialize();
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    public void subview_initialize() {
        if (getContext() instanceof AbstractFormActivity) {
            customer = ((AbstractFormActivity) getContext()).getObject();
        }
    }

    public void setDescriptor(PR_FormDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public void setRow(PR_FormRow row) {
        this.row = row;
    }
}
