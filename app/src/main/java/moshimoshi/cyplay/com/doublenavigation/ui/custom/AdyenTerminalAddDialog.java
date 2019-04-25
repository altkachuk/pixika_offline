package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.adyen.library.AddDeviceListener;
import com.adyen.library.AdyenLibraryInterface;
import com.adyen.library.DeviceData;
import com.adyen.library.DeviceInfo;
import com.adyen.library.exceptions.AlreadyAddingDeviceException;
import com.adyen.library.exceptions.AppNotRegisteredException;
import com.adyen.library.exceptions.DeviceAlreadyAddedException;

import org.greenrobot.eventbus.EventBus;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.events.AdyenPOSAdded;


/**
 * Created by romainlebouc on 24/11/2016.
 */

public class AdyenTerminalAddDialog extends AlertDialog {

    private static final String BETA_MODE_FLAG = ":0xdeadbeef";
    private static final String DEV_MODE_FLAG = ":0xcafebabe";
    private static final String TEST_MODE_FLAG = ":0xfacefeed";

    final AdyenTerminalAddForm adyenTerminalAddForm;
    final AdyenLibraryInterface adyenLibraryInterface;

    final Activity parentActivity;
    final EventBus bus;

    private final OnClickListener EMPTY_ON_CLICK_LISTENER = new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    };

    private void displayButtonForFinishedState() {
        AdyenTerminalAddDialog.this.getButton(BUTTON_NEGATIVE).setVisibility(View.GONE);
        AdyenTerminalAddDialog.this.getButton(BUTTON_POSITIVE).setVisibility(View.VISIBLE);
        AdyenTerminalAddDialog.this.getButton(BUTTON_POSITIVE).setText(getContext().getString(android.R.string.ok));
        AdyenTerminalAddDialog.this.getButton(BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdyenTerminalAddDialog.this.dismiss();
            }
        });
    }

    private final View.OnClickListener VALIDATE_CREDENTIALS_LISTENER = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            AdyenTerminalAddDialog.this.getButton(BUTTON_POSITIVE).setVisibility(View.GONE);
            final DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.setConnectionType(DeviceInfo.TYPE_WIFI);
            String deviceId = adyenTerminalAddForm.getTerminalId();
            deviceInfo.setDeviceId(deviceId);
            deviceInfo.setFriendlyName(deviceId);

            try {
                adyenLibraryInterface.addDevice(new AddDeviceListener() {
                    @Override
                    public void onAddDeviceComplete(final CompletedStatus completedStatus, final String message, DeviceData deviceData) {
                        Log.e(AdyenTerminalAddDialog.class.getName(), completedStatus.name());
                        switch (completedStatus) {
                            case OK:
                                if (parentActivity != null) {
                                    parentActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            displayButtonForFinishedState();
                                            adyenTerminalAddForm.setStatus(AdyenTerminalAddForm.TerminalAddStatus.DONE, message);
                                            bus.post(new AdyenPOSAdded(deviceInfo));
                                        }
                                    });
                                }

                                break;
                            case ERROR_IDENTIFY:
                            case ERROR:
                            case ERROR_REGISTER:
                            case ERROR_SYNCACTION:
                            case ERROR_SYNCDEVICE:
                            case ERROR_VERIFY:
                            case ERROR_NONETWORK:
                            case ERROR_NOROUTE:
                            case TIMEOUT:
                                if (parentActivity != null) {
                                    parentActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            displayButtonForFinishedState();
                                            adyenTerminalAddForm.setStatus(AdyenTerminalAddForm.TerminalAddStatus.ERROR, message);
                                        }
                                    });
                                }
                                break;
                            default:
                                if (parentActivity != null) {
                                    parentActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            AdyenTerminalAddDialog.this.setButton(AlertDialog.BUTTON_POSITIVE, getContext().getString(R.string.retry), EMPTY_ON_CLICK_LISTENER);
                                            Button b = AdyenTerminalAddDialog.this.getButton(AlertDialog.BUTTON_POSITIVE);
                                            b.setOnClickListener(VALIDATE_CREDENTIALS_LISTENER);
                                        }
                                    });
                                }

                                break;
                        }


                    }

                    @Override
                    public void onProgress(final ProcessStatus processStatus, final String message) {
                        adyenTerminalAddForm.setStatus(AdyenTerminalAddForm.TerminalAddStatus.IN_PROGRESS, message);
                    }
                }, deviceInfo);
            } catch (DeviceAlreadyAddedException e) {
                e.printStackTrace();
            } catch (AlreadyAddingDeviceException e) {
                e.printStackTrace();
            } catch (AppNotRegisteredException e) {
                e.printStackTrace();
            }
        }
    };

    public AdyenTerminalAddDialog(Activity parentActivity, AdyenLibraryInterface adyenLibraryInterface, EventBus bus) {
        super(parentActivity, R.style.AppCompatAlertDialogStyle);
        adyenTerminalAddForm = new AdyenTerminalAddForm(this.getContext());
        this.adyenLibraryInterface = adyenLibraryInterface;
        this.parentActivity = parentActivity;
        this.bus = bus;
        this.setTitle(R.string.terminal_configuration_title);

        init();
    }

    private void init() {
        this.setView(adyenTerminalAddForm);
        this.setButton(AlertDialog.BUTTON_POSITIVE, this.getContext().getString(R.string.add), EMPTY_ON_CLICK_LISTENER);
        this.setButton(AlertDialog.BUTTON_NEGATIVE, this.getContext().getString(R.string.cancel), EMPTY_ON_CLICK_LISTENER);
        this.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = AdyenTerminalAddDialog.this.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(VALIDATE_CREDENTIALS_LISTENER);
            }
        });

    }

}

