package moshimoshi.cyplay.com.doublenavigation.presenter;

import com.adyen.library.AdyenLibraryInterface;
import com.adyen.library.DeviceInfo;
import com.adyen.library.exceptions.NotYetRegisteredException;

import moshimoshi.cyplay.com.doublenavigation.model.business.Payment;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerPaymentTransaction;
import moshimoshi.cyplay.com.doublenavigation.view.AdyenPosView;


/**
 * Created by romainlebouc on 09/03/2017.
 */

public interface AdyenPosPresenter extends Presenter<AdyenPosView> {

    void init() throws NotYetRegisteredException;

    DeviceInfo getDefaultPOS();

    AdyenLibraryInterface getAdyenLibraryInterface ();

    void onResume();

    void onPause();

    void choosePOS();

    void pay(Payment payment, CustomerPaymentTransaction customerPaymentTransaction);

    void startPaiementProcess();

    void initTransaction() ;

    void onTransactionCancelled();
}
