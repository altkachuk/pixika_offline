package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Address;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_Form;
import moshimoshi.cyplay.com.doublenavigation.model.events.BasketAddressEvent;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.validator.FormValidation;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.validator.FormValidator;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.FormHelper;
import ninja.cyplay.com.apilibrary.utils.Constants;

/**
 * Created by wentongwang on 22/03/2017.
 */

public class DeliveryAddressItemViewHolder extends ItemViewHolder<Address> {

    @Inject
    EventBus bus;

    @Inject
    ConfigHelper configHelper;

    @BindView(R.id.check_button)
    RadioButton checkBtn;

    @BindView(R.id.delivery_address_customer_name)
    TextView addressCustomerName;

    @BindView(R.id.delivery_address_preview)
    TextView addressPreview;

    @BindView(R.id.delivery_address_warring)
    TextView addressWarring;

    @BindView(R.id.arrow_more)
    View arrowMore;

    @BindView(R.id.delivery_address_content)
    View deliveryContent;

    private Address address;

    public DeliveryAddressItemViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setItem(Address address) {
        this.address = address;
        addressCustomerName.setText(address.getFormatName());
        addressPreview.setText(address.getMail().getOneLineAddress());

        PR_Form form = FormHelper.getFormFromKey(context, Constants.FORM_SHIPPING_ADDRESS_KEY, configHelper);
        FormValidator formValidator = new FormValidator(context, configHelper);
        List<FormValidation> invalidFormValidations = formValidator.validateEntity(form, address);
        if (invalidFormValidations.isEmpty()) {
            checkBtn.setEnabled(true);
            deliveryContent.setEnabled(true);
            addressWarring.setVisibility(View.GONE);
        } else {
            checkBtn.setEnabled(false);
            deliveryContent.setEnabled(false);
            addressWarring.setVisibility(View.VISIBLE);
        }

    }

    public void setOnCheckedListener(View.OnClickListener listener) {
        checkBtn.setOnClickListener(listener);
        deliveryContent.setOnClickListener(listener);
    }

    @OnClick(R.id.arrow_more)
    public void updateAddress() {
        BasketAddressEvent basketAddressEvent = new BasketAddressEvent(BasketAddressEvent.UPDATE_ADDRESS, address.getAddressType());
        basketAddressEvent.setAddress(address);
        bus.post(basketAddressEvent);
    }

    public void setCheckBtn(boolean checked) {
        checkBtn.setChecked(checked);
    }

    public boolean isCheckBtnEnable() {
        return checkBtn.isEnabled();
    }
}
