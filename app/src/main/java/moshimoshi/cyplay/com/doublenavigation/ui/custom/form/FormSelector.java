package moshimoshi.cyplay.com.doublenavigation.ui.custom.form;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;
import moshimoshi.cyplay.com.doublenavigation.model.business.Shop;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormRow;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormRowValue;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EShopType;
import moshimoshi.cyplay.com.doublenavigation.model.events.AutoFillPlaceEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.DQEAutoFillPlaceEvent;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetShopsPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.FormSelectorAdapter;
import moshimoshi.cyplay.com.doublenavigation.utils.CountryCodeConverter;
import moshimoshi.cyplay.com.doublenavigation.utils.FormHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.GooglePlaceUtils;
import moshimoshi.cyplay.com.doublenavigation.utils.PlaceUtilTask;
import moshimoshi.cyplay.com.doublenavigation.utils.StringUtils;
import moshimoshi.cyplay.com.doublenavigation.view.GetShopsView;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;
import ninja.cyplay.com.apilibrary.utils.ReflectionUtils;

/**
 * Created by wentongwang on 26/04/2017.
 */

public class FormSelector extends FormEventBusView implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    protected GoogleApiClient mGoogleApiClient;

    @Inject
    GetShopsPresenter getShopsPresenter;

    @BindView(R.id.label_text_view)
    TextView labelTextView;

    @BindView(R.id.is_required_text_view)
    TextView requiredTextView;

    @BindView(R.id.edit_text_for_error)
    EditText editTextForError;

    @BindView(R.id.selector)
    Spinner selector;
    private FormSelectorAdapter adapter;
    private List<PR_FormRowValue> values;

    private Context context;

    private PR_FormRow row;

    private boolean isloaded = false;

    private final static List<String> FIELDS = new ArrayList<>();
    private final static List<String> SORT_BYS = new ArrayList<>();

    protected GooglePlaceUtils utils;

    static {

        FIELDS.add("id");
        FIELDS.add("long_name");
        SORT_BYS.add("long_name");
    }

    public FormSelector(Context context) {
        this(context, null);
    }

    public FormSelector(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FormSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        PlayRetailApp.get(context).inject(this);

        initGoogleApiClient();

        utils = new GooglePlaceUtils(context);

        init();

        getShopsPresenter.setView(new GetShopsView() {
            @Override
            public void showLoading() {

            }

            @Override
            public void onShopsSuccess(List<Shop> PRShops) {
                values.clear();
                for (Shop shop : PRShops) {
                    PR_FormRowValue value = new PR_FormRowValue();
                    value.key = shop.getId();
                    value.label = shop.getLong_name();
                    values.add(value);
                }
                updateInfo();
            }

            @Override
            public void onShopsError() {
                values = row.getValues();
                updateInfo();
            }

            @Override
            public void onError(ExceptionType exceptionType, String status, String message) {
                values = row.getValues();
                updateInfo();
            }
        });
    }

    private void init() {
        LayoutInflater.from(context).inflate(R.layout.form_selector_layout, this);
        ButterKnife.bind(this);
        isloaded = true;
        values = new ArrayList<>();
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

    @Override
    public void setRow(PR_FormRow row) {
        this.row = row;
        if (row.getValuesSource() != null) {
            switch (row.getValuesSource()) {
                case SHOPS:
                    getShopsPresenter.getShops(true, FIELDS, SORT_BYS, EShopType.REGULAR);
                    break;
                default:
                    values.addAll(row.getValues());
                    updateInfo();
            }
        } else {
            values.addAll(row.getValues());
            updateInfo();
        }

    }

    private void updateInfo() {
        if (isloaded && row != null) {
            // Label
            String labelText = StringUtils.getStringResourceByName(getContext(), row.getLabel());
            labelTextView.setText(labelText);
            // isRequired
            requiredTextView.setVisibility((FormHelper.isRequired(descriptor) ? VISIBLE : GONE));
            //add empty cell
            PR_FormRowValue emptyValue = new PR_FormRowValue();
            values.add(0, emptyValue);

            initSpinner();

            String value = ReflectionUtils.get(customer, row.getTag());
            setValue(value);
        }
    }

    private void initSpinner() {
        adapter = new FormSelectorAdapter(context, values);
        selector.setAdapter(adapter);
        //set hint
        String placeholderText = StringUtils.getStringResourceByName(getContext(), row.getPlaceholder());
        adapter.setPlaceHolder(placeholderText);
        //set default value
        setValue(row.defaultValue);
    }

    @Override
    public View runValidation() {
        if (row == null || descriptor == null)
            return null;

        if (FormHelper.isRequired(descriptor)) {
            //check if is empty
            PR_FormRowValue value = (PR_FormRowValue) selector.getSelectedItem();
            if (value.key == null) {
                editTextForError.setError(getContext().getString(R.string.form_required_error));
                return selector;
            }
            if (value.key.isEmpty()) {
                editTextForError.setError(getContext().getString(R.string.form_required_error));
                return selector;
            }
        }
        if (customer != null && row != null)
            ReflectionUtils.set(customer, row.getTag(), getValue());
        editTextForError.setError(null);
        return null;
    }

    @Override
    public void setValue(Object val) {
        if (val != null && values != null) {
            for (int index = 0; index < values.size(); index++) {
                if (val.equals(values.get(index).getKey())) {
                    selector.setSelection(index);
                    return;
                }
            }
        }
        selector.setSelection(0);
    }

    @Override
    public String getValue() {
        PR_FormRowValue value = (PR_FormRowValue) selector.getSelectedItem();
        return value.getKey();
    }

    @Subscribe
    public void onAutoFillPlaceEvent(AutoFillPlaceEvent autoFillPlaceEvent) {

        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                .getPlaceById(mGoogleApiClient, autoFillPlaceEvent.getPlaceId());
        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(@NonNull PlaceBuffer places) {
                if (!places.getStatus().isSuccess()) {
                    Log.e(FormAddressAutoCompleteLayout.class.getName(),
                            "Place query did not complete. Error: " + places.getStatus().toString());
                    return;
                }
                // Selecting the first object buffer.
                final Place place = places.get(0);
                utils.setCallBack(new PlaceUtilTask.CallBack() {
                    @Override
                    public void onResult(String result) {
                        setValue(result.toUpperCase());
                    }
                });
                utils.getCountryNameByCoordinates(place.getLatLng().latitude, place.getLatLng().longitude);
            }
        });
    }

    @Subscribe
    public void onDQEAutoFillPlaceEvent(DQEAutoFillPlaceEvent event) {
        if (event.getPlaceResult() == null) {
            return;
        }
        String countryISO2Code = CountryCodeConverter.getInstacne()
                .iso3CountryCodeToIso2CountryCode(event.getPlaceResult().getPays());
        setValue(countryISO2Code);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

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
