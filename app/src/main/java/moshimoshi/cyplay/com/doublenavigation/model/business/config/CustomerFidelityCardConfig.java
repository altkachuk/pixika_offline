package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

/**
 * Created by romainlebouc on 12/09/16.
 */
@Parcel
public class CustomerFidelityCardConfig {

    String reg_exp_card_number;

    public String getReg_exp_card_number() {
        return reg_exp_card_number;
    }

    public void setReg_exp_card_number(String reg_exp_card_number) {
        this.reg_exp_card_number = reg_exp_card_number;
    }
}
