package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adyen.library.AddDeviceListener;
import com.adyen.library.AdyenLibraryInterface;
import com.adyen.library.DeviceData;
import com.adyen.library.DeviceInfo;
import com.adyen.library.exceptions.UnknownDeviceIdException;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.events.AdyenPOSChosenEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.AdyenPOSRemoved;

/**
 * Created by romainlebouc on 24/11/2016.
 */


//TODO : replace Object by POS
public class POSViewHolder extends ItemViewHolder<DeviceInfo> {

    @BindView(R.id.cell_container)
    LinearLayout cellContainer;

    @BindView(R.id.terminal_state_information)
    TextView tvTerminalState;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.hostname)
    TextView hostName;

    @BindView(R.id.disconnect)
    TextView disconnect;

    @BindView(R.id.connect)
    TextView connect;

    @BindView(R.id.remove_device)
    TextView removeDevice;

    @BindView(R.id.choose_device)
    TextView chooseDevice;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    private final AdyenLibraryInterface adyenLibraryInterface;
    private final EventBus bus;

    // In this mode, we can only select a device (no administration : remove, disconnect...)
    private final boolean choosingMode;

    private DeviceInfo deviceInfo;

    public POSViewHolder(View itemView, AdyenLibraryInterface adyenLibraryInterface, EventBus bus, boolean chooserMode) {
        super(itemView);
        this.adyenLibraryInterface = adyenLibraryInterface;
        this.bus = bus;
        this.choosingMode = chooserMode;
        ButterKnife.bind(this, itemView);

        chooseDevice.setVisibility(chooserMode ? View.VISIBLE : View.GONE);
        removeDevice.setVisibility(chooserMode ? View.GONE : View.VISIBLE);
    }


    @Override
    public void setItem(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;


        disconnect.setVisibility(View.GONE);
        connect.setVisibility(View.GONE);

        name.setText(deviceInfo.getFriendlyName());
        hostName.setText(deviceInfo.getDeviceId());

        if (deviceInfo.isMisconfigured()) {
            tvTerminalState.setBackgroundColor(ContextCompat.getColor(this.context, R.color.error_red));
            tvTerminalState.setText(this.context.getString(R.string.terminal_problem, deviceInfo.getFriendlyName()));
            tvTerminalState.setTextColor(ContextCompat.getColor(this.context, R.color.text_white));

            Drawable img = ContextCompat.getDrawable(this.context, R.drawable.red_circle);
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            name.setCompoundDrawables(img, null, null, null);
        } else if (deviceInfo.isConnected()) {
            disconnect.setVisibility(choosingMode ? View.GONE : View.VISIBLE);
            tvTerminalState.setBackgroundColor(ContextCompat.getColor(this.context, R.color.success_green));
            tvTerminalState.setText(this.context.getString(R.string.terminal_connected, deviceInfo.getFriendlyName()));
            tvTerminalState.setTextColor(ContextCompat.getColor(this.context, R.color.text_white));

            Drawable img = ContextCompat.getDrawable(this.context, R.drawable.green_circle);
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            name.setCompoundDrawables(img, null, null, null);
        } else if (deviceInfo.isRegistered()) {
            connect.setVisibility(choosingMode ? View.GONE : View.VISIBLE);
            tvTerminalState.setBackgroundColor(ContextCompat.getColor(this.context, R.color.warning_yellow));
            tvTerminalState.setText(this.context.getString(R.string.terminal_saved, deviceInfo.getFriendlyName()));
            tvTerminalState.setTextColor(ContextCompat.getColor(this.context, R.color.text_black));

            Drawable img = ContextCompat.getDrawable(this.context, R.drawable.orange_circle);
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            name.setCompoundDrawables(img, null, null, null);
        } else if (deviceInfo.isPaired()) {
            tvTerminalState.setBackgroundColor(ContextCompat.getColor(this.context, R.color.GreyishBrown));
            tvTerminalState.setText(this.context.getString(R.string.terminal_delete, deviceInfo.getFriendlyName()));
            tvTerminalState.setTextColor(ContextCompat.getColor(this.context, R.color.text_white));

            Drawable img = ContextCompat.getDrawable(this.context, R.drawable.grey_circle);
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            name.setCompoundDrawables(img, null, null, null);
        }


    }

    @OnClick({R.id.connect, R.id.choose_device})
    public void onConnectDevice() {
        connectToDevice();
    }

    @OnClick(R.id.disconnect)
    public void onDisconnectDevice() {
        adyenLibraryInterface.disconnectDevice();
    }

    @OnClick(R.id.remove_device)
    public void onRemoveDevice() {
        //set paired false forces removing device bound
        deviceInfo.setPaired(false);
        try {
            adyenLibraryInterface.removeDevice(deviceInfo);
            bus.post(new AdyenPOSRemoved(deviceInfo));
        } catch (UnknownDeviceIdException e) {
            Log.e(POSViewHolder.class.getName(), e.getMessage(), e);
        }
    }

    private void connectToDevice() {
        progressBar.setVisibility(View.VISIBLE);

        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle(this.context.getString(R.string.terminal_connection_status_connecting, deviceInfo != null ? deviceInfo.getFriendlyName() : ""));
        builder.setCancelable(false);
        builder.setMessage("");

        final AlertDialog alertDialog = builder.create();
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, POSViewHolder.this.context.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (choosingMode) {
                    POSViewHolder.this.bus.post(new AdyenPOSChosenEvent(deviceInfo));
                }
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, POSViewHolder.this.context.getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setVisibility(View.GONE);
            }
        });
        alertDialog.show();


        adyenLibraryInterface.getTerminalIdentity(new AddDeviceListener() {
            @Override
            public void onAddDeviceComplete(final AddDeviceListener.CompletedStatus completedStatus, final String message, DeviceData deviceData) {
                Log.e(POSViewHolder.class.getName(), "onAddDeviceComplete" + completedStatus.name());
                progressBar.setVisibility(View.GONE);

                switch (completedStatus) {
                    case OK:
                        alertDialog.setMessage(message);
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
                        alertDialog.setMessage(message);
                        break;
                    default:
                        alertDialog.setMessage(message);
                }

                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setVisibility(View.VISIBLE);
            }

            @Override
            public void onProgress(final AddDeviceListener.ProcessStatus processStatus, final String message) {
                Log.e(POSViewHolder.class.getName(), "onProgress" + processStatus.name());
                alertDialog.setMessage(message);
            }
        }, deviceInfo, false, false);
    }


}
