package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.BuildConfig;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.EventSource;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.CalendarGroup;
import moshimoshi.cyplay.com.doublenavigation.model.custom.TimeDirection;
import moshimoshi.cyplay.com.doublenavigation.model.events.EventsListRefresh;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CalendarEventList;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.MenuBaseActivity;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;


/**
 * Created by romainlebouc on 18/04/16.
 */
public class CalendarActivity extends MenuBaseActivity implements AdapterView.OnItemSelectedListener {

    @Inject
    EventBus bus;

    @Inject
    CalendarEventList calendarEventList;

    @BindView(R.id.event_types_spinner)
    Spinner eventTypesSpinnger;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        // Spinner in the toolbar
        createFilterSpinner();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Remove this for Prod (instant-run issue)
        if (BuildConfig.DEBUG)
            calendarEventList.clear();
    }

    @Override
    protected void onStop() {
        super.onStop();
        calendarEventList.clear();
    }

    // -------------------------------------------------------------------------------------------
    //                                      Override(s)
    // -------------------------------------------------------------------------------------------

    @Override
    protected String getNavDrawerCurrentItem() {
        return EMenuAction.NAVDRAWER_ITEM_CALENDAR.getCode();
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void createFilterSpinner() {
        // Get all the available the options
        List<String> spinnerArray = new ArrayList<>();
        if (configHelper.getCalendarGroups() != null) {
            for (CalendarGroup calendarGroup : new ArrayList<CalendarGroup>() /*PlayRetailApp.get(this).getConfigHelper().getCalendarGroups()*/) {
                spinnerArray.add(calendarGroup.getLabel());
            }
        }

        // Create adapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this,
                R.layout.calendar_layout_drop_title,
                spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.calendar_spinner_dropdown_item);

        // Set adapter
        eventTypesSpinnger.setAdapter(spinnerArrayAdapter);
        eventTypesSpinnger.setSelection(0, false);
        eventTypesSpinnger.setOnItemSelectedListener(this);

    }

    // -------------------------------------------------------------------------------------------
    //                                      View Impl
    // -------------------------------------------------------------------------------------------

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        calendarEventList.setCurrentEventGroup(configHelper.getCalendarGroups().get(position).getCode());
        bus.post(new EventsListRefresh(TimeDirection.PRESENT,
                0,
                calendarEventList.getCalendarDays(calendarEventList.getCurrentEventGroup(), EventSource.ALL).size(),
                null));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {

    }
}
