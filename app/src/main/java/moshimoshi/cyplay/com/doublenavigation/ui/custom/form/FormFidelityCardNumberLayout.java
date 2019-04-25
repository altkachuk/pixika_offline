package moshimoshi.cyplay.com.doublenavigation.ui.custom.form;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormRow;
import moshimoshi.cyplay.com.doublenavigation.utils.FormHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.LocalImagesUtils;
import ninja.cyplay.com.apilibrary.utils.ReflectionUtils;

/**
 * Created by damien on 07/01/16.
 */
public class FormFidelityCardNumberLayout extends FormSubView {

    @Nullable
    @BindView(R.id.form_edit_text)
    EditText editText;

    @Nullable
    @BindView(R.id.label_text_view)
    TextView labelTextView;

    private boolean isloaded = false;

    public FormFidelityCardNumberLayout(Context context) {
        super(context);
        init(context);
    }

    public FormFidelityCardNumberLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FormFidelityCardNumberLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.form_fidelity_card_number_layout, this);
        ButterKnife.bind(this);
        isloaded = true;
        updateInfo();
    }

    private void updateInfo() {
        if (isloaded && row != null) {
            // is Editable
            editText.setEnabled(!FormHelper.isDisabled(descriptor));
            // Label
            labelTextView.setText(row.getLabel());
            // hint
            editText.setHint(row.getPlaceholder());
            // set default assignedValue
            if (assignedValue != null)
                editText.setText(assignedValue);
            else {
                try {
                    String value = ReflectionUtils.get(customer, row.getTag());
                    if (value != null)
                        editText.setText(value);
                } catch (Exception e) {
                    Log.e(LocalImagesUtils.class.getName(),e.getMessage(),e);
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
        if (FormHelper.isRequired(descriptor) && FormHelper.isEditTextEmpty(editText)) {
            editText.setError(getContext().getString(R.string.form_required_error));
            return this;
        }
        if (!FormHelper.isEditTextEmpty(editText) && !FormHelper.testPattern(row, editText.getText().toString())) {
            editText.setError(getContext().getString(R.string.form_regex_error));
            return this;
        }
        if (customer != null && row != null)
            ReflectionUtils.set(customer, row.getTag(), getValue());
        editText.setError(null);
        return null ;
    }

    @Override
    public void setValue(Object val) {
        assignedValue = (String) val;
        updateInfo();
    }

    @Override
    public String getValue() {
        return editText.getText().toString();
    }

}