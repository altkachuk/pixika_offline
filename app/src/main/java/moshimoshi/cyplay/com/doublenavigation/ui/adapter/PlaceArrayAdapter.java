package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.ItemViewHolder;

/**
 * Created by wentongwang on 24/05/2017.
 */
public class PlaceArrayAdapter extends AbstractDropDownListAdapter {
    private static final String TAG = "PlaceArrayAdapter";
    private GoogleApiClient mGoogleApiClient;
    private AutocompleteFilter mPlaceFilter;
    private LatLngBounds mBounds;
    private ArrayList<PlaceAutocomplete> mResultList;

    /**
     * Constructor
     *
     * @param context  Context
     * @param resource Layout resource
     * @param bounds   Used to specify the search bounds
     * @param filter   Used to specify place types
     */
    public PlaceArrayAdapter(Context context, int resource, LatLngBounds bounds, AutocompleteFilter filter) {
        super(context, resource);
        mBounds = bounds;
        mPlaceFilter = filter;
        mResultList = new ArrayList<>();
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        if (googleApiClient == null || !googleApiClient.isConnected()) {
            mGoogleApiClient = null;
        } else {
            mGoogleApiClient = googleApiClient;
        }
    }


    @Override
    public String getItem(int position) {
        //return string to put in the edit text
        return mResultList.get(position).description.toString();
    }

    public String getPlaceId(int position){
        return String.valueOf(mResultList.get(position).placeId);
    }

    private ArrayList<PlaceAutocomplete> getPredictions(CharSequence constraint) {
        if (mGoogleApiClient != null) {
            PendingResult<AutocompletePredictionBuffer> results =
                    Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, constraint.toString(), mBounds, mPlaceFilter);
            // Wait for predictions, set the timeout.
            AutocompletePredictionBuffer autocompletePredictions = results.await(60, TimeUnit.SECONDS);
            final Status status = autocompletePredictions.getStatus();
            if (!status.isSuccess()) {
                Log.e(TAG, "Error getting place predictions: " + status.toString());
                autocompletePredictions.release();
                return null;
            }

            Iterator<AutocompletePrediction> iterator = autocompletePredictions.iterator();
            ArrayList<PlaceAutocomplete> resultList = new ArrayList<>();
            while (iterator.hasNext()) {
                AutocompletePrediction prediction = iterator.next();
                PlaceAutocomplete place = new PlaceAutocomplete(prediction.getPlaceId(),
                        prediction.getPrimaryText(null),
                        prediction.getFullText(null));

                if (!resultList.contains(place)) {
                    resultList.add(place);

                }
            }
            // Buffer release
            autocompletePredictions.release();
            return resultList;
        }
        return null;
    }

    @Override
    protected List<String> getDropDownResults(CharSequence constraint) {
        //show auto complete list
        mResultList = getPredictions(constraint);

        List<String> mResults = new ArrayList<>();
        if (mResultList == null) {
            return mResults;
        }
        for (PlaceAutocomplete item : mResultList) {
            mResults.add(item.fullDescription.toString());
        }

        return mResults;
    }

    class PlaceAutocomplete {

        public CharSequence placeId;
        public CharSequence description;
        public CharSequence fullDescription;

        PlaceAutocomplete(CharSequence placeId, CharSequence description, CharSequence fullDescription) {
            this.placeId = placeId;
            this.description = description;
            this.fullDescription = fullDescription;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PlaceAutocomplete that = (PlaceAutocomplete) o;

            return fullDescription != null ? fullDescription.equals(that.fullDescription) : that.fullDescription == null;

        }

        @Override
        public String toString() {
            //return string to complete in edit text
            return description.toString();
        }
    }

}