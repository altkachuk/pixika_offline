package moshimoshi.cyplay.com.doublenavigation.ui.custom.form;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.AddressSuggestionConfig;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormRow;
import moshimoshi.cyplay.com.doublenavigation.model.events.AutoFillPlaceEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.DQEAutoFillPlaceEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.presenter.DQEAddressPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.DQEPlaceAutoCompleteAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.PlaceArrayAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.validator.FormValidation;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.validator.StringInputValidator;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.CountryCodeConverter;
import moshimoshi.cyplay.com.doublenavigation.utils.FormHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.GooglePlaceUtils;
import moshimoshi.cyplay.com.doublenavigation.utils.StringUtils;
import moshimoshi.cyplay.com.doublenavigation.utils.XmlUtils;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.models.abstractmodels.DQEResult;
import ninja.cyplay.com.apilibrary.utils.ReflectionUtils;

/**
 * Created by wentongwang on 26/05/2017.
 */

public class FormAddressAutoCompleteLayout extends FormEventBusView implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String DQE_LICENCE = "dqe_licence";

    @Inject
    ConfigHelper configHelper;

    @Inject
    DQEAddressPresenter dqeAddressPresenter;

    protected Context context;
    protected GoogleApiClient mGoogleApiClient;

    @Nullable
    @BindView(R.id.label_text_view)
    TextView labelTextView;

    @Nullable
    @BindView(R.id.is_required_text_view)
    TextView requiredTextView;

    @BindView(R.id.auto_complete_view)
    AutoCompleteTextView autoCompleteTextView;

    protected PlaceArrayAdapter adapter;
    protected DQEPlaceAutoCompleteAdapter dqeAdapter;

    protected GooglePlaceUtils utils;

    private boolean isloaded = false;

    private StringInputValidator stringInputValidator;

    protected AddressSuggestionConfig config;

    // default bounds for the entire France
    private static final LatLngBounds DEFAULT_BOUNDS = new LatLngBounds(new LatLng(-5.133333, 41.333333), new LatLng(9.533333, 51.083333));
    // bounds, nearby the current shop
    private LatLngBounds bounds = null;
    protected AutocompleteFilter filter;

    private boolean isSearching = false;
    private String currentQuery;

    protected String countryISO2Code;

    public FormAddressAutoCompleteLayout(Context context) {
        this(context, null);
    }

    public FormAddressAutoCompleteLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FormAddressAutoCompleteLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.form_address_auto_complete, this);
        ButterKnife.bind(this);

        stringInputValidator = new StringInputValidator(context);
        this.context = context;

        //get current shop's country
        countryISO2Code = configHelper.getCurrentShop().getMail().getCountryISO2Code();
        if (countryISO2Code == null || countryISO2Code.isEmpty()) {
            countryISO2Code = "FR";
        }

        initSuggestion();

        isloaded = true;
    }

    private void initSuggestion() {
        config = configHelper.getConfig().getFeature().getFormConfig().getAddressSuggestionConfig();
        if (config.getEnable()) {
            switch (config.getPartner()) {
                case GOOGLE_MAP:
                    utils = new GooglePlaceUtils(context);
                    initGoogleApiClient();
                    initBounds();
                    initGoogleAutocompleteFilter();
                    initGoogleAdapter();
                    break;
                case DQE:
                    initDQEPresenter();
                    initDQEAdapter();
                    break;
                default:
                    //do nothing
                    break;
            }
        }
    }


    //=========GOOGLE==============

    /**
     * create search bounds with priority by latitude and longitude
     */
    private void initBounds() {
        float lat, log;
        if (configHelper.getCurrentShop() == null) {
            return;
        }

        if (configHelper.getCurrentShop().getGeolocation() == null) {
            return;
        }

        if (configHelper.getCurrentShop().getGeolocation().getLatitude() != null &&
                configHelper.getCurrentShop().getGeolocation().getLongitude() != null) {
            //create the bounds for api show the nearby address
            lat = configHelper.getCurrentShop().getGeolocation().getLatitude();
            log = configHelper.getCurrentShop().getGeolocation().getLongitude();
            bounds = toBounds(new LatLng(lat, log), 5000);
        }
    }

    /**
     * create bounds with center latlng and radius of range
     *
     * @param center bounds center
     * @param radius bounds radius
     */
    private LatLngBounds toBounds(LatLng center, double radius) {
        LatLng southwest = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 225);
        LatLng northeast = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 45);
        return new LatLngBounds(southwest, northeast);
    }

    private void initGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    protected void initGoogleAutocompleteFilter() {
        filter = new AutocompleteFilter.Builder()
                .setCountry(countryISO2Code)
                .build();
    }

    protected void initGoogleAdapter() {
        //if current bounds is null, use default bounds
        adapter = new PlaceArrayAdapter(context,
                R.layout.cell_form_selector,
                bounds != null ? bounds : DEFAULT_BOUNDS,
                filter);

        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String placeId = adapter.getPlaceId(position);
                bus.post(new AutoFillPlaceEvent(placeId));
            }
        });
    }

    //=========!GOOGLE==============


    //=========DQE==============

    protected void initDQEPresenter() {
        String url = config.getUrlPrefix();
        dqeAddressPresenter.setBaseUrl(url);
        dqeAddressPresenter.setLicence(getLicence());
        dqeAddressPresenter.setView(new ResourceView<List<DQEResult>>() {
            @Override
            public void onResourceViewResponse(ResourceResponseEvent<List<DQEResult>> resourceResponseEvent) {
                isSearching = false;
                if (resourceResponseEvent.getResourceException() == null &&
                        resourceResponseEvent.getResource() != null) {

                    if (dqeAdapter != null)
                        dqeAdapter.setmResultList(resourceResponseEvent.getResource());
                }
            }
        });
    }

    //get DQE api licence
    private String getLicence() {
        Map<String, String> results;
        results = XmlUtils.getHashMapResource(context, R.xml.licences);
        if (results != null) {
            return results.get(DQE_LICENCE);
        } else {
            return "";
        }
    }

    protected void initDQEAdapter() {
        dqeAdapter = new DQEPlaceAutoCompleteAdapter(context,
                R.layout.cell_form_selector);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() < 3) {
                    return;
                }

                String query = s.toString().trim();
                if (!isSearching && !query.equals(currentQuery)) {
                    isSearching = true;
                    dqeAddressPresenter.getAddress(s.toString(),
                            CountryCodeConverter.getInstacne().iso2CountryCodeToIso3CountryCode(countryISO2Code));
                } else {
                    currentQuery = query;
                }
            }
        });

        autoCompleteTextView.setAdapter(dqeAdapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DQEResult result = dqeAdapter.getCompleteItem(position);
                bus.post(new DQEAutoFillPlaceEvent(result));
            }
        });
    }

    //=========!DQE==============

    @Override
    public View runValidation() {
        if (row == null || descriptor == null)
            return null;

        FormValidation formValidation = stringInputValidator.validate(autoCompleteTextView.getText().toString(), descriptor, row);
        if (!formValidation.isValidated()) {
            autoCompleteTextView.setError(formValidation.getMessage());
            return this;
        }

        if (customer != null && row != null) {
            ReflectionUtils.set(customer, row.getTag(), getValue().isEmpty() ? null : getValue());
        }
        autoCompleteTextView.setError(null);
        return null;
    }

    @Override
    public void setValue(Object val) {
        assignedValue = (String) val;
        updateInfo();
    }

    @Override
    public String getValue() {
        return autoCompleteTextView.getText().toString().trim();
    }

    public void setRow(PR_FormRow row) {
        this.row = row;
        updateInfo();
    }

    private void updateInfo() {
        if (isloaded && row != null) {
            // is Editable
            autoCompleteTextView.setEnabled(!FormHelper.isDisabled(descriptor));
            // Label
            String labelText = StringUtils.getStringResourceByName(getContext(), row.getLabel());
            labelTextView.setText(labelText);
            // isRequired
            requiredTextView.setVisibility((FormHelper.isRequired(descriptor) ? VISIBLE : GONE));
            // hint
            String placeholderText = StringUtils.getStringResourceByName(getContext(), row.getPlaceholder());
            autoCompleteTextView.setHint(placeholderText);
            // set default assignedValue
            if (assignedValue != null)
                autoCompleteTextView.setText(assignedValue);
            else {
                try {
                    String value = ReflectionUtils.get(customer, row.getTag());
                    if (value != null)
                        autoCompleteTextView.setText(value);
                } catch (Exception e) {
                    Log.e(FormAddressAutoCompleteLayout.class.getName(), e.getMessage(), e);
                }
            }
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (adapter != null)
            adapter.setGoogleApiClient(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (adapter != null)
            adapter.setGoogleApiClient(null);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        switch (visibility) {
            case VISIBLE:
                if (mGoogleApiClient != null)
                    mGoogleApiClient.connect();
                break;
            case GONE:
            case INVISIBLE:
                if (mGoogleApiClient != null)
                    mGoogleApiClient.disconnect();
                break;
        }
        super.onVisibilityChanged(changedView, visibility);

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        String error = "";
        if (connectionResult != null) {
            error = connectionResult.getErrorMessage();
        }
        Log.e(FormAddressAutoCompleteLayout.class.getName(), "google map api connect failed! " + error);
    }


}
