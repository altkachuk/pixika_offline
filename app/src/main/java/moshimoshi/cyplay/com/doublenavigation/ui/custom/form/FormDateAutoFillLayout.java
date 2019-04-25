package moshimoshi.cyplay.com.doublenavigation.ui.custom.form;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormRow;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.InputFixEditText;
import moshimoshi.cyplay.com.doublenavigation.utils.FormHelper;
import ninja.cyplay.com.apilibrary.utils.ReflectionUtils;

/**
 * Created by wentongwang on 09/03/2017.
 */

public abstract class FormDateAutoFillLayout extends FormSubView {

    protected static final int DAY_INPUT = 0;
    protected static final int MONTH_INPUT = 1;
    protected static final int YEAR_INPUT = 2;
    protected static final String EMPTY_YEAR = "1900";

    @BindView(R.id.label_text_view)
    TextView labelTextView;

    @BindView(R.id.is_required_text_view)
    TextView requiredTextView;

    @BindView(R.id.day_text_view)
    InputFixEditText dayInput;

    @BindView(R.id.month_text_view)
    InputFixEditText monthInput;

    @BindView(R.id.year_text_view)
    InputFixEditText yearInput;

    @BindView(R.id.date_edit_layout)
    View inputLayout;

    protected PR_FormRow row;

    protected boolean isloaded = false;

    public FormDateAutoFillLayout(Context context) {
        this(context, null);
    }

    public FormDateAutoFillLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FormDateAutoFillLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public abstract int getLayoutId();

    private void init(Context context) {
        LayoutInflater.from(context).inflate(getLayoutId(), this);
        ButterKnife.bind(this);
        initListeners();
        updateInfo();

        isloaded = true;
    }

    private void initListeners() {

        dayInput.setOnInputListener(new InputFixEditText.OnInputListener() {
            @Override
            public void deleteFinished() {
                //do nothing
            }

            @Override
            public void inputFinished() {
                //change to next editText
                if (isloaded)
                    inputLayoutRequestFocus(MONTH_INPUT);
            }
        });
        monthInput.setOnInputListener(new InputFixEditText.OnInputListener() {
            @Override
            public void deleteFinished() {
                //back to last editText
                if (isloaded)
                    inputLayoutRequestFocus(DAY_INPUT);
            }

            @Override
            public void inputFinished() {
                //change to next editText
                if (isloaded)
                    inputLayoutRequestFocus(YEAR_INPUT);
            }
        });
        yearInput.setOnInputListener(new InputFixEditText.OnInputListener() {
            @Override
            public void deleteFinished() {
                if (isloaded)
                    inputLayoutRequestFocus(MONTH_INPUT);
            }

            @Override
            public void inputFinished() {
                dayInput.clearFocus();
                monthInput.clearFocus();
//                yearInput.clearFocus();
            }
        });

    }


    public void setRow(PR_FormRow row) {
        this.row = row;
        updateInfo();
    }

    private void updateInfo() {
        if (row != null) {
            dayInput.setEnabled(!FormHelper.isDisabled(descriptor));
            monthInput.setEnabled(!FormHelper.isDisabled(descriptor));
            yearInput.setEnabled(!FormHelper.isDisabled(descriptor));
            // Label
            labelTextView.setText(row.getLabel());
            // isRequired
            requiredTextView.setVisibility((FormHelper.isRequired(descriptor) ? VISIBLE : GONE));
            // set assignedValue from model
            Date value = ReflectionUtils.get(customer, row.getTag());
            if (value != null) {
                SimpleDateFormat format = new SimpleDateFormat("dd,MM,yyyy");
                String customerDate = format.format(value);
                if (customerDate != null && customerDate.length() > 0) {
                    dayInput.setText(getDay(customerDate));
                    monthInput.setText(getMonth(customerDate));
                    yearInput.setText(getYear(customerDate));

                    dayInput.clearFocus();
                    monthInput.clearFocus();
                    yearInput.clearFocus();
                }
            }
        }
    }


    private String getDay(String customerDate) {
        String[] date = customerDate.split(",");
        return date[DAY_INPUT];
    }

    private String getMonth(String customerDate) {
        String[] date = customerDate.split(",");
        return date[MONTH_INPUT];
    }

    private String getYear(String customerDate) {
        String[] date = customerDate.split(",");
        if (date.length - 1 >= YEAR_INPUT)
            return date[YEAR_INPUT].equals(EMPTY_YEAR) ? "" : date[YEAR_INPUT];
        else
            return "";
    }

    protected String getDate() {
        return dayInput.getText().toString() + "," +
                monthInput.getText().toString() + "," +
                yearInput.getText().toString();
    }

    protected String dateFormat(Date date) {
        return (getSimpleDateFormat().format(date));
    }

    protected SimpleDateFormat getSimpleDateFormat() {
        return new SimpleDateFormat("dd,MM,yyyy");
    }

    protected void autoFill() {
        if (dayInput.getText().toString().length() == 1) {
            String str = "0" + dayInput.getText().toString();
            dayInput.setText(str);
        }

        if (monthInput.getText().toString().length() == 1) {
            String str = "0" + monthInput.getText().toString();
            monthInput.setText(str);
        }
    }

    @Override
    public void setValue(Object val) {

    }

    @Override
    public String getValue() {
        return dayInput.getText().toString() +
                monthInput.getText().toString() +
                yearInput.getText().toString();
    }

    //change the editText focus
    //position 0:day   1:month   2:year
    protected void inputLayoutRequestFocus(int position) {
        switch (position) {
            case DAY_INPUT:
                dayInput.setFocusable(true);
                dayInput.setFocusableInTouchMode(true);
                dayInput.requestFocus();
                break;
            case MONTH_INPUT:
                monthInput.setFocusable(true);
                monthInput.setFocusableInTouchMode(true);
                monthInput.requestFocus();
                break;
            case YEAR_INPUT:
                yearInput.setFocusable(true);
                yearInput.setFocusableInTouchMode(true);
                yearInput.requestFocus();
                break;
        }
    }
}
