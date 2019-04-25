package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Shop;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EShopType;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetShopsPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.ProvisionDevicePresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.SplashScreenActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.constants.SharedPreferenceConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.DeviceRegistrationShopAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.LoadingView;
import moshimoshi.cyplay.com.doublenavigation.view.GetShopsView;
import moshimoshi.cyplay.com.doublenavigation.view.ProvisionDeviceView;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;
import ninja.cyplay.com.apilibrary.utils.ActivityHelper;

/**
 * Created by damien on 25/04/16.
 */
public class DeviceRegistrationFragment extends BaseFragment implements ProvisionDeviceView, GetShopsView, AdapterView.OnItemSelectedListener {

    @Inject
    GetShopsPresenter getShopsPresenter;

    @Inject
    ProvisionDevicePresenter provisionDevicePresenter;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.state_view)
    LoadingView viewContainer;

    @BindView(R.id.registration_countries_spinner)
    Spinner countriesSpinner;

    @BindView(R.id.registration_config_shops_spinner)
    AutoCompleteTextView shopsSpinnerAutoComplete;

    private Boolean editMode = false;

    private List<String> countries;
    private List<String> shops;
    private Shop selectedShop;

    private TreeMap<String, ArrayList<Shop>> countryShopList;

    private final static List<String> FIELDS = new ArrayList<>();
    private final static List<String> SORT_BYS = new ArrayList<>();

    static {

        FIELDS.add("id");
        FIELDS.add("long_name");
        FIELDS.add("mail__country");
        SORT_BYS.add("long_name");
    }

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_device_registration, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity().getIntent() != null && getActivity().getIntent().hasExtra(IntentConstants.EXTRA_EDIT_STORE)){
            editMode = getActivity().getIntent().getBooleanExtra(IntentConstants.EXTRA_EDIT_STORE, false);
        }
        getShopsPresenter.getShops(true, FIELDS, SORT_BYS, EShopType.REGULAR);
        // Set Listeners for Spinners
        countriesSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getShopsPresenter.setView(this);
        provisionDevicePresenter.setView(this);
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void setAdapterForCountry(List<String> keys) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getContext(),  android.R.layout.simple_dropdown_item_1line, keys); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        countriesSpinner.setAdapter(spinnerArrayAdapter);
    }

    private void setAdapterForShops(final List<Shop> shopList) {
        selectedShop = null;
        if (shops == null)
            shops = new ArrayList<>();
        else
            shops.clear();
        if (shopList != null) {
            for (int i = 0; i < shopList.size(); i++){
                shops.add(shopList.get(i).toString());
            }

            DeviceRegistrationShopAdapter arrayAdapter = new DeviceRegistrationShopAdapter(getContext(), android.R.layout.simple_dropdown_item_1line,
                    shopList);
            shopsSpinnerAutoComplete.setAdapter(arrayAdapter);
            shopsSpinnerAutoComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View arg0) {
                    shopsSpinnerAutoComplete.showDropDown();
                }
            });

            shopsSpinnerAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectedShop = (Shop)shopsSpinnerAutoComplete.getAdapter().getItem(position);
                }
            });


        }
    }

    // -------------------------------------------------------------------------------------------
    //                                      View Impl
    // -------------------------------------------------------------------------------------------

    @Override
    public void showLoading() {
        viewContainer.setLoadingState(LoadingView.LoadingState.LOADING);
    }

    @Override
    public void onShopsSuccess(List<Shop> shops) {
        if (countryShopList == null)
            countryShopList = new TreeMap<>();
        viewContainer.setLoadingState(LoadingView.LoadingState.LOADED);
        if (shops != null) {
            for (int i = 0; i < shops.size(); i++) {
                Shop shop = shops.get(i);
                if (shop.getMail() != null && shop.getMail().getCountry() != null) {
                    if (!countryShopList.containsKey(shop.getMail().getCountry()))
                        countryShopList.put(shop.getMail().getCountry(), new ArrayList<Shop>());
                }
                if (shop.getMail() != null && shop.getMail().getCountry() != null)
                    countryShopList.get(shop.getMail().getCountry()).add(shop);
            }
        }
        countries = new ArrayList<>(countryShopList.keySet());
        setAdapterForCountry(countries);
    }

    @Override
    public void onShopsError() {
        viewContainer.setLoadingState(LoadingView.LoadingState.FAILED);
        if (coordinatorLayout != null) {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, getString(R.string.reload_error_msg), Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    @Override
    public void onProvisionDeviceSuccess() {
        //save shopId in sharePreferences
        SharedPreferences sp = getActivity().getSharedPreferences(SharedPreferenceConstants.SP_CONFIGURATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SharedPreferenceConstants.SP_EXTRA_SHOP_ID, selectedShop.getId());
        editor.apply();

        Intent intent = new Intent(getContext(), SplashScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onProvisionDeviceError() {
        // re-show provision device view
        viewContainer.setLoadingState(LoadingView.LoadingState.LOADED);
        if (coordinatorLayout != null) {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, getString(R.string.reload_error_msg), Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    // -------------------------------------------------------------------------------------------
    //                                      Action(s)
    // -------------------------------------------------------------------------------------------

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (countries != null && countries.size() > position) {
            if (countryShopList.containsKey(countries.get(position)))
                setAdapterForShops(countryShopList.get(countries.get(position)));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // -------------------------------------------------------------------------------------------
    //                                      Action(s)
    // -------------------------------------------------------------------------------------------

    @OnClick(R.id.reload_button)
    public void onReloadClick() {
        getShopsPresenter.getShops(true, FIELDS, SORT_BYS, EShopType.REGULAR);
    }

    public void onDeviceAttachClick() {
        int countryPosition = countriesSpinner.getSelectedItemPosition();
        if (countries != null && countries.size() > countryPosition) {
            if (countryShopList.containsKey(countries.get(countryPosition))) {
                ArrayList<Shop> countryShops = countryShopList.get(countries.get(countryPosition));
                if (countryShops != null && selectedShop !=null ) {
                    if (!editMode)
                        provisionDevicePresenter.provisionDevice(selectedShop.getId());
                    else
                        provisionDevicePresenter.updateStore(selectedShop.getId());
                }
            }
        }
    }

    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {
        viewContainer.setLoadingState(LoadingView.LoadingState.LOADED);
        if (coordinatorLayout != null) {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, getString(R.string.reload_error_msg), Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.device_registration, menu);
        MenuItem cancelRegistration = menu.findItem(R.id.action_cancel_register);
        cancelRegistration.setVisible(editMode);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_cancel_register:
                ActivityHelper.goToRestartActivity();
                break;
            case R.id.action_validate_register:
                onDeviceAttachClick();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
