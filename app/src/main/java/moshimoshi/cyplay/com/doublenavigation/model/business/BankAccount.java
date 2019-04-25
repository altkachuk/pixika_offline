package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

/**
 * Created by romainlebouc on 28/02/2017.
 */
@Parcel
public class BankAccount {

    String iban;
    String bic;

    public String getIban() {
        return iban;
    }

    public String getBic() {
        return bic;
    }
}
