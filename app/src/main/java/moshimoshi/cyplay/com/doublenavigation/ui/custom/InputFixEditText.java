package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by wentongwang on 09/03/2017.
 */

public class InputFixEditText extends EditText implements TextWatcher, View.OnKeyListener {

    private static final int DEFAULT_MAX_INPUT = 10;

    private int maxInput = DEFAULT_MAX_INPUT;
    private boolean onlyInteger = false;

    private Context context;

    public InputFixEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initAttr(attrs);

        setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxInput)});

        if (onlyInteger)
            setKeyListener(new DigitsKeyListener(false, false));

        addTextChangedListener(this);
        setOnKeyListener(this);
    }


    private void initAttr(AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.InputFixEditText);

        maxInput = ta.getInteger(R.styleable.InputFixEditText_input_number, DEFAULT_MAX_INPUT);
        onlyInteger = ta.getBoolean(R.styleable.InputFixEditText_only_integer, false);

        ta.recycle();
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if (focused)
            setSelection(getText().toString().length());
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    //==============TextWatcher================
    private boolean onDelete = false;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int after) {
        onDelete = before > after;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        int currentLength = editable.toString().length();
        if (onDelete) {
            //on Delete Mode
            if (currentLength == 0) {
                if (listener != null) {
                    listener.deleteFinished();
                }
            }
        } else {
            //on input mode
            if (currentLength == maxInput) {
                if (listener != null) {
                    listener.inputFinished();
                }
            }
        }
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        int currentLength = this.getText().toString().length();
        if(view.equals(this) &&
                keyCode == KeyEvent.KEYCODE_DEL &&
                keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                currentLength == 0) {
            if (listener != null) {
                listener.deleteFinished();
            }
        }
        return false;
    }

    private OnInputListener listener;

    public interface OnInputListener {
        void deleteFinished();

        void inputFinished();
    }

    public void setOnInputListener(OnInputListener listener) {
        this.listener = listener;
    }
}
