package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

/**
 * Created by romainlebouc on 28/02/2017.
 */
@Parcel
public class MeansOfPayment {

    BankAccount bank_account;

    public MeansOfPayment(){
        this.bank_account = new BankAccount();
    }

    public BankAccount getBank_account() {
        return bank_account;
    }
}
