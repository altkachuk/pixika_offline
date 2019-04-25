package moshimoshi.cyplay.com.doublenavigation.ui.custom.form;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormRow;
import moshimoshi.cyplay.com.doublenavigation.utils.FormHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.StringUtils;
import ninja.cyplay.com.apilibrary.utils.ReflectionUtils;

/**
 * Created by romainlebouc on 27/09/16.
 */
public class FormRadioLayout extends FormSubView {

    @Nullable
    @BindView(R.id.label_text_view)
    TextView label;

    @Nullable
    @BindView(R.id.form_radio)
    RadioGroup radioGroup;

    @Nullable
    @BindView(R.id.is_required_text_view)
    TextView requiredTextView;

    private List<String> values;

    public FormRadioLayout(Context context) {
        super(context);
        init(context);
    }

    public FormRadioLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FormRadioLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.form_radio_layout, this);
        ButterKnife.bind(this);

        values = new ArrayList<>();
        updateInfo();
    }

    public void setRow(PR_FormRow row) {
        this.row = row;
        updateInfo();
    }


    private void updateInfo() {
        if (row != null && row.getValues() != null
                && values != null && values.size() == 0) {
            for (int i = 0; i < row.getValues().size(); i++) {
                values.add(row.getValues().get(i).getKey());
                RadioButton radioButton = new RadioButton(this.getContext());
                radioButton.setId(i);
                radioButton.setTextColor(ContextCompat.getColor(this.getContext(), R.color.VeryDarkGray));
                radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                radioButton.setLayoutParams(new RadioGroup.LayoutParams(0, RadioGroup.LayoutParams.WRAP_CONTENT, 1f));
                String valueLabel = StringUtils.getStringResourceByName(getContext(), row.getValues().get(i).getLabel());
                radioButton.setText(valueLabel);
                String value = ReflectionUtils.get(customer, row.getTag());
                radioButton.setChecked(value != null && row != null && value.equals(row.getValues().get(i).getKey()));
                radioGroup.addView(radioButton);
            }
            // Label
            String labelText = StringUtils.getStringResourceByName(getContext(), row.getLabel());
            label.setText(labelText);
            // isRequired
            requiredTextView.setVisibility((FormHelper.isRequired(descriptor) ? VISIBLE : GONE));
        }

    }

    @Override
    public View runValidation() {
        if (row == null || descriptor == null)
            return null;
        if (customer != null)
            ReflectionUtils.set(customer, row.getTag(), getValue());
        return null;
    }

    @Override
    public void setValue(Object val) {

    }

    @Override
    public String getValue() {
        if (radioGroup.getCheckedRadioButtonId() <= values.size() && radioGroup.getCheckedRadioButtonId() >= 0) {
            return values.get(radioGroup.getCheckedRadioButtonId());
        }
        return null;
    }
}
