package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by romainlebouc on 28/06/2017.
 */

public class DialogPosTransaction extends RelativeLayout {

    @BindView(R.id.pos_status)
    TextView status;

    public DialogPosTransaction(Context context) {
        this(context, null);
    }

    public DialogPosTransaction(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialogPosTransaction(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.dialog_pos_transaction, this, true);
        ButterKnife.bind(this);
    }

    public void setStatusMessage(String statusMessage) {
        this.status.setText(statusMessage);
    }

}
