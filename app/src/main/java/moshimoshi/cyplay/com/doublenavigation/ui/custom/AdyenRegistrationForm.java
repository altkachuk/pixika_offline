package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adyen.adyenpos.generic.Preferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by romainlebouc on 23/11/16.
 */
public class AdyenRegistrationForm extends LinearLayout {

    @BindView(R.id.merchant)
    EditText merchant;

    @BindView(R.id.name)
    EditText name;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.status_title)
    TextView statusTitle;

    @BindView(R.id.status_message)
    TextView statusMessage;

    @BindView(R.id.password_visibility)
    CheckBox passwordVisibility;

    Preferences preferences;

    public AdyenRegistrationForm(Context context) {
        this(context, null);
    }

    public AdyenRegistrationForm(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdyenRegistrationForm(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_adyen_registration_app_form, this, true);
        // Bind xml
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        passwordVisibility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                password.setInputType(InputType.TYPE_CLASS_TEXT |
                        (isChecked ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_TEXT_VARIATION_PASSWORD));
                password.setSelection(password.getText().length());
            }
        });
        preferences = new Preferences(this.getContext());
        this.setMerchant(preferences.getMerchantAccount());

    }

    public void setMerchant(String merchant) {
        this.merchant.setText(merchant);
    }

    public void setName(EditText name) {
        this.name = name;
    }

    public String getMerchant() {
        return merchant.getText().toString().trim();
    }

    public String getName() {
        return name.getText().toString().trim();
    }

    public String getPassword() {
        return password.getText().toString().trim();
    }

    public void setStatus(int errorTitle, String errorMessage) {
        this.statusTitle.setText(errorTitle);
        this.statusMessage.setText(errorMessage);
    }

    public void setStatus(int errorTitle, int errorMessage) {
        this.statusTitle.setText(errorTitle);
        this.statusMessage.setText(errorMessage);
    }

    public void resetErrorMessages(){
        this.statusTitle.setText("");
        this.statusMessage.setText("");
    }

}
