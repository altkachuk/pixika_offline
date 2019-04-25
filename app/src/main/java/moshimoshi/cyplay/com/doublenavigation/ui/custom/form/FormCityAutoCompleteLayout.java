package moshimoshi.cyplay.com.doublenavigation.ui.custom.form;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import org.greenrobot.eventbus.Subscribe;

import moshimoshi.cyplay.com.doublenavigation.model.events.AutoFillPlaceEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.DQEAutoFillPlaceEvent;
import moshimoshi.cyplay.com.doublenavigation.utils.PlaceUtilTask;

/**
 * Created by wentongwang on 29/05/2017.
 */

public class FormCityAutoCompleteLayout extends FormAddressAutoCompleteLayout {

    public FormCityAutoCompleteLayout(Context context) {
        this(context, null);
    }

    public FormCityAutoCompleteLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FormCityAutoCompleteLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Subscribe
    public void onAutoFillPlaceEvent(AutoFillPlaceEvent autoFillPlaceEvent) {

        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                .getPlaceById(mGoogleApiClient, autoFillPlaceEvent.getPlaceId());
        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(@NonNull PlaceBuffer places) {
                if (!places.getStatus().isSuccess()) {
                    Log.e(FormCityAutoCompleteLayout.class.getName(),
                            "Error: " + places.getStatus().toString());
                    return;
                }
                // Selecting the first object buffer.
                final Place place = places.get(0);
                utils.setCallBack(new PlaceUtilTask.CallBack() {
                    @Override
                    public void onResult(String result) {
                        autoCompleteTextView.setText(result);
                        autoCompleteTextView.dismissDropDown();
                    }
                });
                utils.getCityNameByCoordinates(place.getLatLng().latitude, place.getLatLng().longitude);
            }
        });
    }


    @Override
    protected void initGoogleAutocompleteFilter() {
        filter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .setCountry(countryISO2Code)
                .build();
    }

    @Subscribe
    public void onDQEAutoFillPlaceEvent(DQEAutoFillPlaceEvent event) {
        if (event.getPlaceResult() == null) {
            return;
        }
        autoCompleteTextView.setText(event.getPlaceResult().getLocalite());
    }


    @Override
    protected void initDQEAdapter() {
        //when use DQE apis, inactive autocomplete for city
        autoCompleteTextView.setAdapter(null);
    }
}
