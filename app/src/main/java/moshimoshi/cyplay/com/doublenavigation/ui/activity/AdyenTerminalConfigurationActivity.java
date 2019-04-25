package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.adyen.adyenpos.generic.Preferences;
import com.adyen.adyenpos.generic.TerminalConnectionStatus;
import com.adyen.library.AdyenLibraryInterface;
import com.adyen.library.DeviceInfo;
import com.adyen.library.callbacks.TerminalConnectionListener;
import com.adyen.library.exceptions.NoDefaultDeviceException;
import com.adyen.library.exceptions.NotYetRegisteredException;
import com.adyen.library.real.LibraryReal;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.events.AdyenMerchantUpdatedEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.AdyenPOSAdded;
import moshimoshi.cyplay.com.doublenavigation.model.events.AdyenPOSChosenEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.AdyenPOSRemoved;
import moshimoshi.cyplay.com.doublenavigation.model.events.AdyenPOSUpdated;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BasketBasedActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.POSAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.AdyenRegistrationDialog;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.AdyenTerminalAddDialog;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.DividerItemDecoration;
import ninja.cyplay.com.apilibrary.utils.LogUtils;

import static moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants.EXTRA_CHOOSE_MODE;

/**
 * Created by romainlebouc on 23/11/16.
 */
public class AdyenTerminalConfigurationActivity extends BasketBasedActivity {

    @BindView(R.id.adyen_pos_recyclerview)
    RecyclerView posRecyclerView;

    @BindView(R.id.adyen_merchant)
    TextView merchant;

    @BindView(R.id.edit_adyen_merchant)
    TextView merchantEdit;

    @BindView(R.id.merchant_container)
    View merchantContainer;

    AdyenLibraryInterface lib = null;
    POSAdapter posAdapter;

    Preferences preferences;

    private MenuItem registerMerchantMenuItem;
    private MenuItem addTerminalMenuItem;


    private boolean chooserMode = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chooserMode = this.getIntent().getBooleanExtra(EXTRA_CHOOSE_MODE, false);
        setContentView(R.layout.activity_adyen_pos);

        // Show the Up button in the action bar
        if (chooserMode && getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        }

