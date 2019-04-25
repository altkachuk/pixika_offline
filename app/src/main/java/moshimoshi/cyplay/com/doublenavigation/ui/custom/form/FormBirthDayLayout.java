package moshimoshi.cyplay.com.doublenavigation.ui.custom.form;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import java.text.ParseException;
import java.util.Date;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.utils.FormHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.StringUtils;
import ninja.cyplay.com.apilibrary.utils.ReflectionUtils;

/**
 * Created by wentongwang on 31/05/2017.
 */

public class FormBirthDayLayout extends FormDateAutoFillLayout {

    public FormBirthDayLayout(Context context) {
        this(context, null);
    }

    public FormBirthDayLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FormBirthDayLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getLayoutId() {
        return R.layout.form_date_auto_fill_layout;
    }

    @Override
    public View runValidation() {

        if (row == null || descriptor == null)
            return null;

        Date date = null;

        autoFill();

        if (FormHelper.isRequired(descriptor)) {
            //check if is empty
            if (FormHelper.isEditTextEmpty(dayInput)) {
                inputLayoutRequestFocus(DAY_INPUT);
                dayInput.setError(getContext().getString(R.string.form_required_error));
                return dayInput;
            }

            if (FormHelper.isEditTextEmpty(monthInput)) {
                inputLayoutRequestFocus(MONTH_INPUT);
                monthInput.setError(getContext().getString(R.string.form_required_error));
                return monthInput;
            }

            if (FormHelper.isEditTextEmpty(yearInput)) {
                inputLayoutRequestFocus(YEAR_INPUT);
                yearInput.setError(getContext().getString(R.string.form_required_error));
                return yearInput;
            }

        }

        //if day number is numeric then check day number <= 31
        if (StringUtils.isNumeric(dayInput.getText().toString()) &&
                (Integer.valueOf(dayInput.getText().toString()) > 31 || Integer.valueOf(dayInput.getText().toString()) <= 0)) {
            inputLayoutRequestFocus(DAY_INPUT);
            dayInput.setError(getContext().getString(R.string.form_regex_error));
            return dayInput;
        }
        //if month number is numeric then check month number <=12
        if (StringUtils.isNumeric(monthInput.getText().toString()) &&
                (Integer.valueOf(monthInput.getText().toString()) > 12 || Integer.valueOf(monthInput.getText().toString()) <= 0)) {
            inputLayoutRequestFocus(MONTH_INPUT);
            monthInput.setError(getContext().getString(R.string.form_regex_error));
            return monthInput;
        }

        //if year number is numeric then check year number > 1900
        if (StringUtils.isNumeric(yearInput.getText().toString()) &&
                (Integer.valueOf(yearInput.getText().toString()) < 1900)) {
            inputLayoutRequestFocus(YEAR_INPUT);
            yearInput.setError(getContext().getString(R.string.form_regex_error));
            return yearInput;
        }

        if (FormHelper.isRequired(descriptor) ||
                !dayInput.getText().toString().isEmpty() ||
                !monthInput.getText().toString().isEmpty()||
                !yearInput.getText().toString().isEmpty()) {
            //if it is required or user just input one cell, should check date format
            try {
                date = getSimpleDateFormat().parse(getDate());
            } catch (ParseException e) {
                if (!StringUtils.isNumeric(dayInput.getText().toString())) {
                    inputLayoutRequestFocus(DAY_INPUT);
                    dayInput.setError(getContext().getString(R.string.form_regex_error));
                    return dayInput;
                } else if (!StringUtils.isNumeric(monthInput.getText().toString())) {
                    inputLayoutRequestFocus(MONTH_INPUT);
                    monthInput.setError(getContext().getString(R.string.form_regex_error));
                    return monthInput;
                }else{
                    inputLayoutRequestFocus(YEAR_INPUT);
                    yearInput.setError(getContext().getString(R.string.form_regex_error));
                    return yearInput;
                }
            }

        }


        if (customer != null && row != null) {
            ReflectionUtils.set(customer, row.getTag(), date);
            dayInput.setError(null);
            monthInput.setError(null);
            yearInput.setError(null);
        }
        return null;
    }
}
