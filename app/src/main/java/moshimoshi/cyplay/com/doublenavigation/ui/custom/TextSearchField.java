package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.utils.CompatUtils;
import moshimoshi.cyplay.com.doublenavigation.utils.StringUtils;

/**
 * Created by romainlebouc on 31/01/2017.
 */

public class TextSearchField extends LinearLayout {

    @BindView(R.id.clear_button)
    Button clear;

    @BindView(R.id.field_value)
    EditText valueField;

    @BindView(R.id.search_button)
    View searchButton;

    private CustomerSearchFieldListener customerSearchFieldListener;
    private String hint;
    private String name;


    public interface CustomerSearchFieldListener {
        void onSearch();
    }

    public TextSearchField(Context context) {
        this(context, null);
    }

    public TextSearchField(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextSearchField(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setCustomerSearchFieldListener(CustomerSearchFieldListener customerSearchFieldListener) {
        this.customerSearchFieldListener = customerSearchFieldListener;
        if (customerSearchFieldListener != null) {
            searchButton.setVisibility(VISIBLE);
            valueField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        TextSearchField.this.customerSearchFieldListener.onSearch();
                        return true;
                    }
                    return false;
                }
            });
            valueField.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        } else {
            searchButton.setVisibility(GONE);
            valueField.setOnEditorActionListener(null);
        }
    }

    private void init() {
        inflate(getContext(), R.layout.custom_customer_search_field, this);
        ButterKnife.bind(this);
        valueField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                clear.setVisibility(s.toString().isEmpty() ? View.INVISIBLE : View.VISIBLE);
            }
        });

        CompatUtils.setDrawableTint( searchButton.getBackground(), ContextCompat.getColor(this.getContext(), R.color.colorAccent));

    }

    @OnClick(R.id.clear_button)
    public void onClearSearch() {
        valueField.setText(StringUtils.EMPTY_STRING);
    }


    @OnClick(R.id.search_button)
    public void onSearch() {
        if (customerSearchFieldListener != null) {
            customerSearchFieldListener.onSearch();
        }
    }



    public void initFieldNames(String name, String hint){
        this.hint = hint;
        this.name = name;
        this.valueField.setHint(hint);
    }

    public EditText getValueField(){
        return valueField;
    }

    public String getValue() {
        return valueField.getText().toString().replaceAll(" ", "");
    }

    public boolean hasMinimumLength(int minimum, boolean acceptEmpty) {

        Boolean ret = valueField.getText().toString().length() >= minimum;
        if (acceptEmpty) {
            ret = ret || valueField.getText().toString().isEmpty();
        }
        valueField.setError(ret ? null : this.getContext().getString(R.string.client_search_generic_error, this.name, minimum));
        return ret;

    }
}
