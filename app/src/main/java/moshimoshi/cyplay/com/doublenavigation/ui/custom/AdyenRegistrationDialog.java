package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.adyen.adyenpos.generic.Preferences;
import com.adyen.library.AdyenLibraryInterface;
import com.adyen.library.AppInfo;
import com.adyen.library.RegistrationCompleteListener;
import com.adyen.library.ServerMode;
import com.adyen.library.exceptions.AlreadyRegisteringAppException;
import com.adyen.library.exceptions.AppAlreadyRegisteredException;
import com.adyen.library.exceptions.AppNotRegisteredException;
import com.adyen.library.exceptions.InvalidRequestException;

import org.greenrobot.eventbus.EventBus;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.events.AdyenMerchantUpdatedEvent;


/**
 * Created by romainlebouc on 24/11/2016.
 */

public class AdyenRegistrationDialog extends AlertDialog {

    private static final String BETA_MODE_FLAG = ":0xdeadbeef";
    private static final String DEV_MODE_FLAG = ":0xcafebabe";
    private static final String TEST_MODE_FLAG = ":0xfacefeed";

    private final AdyenRegistrationForm adyenRegistrationForm;
    private final AdyenLibraryInterface adyenLibraryInterface;
    private final EventBus bus;

    Preferences preferences;
    private String oldUrlUsername;
    private String oldMerchant;

    private final OnClickListener EMPTY_ON_CLICK_LISTENER = new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    };

    private final View.OnClickListener VALIDATE_CREDENTIALS_LISTENER = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            try {
                adyenRegistrationForm.resetErrorMessages();
                String merchantCode = adyenRegistrationForm.getMerchant();
                String userName = adyenRegistrationForm.getName();
                String password = adyenRegistrationForm.getPassword();

                adyenLibraryInterface.setServerMode(ServerMode.LIVE);
                if (userName.endsWith(TEST_MODE_FLAG)) {
                    userName = userName.replace(TEST_MODE_FLAG, "");
                    adyenLibraryInterface.setServerMode(ServerMode.TEST);
                } else if (userName.endsWith(BETA_MODE_FLAG)) {
                    userName = userName.replace(BETA_MODE_FLAG, "");
                    adyenLibraryInterface.setServerMode(ServerMode.BETA);
                } else if (userName.endsWith(DEV_MODE_FLAG)) {
                    userName = userName.replace(DEV_MODE_FLAG, "");
                    adyenLibraryInterface.setServerMode(ServerMode.DEV);
                }

                try {
                    adyenLibraryInterface.unRegisterApp();
                } catch (AppNotRegisteredException e) {
                    Log.i(AdyenRegistrationDialog.class.getName(), e.getMessage());
                }
                adyenLibraryInterface.registerApp(merchantCode,
                        userName,
                        password,
                        new RegistrationCompleteListener() {

                            @Override
                            public void onRegistrationComplete(final CompletedStatus completedStatus, AppInfo appInfo, final String message) {

                                switch (completedStatus) {
                                    case ERROR:
                                        adyenRegistrationForm.setVisibility(View.VISIBLE);
                                        adyenRegistrationForm.setStatus(R.string.login_failed_title, R.string.login_failed_message);

                                        //for this error, the merchant and the urlUsername are already changed in the preferences
                                        //so, we need to reset the Preferences to old
                                        resetOldPreferences();
                                        break;
                                    case ERROR_BADCREDENTIALS:
                                        adyenRegistrationForm.setVisibility(View.VISIBLE);
                                        adyenRegistrationForm.setStatus(R.string.login_failed_title, R.string.login_error_badcredentials);

                                        //for this error, the merchant and the urlUsername are already changed in the preferences
                                        //so, we need to reset the Preferences to old
                                        resetOldPreferences();
                                        break;
                                    case ERROR_NONETWORK:
                                        adyenRegistrationForm.setVisibility(View.VISIBLE);
                                        adyenRegistrationForm.setStatus(R.string.login_failed_title, R.string.login_error_nonetwork);
                                        break;
                                    case ERROR_NOROUTE:
                                        adyenRegistrationForm.setVisibility(View.VISIBLE);
                                        adyenRegistrationForm.setStatus(R.string.login_failed_title, R.string.login_error_noroute);
                                        break;
                                    case OK:
                                        dismiss();
                                        bus.post(new AdyenMerchantUpdatedEvent());
                                        adyenRegistrationForm.setVisibility(View.INVISIBLE);
                                        break;
                                    default:
                                        adyenRegistrationForm.setVisibility(View.VISIBLE);
                                        adyenRegistrationForm.setStatus(R.string.login_failed_title, message);
                                        break;
                                }
                            }
                        });
            } catch (AppAlreadyRegisteredException e) {
                adyenRegistrationForm.setVisibility(View.VISIBLE);
                adyenRegistrationForm.setStatus(R.string.login_failed_title, e.getMessage());
            } catch (AlreadyRegisteringAppException e) {
                adyenRegistrationForm.setVisibility(View.VISIBLE);
                adyenRegistrationForm.setStatus(R.string.login_failed_title, e.getMessage());
            } catch (InvalidRequestException e) {
                adyenRegistrationForm.setVisibility(View.VISIBLE);
                adyenRegistrationForm.setStatus(R.string.login_failed_title, e.getMessage());
            }
        }
    };

    public AdyenRegistrationDialog(Context context, AdyenLibraryInterface adyenLibraryInterface, EventBus bus) {
        super(context, R.style.AppCompatAlertDialogStyle);
        adyenRegistrationForm = new AdyenRegistrationForm(this.getContext());
        this.adyenLibraryInterface = adyenLibraryInterface;
        this.bus = bus;
        this.setTitle(R.string.marchant_configuration_title);
        this.setView(adyenRegistrationForm);
        this.preferences = new Preferences(context);

        oldUrlUsername = preferences.getUrlUsername();
        oldMerchant = preferences.getMerchantAccount();

        init();
    }

    private void resetOldPreferences() {
        preferences.setMerchantAccount(oldMerchant);
        preferences.setUrlUsername(oldUrlUsername);
    }

    private void init() {

        this.setButton(AlertDialog.BUTTON_POSITIVE, this.getContext().getString(R.string.valid), EMPTY_ON_CLICK_LISTENER);
        this.setButton(AlertDialog.BUTTON_NEGATIVE, this.getContext().getString(R.string.cancel), EMPTY_ON_CLICK_LISTENER);
        this.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button valid = AdyenRegistrationDialog.this.getButton(AlertDialog.BUTTON_POSITIVE);
                valid.setOnClickListener(VALIDATE_CREDENTIALS_LISTENER);
                Button cancel = AdyenRegistrationDialog.this.getButton(AlertDialog.BUTTON_NEGATIVE);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AdyenRegistrationDialog.this.dismiss();
                    }
                });
            }
        });

    }

    public void setMerchant(String merchant) {
        adyenRegistrationForm.setMerchant(merchant);
    }

}

