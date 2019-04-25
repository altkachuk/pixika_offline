package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.EventSource;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.CalendarGroup;
import moshimoshi.cyplay.com.doublenavigation.model.custom.CalendarDayEvent;
import moshimoshi.cyplay.com.doublenavigation.model.custom.TimeDirection;
import moshimoshi.cyplay.com.doublenavigation.model.events.CalendarDateChosenEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.CalendarDateScrolledEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.EventsListRefresh;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CalendarEventList;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.calendar.EventDecorator;
import moshimoshi.cyplay.com.doublenavigation.utils.FadeOutFadInAnimation;
import moshimoshi.cyplay.com.doublenavigation.utils.FeatureColor;

/**
 * Created by romainlebouc on 18/04/16.
 */
public class CalendarBrowserFragment extends BaseFragment {

    @Inject
    EventBus bus;

    @BindView(R.id.calendarView)
    MaterialCalendarView materialCalendarView;

    @Inject
    CalendarEventList calendarEventList;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar_global, container, false);
    }

    private Map<String, EventDecorator> decorators = new HashMap<>();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.initCalendar();
        bus.register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bus.unregister(this);
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void initCalendar() {
        materialCalendarView.setSelectionColor(Color.parseColor(FeatureColor.getColorHex(this.getContext(),FeatureColor.PRIMARY_DARK,configHelper)));

        materialCalendarView.setSelectedDate(new Date());
        materialCalendarView.setCurrentDate(new Date());
        materialCalendarView.setShowOtherDates(MaterialCalendarView.SHOW_NONE);

        if (configHelper.getCalendarGroups() != null) {
            for (CalendarGroup calendarGroup :configHelper.getCalendarGroups()) {
                List<CalendarDay> calendarDays = new ArrayList<>();
                for (CalendarDayEvent calendarDayEvent : calendarEventList.getCalendarDayEvents(calendarEventList.getCurrentEventGroup())) {
                    calendarDays.add(calendarDayEvent.getCalendarDay());
                }
                EventDecorator eventTypeDecorator = new EventDecorator(Color.parseColor(FeatureColor.getColorHex(this.getContext(),FeatureColor.PRIMARY_LIGHT,configHelper)), calendarDays, 8);
                decorators.put(calendarGroup.getCode(), eventTypeDecorator);
            }
        }

        List<DayViewDecorator> decoratorList = new ArrayList<>();
        decoratorList.addAll(decorators.values());
        materialCalendarView.addDecorators(decoratorList);

        setDateSelectedListenerActivation(true);
        setMonthChangedListenerActivation(true);
    }

    private void setDateSelectedListenerActivation(boolean enable) {
        materialCalendarView.setOnDateChangedListener(enable ? onDateSelectedListener : null);
    }

    private void setMonthChangedListenerActivation(boolean enable) {
        materialCalendarView.setOnMonthChangedListener(enable ? onMonthChangedListener : null);
    }


    private final OnDateSelectedListener onDateSelectedListener = new OnDateSelectedListener() {
        @Override
        public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
            bus.post(new CalendarDateChosenEvent(date));
        }
    };

    private final OnMonthChangedListener onMonthChangedListener = new OnMonthChangedListener() {
        @Override
        public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
            bus.post(new CalendarDateChosenEvent(date));
        }
    };

    // -------------------------------------------------------------------------------------------
    //                                       Event(s)
    // -------------------------------------------------------------------------------------------

    @Subscribe
    public void dateScrolled(CalendarDateScrolledEvent event) {
        setDateSelectedListenerActivation(false);
        setMonthChangedListenerActivation(false);
        materialCalendarView.setCurrentDate(event.getCalendarDayEvent().getCalendarDay());
        if (EventSource.REAL.equals(event.getCalendarDayEvent().getEvent().getEventSource()) && event.isSetEventDateAsSelected()) {
            materialCalendarView.setSelectedDate(event.getCalendarDayEvent().getCalendarDay());
        }
        setDateSelectedListenerActivation(true);
        setMonthChangedListenerActivation(true);
    }

    @Subscribe
    public void onEventsListRefresh(final EventsListRefresh eventsListRefresh) {
        if (eventsListRefresh.getTimeDirection() == TimeDirection.PRESENT) {
            FadeOutFadInAnimation fadeOutFadInAnimation = new FadeOutFadInAnimation();
            fadeOutFadInAnimation.setListener(new FadeOutFadInAnimation.FadeOutFadInAnimationOnFadeOutEndListener() {
                @Override
                public void onFadeOutEndListener() {
                    refreshDecorators(eventsListRefresh);
                }
            });
            fadeOutFadInAnimation.startAnimation(materialCalendarView);
        } else {
            refreshDecorators(eventsListRefresh);
        }

    }

    private void refreshDecorators(final EventsListRefresh eventsListRefresh) {
        for (EventDecorator eventDecorator : decorators.values()) {
            eventDecorator.getDates().clear();
            EventDecorator ed = decorators.get(calendarEventList.getCurrentEventGroup());
            //TODO : ic_filter_check that ??
            if (ed != null) {
                ed.getDates().addAll(calendarEventList.getCalendarDays(calendarEventList.getCurrentEventGroup(), EventSource.REAL));
            }
            materialCalendarView.invalidateDecorators();
        }
        if (TimeDirection.PRESENT.equals(eventsListRefresh.getTimeDirection())) {
            setDateSelectedListenerActivation(false);
            setMonthChangedListenerActivation(false);
            materialCalendarView.setCurrentDate(new Date());
            materialCalendarView.setSelectedDate(new Date());
            setDateSelectedListenerActivation(true);
            setMonthChangedListenerActivation(true);
        }
    }


}
