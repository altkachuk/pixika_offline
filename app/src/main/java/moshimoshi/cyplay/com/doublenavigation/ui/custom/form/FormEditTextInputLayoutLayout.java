package moshimoshi.cyplay.com.doublenavigation.ui.custom.form;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EAddressType;
import moshimoshi.cyplay.com.doublenavigation.model.events.AutoFillPlaceEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.DQEAutoFillPlaceEvent;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.validator.FormValidation;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.validator.StringInputValidator;
import moshimoshi.cyplay.com.doublenavigation.utils.FormHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.StringUtils;
import ninja.cyplay.com.apilibrary.utils.ReflectionUtils;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormRow;

/**
 * Created by damien on 07/01/16.
 */
public class FormEditTextInputLayoutLayout extends FormEventBusView {

    @Nullable
    @BindView(R.id.form_edit_text)
    EditText editText;

    @Nullable
    @BindView(R.id.label_text_view)
    TextView labelTextView;

    @Nullable
    @BindView(R.id.is_required_text_view)
    TextView requiredTextView;

    protected boolean isloaded = false;
    private StringInputValidator stringInputValidator;

    private EAddressType addressType;

    public FormEditTextInputLayoutLayout(Context context) {
        super(context);
        init(context);
    }

    public FormEditTextInputLayoutLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FormEditTextInputLayoutLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    protected void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.form_text_input_layout, this);
        ButterKnife.bind(this);

        isloaded = true;
        stringInputValidator = new StringInputValidator(context);
        updateInfo();
    }

    protected void updateInfo() {
        if (isloaded && row != null) {
            // is Editable
            editText.setEnabled(!FormHelper.isDisabled(descriptor));
            // Label
            String labelText = StringUtils.getStringResourceByName(getContext(), row.getLabel());
            labelTextView.setText(labelText);
            // isRequired
            requiredTextView.setVisibility((FormHelper.isRequired(descriptor) ? VISIBLE : GONE));
            // hint
            String placeholderText = StringUtils.getStringResourceByName(getContext(), row.getPlaceholder());
            editText.setHint(placeholderText);
            // set default assignedValue
            if (assignedValue != null)
                editText.setText(assignedValue);
            else {
                try {
                    String value = ReflectionUtils.get(customer, row.getTag());
                    if (value != null)
                        editText.setText(value);
                } catch (Exception e) {
                    Log.e(FormEditTextInputLayoutLayout.class.getName(), e.getMessage(), e);
                }
            }
        }
    }

    @Override
    public void setRow(PR_FormRow row) {
        super.setRow(row);
        updateInfo();
    }

    public void setInputType(int type) {
        editText.setInputType(type);
    }

    // -------------------------------------------------------------------------------------------
    //                                      View Impl
    // -------------------------------------------------------------------------------------------

    @Override
    public View runValidation() {
        if (row == null || descriptor == null)
            return null;

        FormValidation formValidation = stringInputValidator.validate(editText.getText().toString(), descriptor, row);
        if (!formValidation.isValidated()) {
            editText.setError(formValidation.getMessage());
            return this;
        }

        if (customer != null && row != null) {
            ReflectionUtils.set(customer, row.getTag(), getValue().isEmpty() ? null : getValue());
        }
        editText.setError(null);
        return null;
    }

    @Override
    public void setValue(Object val) {
        assignedValue = (String) val;
        updateInfo();
    }

    @Override
    public String getValue() {
        return editText.getText().toString().trim();
    }

    public void setAddressType(EAddressType type) {
        this.addressType = type;
    }

    @Subscribe
    public void onAutoFillPlaceEvent(AutoFillPlaceEvent autoFillPlaceEvent) {
        if (autoFillPlaceEvent.getPlaceId() == null) {
            return;
        }

        if (addressType == null) {
            return;
        }

        switch (addressType) {
            case ADDRESS4:
            case ADDRESS3:
            case ADDRESS2:
                editText.setText(StringUtils.EMPTY_STRING);
                break;
        }
    }

    @Subscribe
    public void onDQEAutoFillPlaceEvent(DQEAutoFillPlaceEvent event) {
        if (event.getPlaceResult() == null) {
            return;
        }

        if (addressType == null) {
            return;
        }

        switch (addressType) {
            case ADDRESS2:
                editText.setText(event.getPlaceResult().getLieuDit());
                break;
            case ADDRESS3:
            case ADDRESS4:
                editText.setText(StringUtils.EMPTY_STRING);
                break;
        }

    }

}