package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by romainlebouc on 01/09/16.
 */
public class ActiveFilterValue extends LinearLayout{

    @BindView(R.id.filter_value_label)
    TextView filterValueLabel;

    public ActiveFilterValue(Context context) {
        super(context);
        init();
    }

    public ActiveFilterValue(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ActiveFilterValue(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ActiveFilterValue(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.custom_active_filter_value, this);
        ButterKnife.bind(this);
    }

    public void setLabel(String label){
        filterValueLabel.setText(label);
    }
}
