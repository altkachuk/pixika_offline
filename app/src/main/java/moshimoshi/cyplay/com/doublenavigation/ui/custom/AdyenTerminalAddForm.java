package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by romainlebouc on 24/11/2016.
 */

public class AdyenTerminalAddForm extends LinearLayout {


    @BindView(R.id.terminal_id)
    EditText terminalId;

    @BindView(R.id.status_title)
    TextView statusMessage;

    @BindView(R.id.in_progress)
    View inProgress;

    public AdyenTerminalAddForm(Context context) {
        this(context, null);
    }

    public AdyenTerminalAddForm(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdyenTerminalAddForm(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_adyen_terminal_add_form, this, true);
        // Bind xml
        ButterKnife.bind(this);
    }

    public String getTerminalId() {
        return terminalId.getText().toString();
    }

    public void setStatus(TerminalAddStatus status, String statusMessage) {
        this.statusMessage.setText(statusMessage);
        this.inProgress.setVisibility(status.visibiltiy);
    }

    public enum TerminalAddStatus {
        INIT(GONE), IN_PROGRESS(VISIBLE), DONE(GONE), ERROR(GONE);

        private final int visibiltiy;

        TerminalAddStatus(int visibiltiy) {
            this.visibiltiy = visibiltiy;
        }

    }

}