        preferences = new Preferences(this);
        try {
            lib = LibraryReal.getLib();
        } catch (NotYetRegisteredException e) {
            Log.e(LogUtils.generateTag(this), "", e);
        }
        posAdapter = new POSAdapter(this, lib, bus, chooserMode);
        setupRecyclerView();
    }

    protected boolean isLeftCrossClosable() {
        return chooserMode;
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // optimization
        posRecyclerView.setHasFixedSize(false);
        // set layout
        posRecyclerView.setLayoutManager(layoutManager);
        posRecyclerView.setAdapter(posAdapter);

        // Add Decorator
        posRecyclerView.addItemDecoration(new DividerItemDecoration(this));
        // OnClick action
        //posRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new CatalogCategoriesFragment.CategoryClick()));
    }

    @Override
    protected void onStart() {
        super.onStart();

        // If app is registred ==> We display the infos
        if (lib.isAppRegistered()) {
            loadMerchantInfos();
            loadDeviceInfos();
            // Else we display a popup to add a Merchand
        } else {
            AdyenRegistrationDialog adyenRegistrationDialog = new AdyenRegistrationDialog(this, lib, bus);
            adyenRegistrationDialog.show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        lib.enableEagerConnection();
        lib.registerTerminalConnectionListener(new TerminalConnectionListener() {
            @Override
            public void onStatusChanged(TerminalConnectionStatus terminalConnectionStatus, DeviceInfo deviceInfo, String s) {
                AdyenTerminalConfigurationActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadDeviceInfos();
                    }
                });

            }

            @Override
            public void onConnecting(DeviceInfo deviceInfo, int i) {

            }

            @Override
            public void onConnectingFailed(DeviceInfo deviceInfo, String s) {

            }

            @Override
            public void onConnected(DeviceInfo deviceInfo) {

            }

            @Override
            public void onDisconnected(DeviceInfo deviceInfo) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        lib.unregisterTerminalConnectionListener();
        lib.disableEagerConnection();
    }


    @OnClick(R.id.adyen_merchant)
    public void onTextClick() {
        if (lib != null) {
            if (!lib.isAppRegistered()) {
                AdyenRegistrationDialog adyenRegistrationDialog = new AdyenRegistrationDialog(this, lib, bus);
                adyenRegistrationDialog.show();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.adyen_pos_activity, menu);
        registerMerchantMenuItem = menu.findItem(R.id.action_register_merchant);
        addTerminalMenuItem = menu.findItem(R.id.action_add_terminal);
        updateOptionsMenuVisibility();
        return true;
    }

    private void updateOptionsMenuVisibility() {
        if (registerMerchantMenuItem != null) {
            registerMerchantMenuItem.setVisible(!lib.isAppRegistered());
        }

        addTerminalMenuItem.setVisible(lib.isAppRegistered());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_terminal:
                AdyenTerminalAddDialog adyenTerminalAddDialog = new AdyenTerminalAddDialog(this, lib, bus);
                adyenTerminalAddDialog.show();
                break;
            case R.id.action_register_merchant:
                AdyenRegistrationDialog adyenRegistrationDialog = new AdyenRegistrationDialog(this, lib, bus);
                adyenRegistrationDialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onAdyenPOSAdded(AdyenPOSAdded adyenPOSAdded) {
        loadDeviceInfos();
    }

    @Subscribe
    public void onAdyenPOSRemoved(AdyenPOSRemoved adyenPOSRemoved) {
        loadDeviceInfos();
    }

    @Subscribe
    public void onAdyenPOSUpdated(AdyenPOSUpdated adyenPOSUpdated) {
        loadDeviceInfos();
    }

    @Subscribe
    public void onAdyenPOSUpdated(AdyenPOSChosenEvent adyenPOSChosenEvent) {
        this.finishConfiguration(true);
    }

    @Subscribe
    public void onAdyenMerchantUpdatedEvent(AdyenMerchantUpdatedEvent adyenMerchantUpdatedEvent) {
        loadMerchantInfos();
        updateOptionsMenuVisibility();
    }

    private void loadDeviceInfos() {
        List<DeviceInfo> deviceInfos = lib.getDeviceList();

        DeviceInfo defaultDeviceInfo = null;

        try {
            defaultDeviceInfo = lib.getDefaultDeviceInfo();
        } catch (final NoDefaultDeviceException e) {
            Log.i(AdyenTerminalConfigurationActivity.class.getName(), "No Default Device Found");
        }


        for (DeviceInfo deviceInfo : deviceInfos) {
            if (defaultDeviceInfo != null && defaultDeviceInfo.getDeviceId().equals(deviceInfo.getDeviceId())) {
                deviceInfo.setConnected(defaultDeviceInfo.isConnected());
            }
        }

        posAdapter.setItems(deviceInfos);
    }

    private void loadMerchantInfos() {
        if (lib.isAppRegistered()) {
            merchantContainer.setVisibility(View.VISIBLE);
            merchant.setText(preferences.getMerchantAccount());
        } else {
            merchantContainer.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.edit_adyen_merchant)
    public void onEditAdyenMerchant() {
        AdyenRegistrationDialog adyenRegistrationDialog = new AdyenRegistrationDialog(this, lib, bus);
        adyenRegistrationDialog.setMerchant(preferences.getMerchantAccount());
        adyenRegistrationDialog.show();
    }

    private void finishConfiguration(boolean success) {
        this.setResult(success ? IntentConstants.RESULT_POS_SELECTION_OK : IntentConstants.RESULT_POS_SELECTION_KO);
        supportFinishAfterTransition();
        if (chooserMode) {
            overridePendingTransition(R.anim.no_anim, R.anim.slide_out_bottom);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishConfiguration(false);
    }
}


