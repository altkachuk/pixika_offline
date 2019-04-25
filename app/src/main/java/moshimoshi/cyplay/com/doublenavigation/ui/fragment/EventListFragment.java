package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.custom.CalendarDayEvent;
import moshimoshi.cyplay.com.doublenavigation.model.custom.TimeDirection;
import moshimoshi.cyplay.com.doublenavigation.model.events.CalendarDateChosenEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.CalendarDateScrolledEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.EventsListRefresh;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CalendarEventList;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.CalendarEventAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.LoadingView;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.SpaceItemDecoration;
import moshimoshi.cyplay.com.doublenavigation.ui.layout.LinearLayoutManagerWithSmoothScroller;
import moshimoshi.cyplay.com.doublenavigation.ui.listener.BothWaysEndlessRecyclerOnScrollListener;
import moshimoshi.cyplay.com.doublenavigation.utils.FadeOutFadInAnimation;


/**
 * Created by damien on 17/04/16.
 */
public class EventListFragment extends BaseFragment {

    @Inject
    public EventBus bus;

    @Inject
    CalendarEventList calendarEventList;

    @BindView(R.id.calendar_event_list_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.calendar_event_list_loading_view)
    LoadingView eventListLoadingView;

    private Date currentDate = null;

    public CalendarEventAdapter adapter;

    private LinearLayoutManagerWithSmoothScroller layoutManager;

    private BothWaysEndlessRecyclerOnScrollListener bothWaysEndlessRecyclerOnScrollListener = null;

    public final static long MONTH_COUNT_THRESHOLD_T0_RELOAD = 6L;

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    public EventListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new CalendarEventAdapter(getContext());
        initEndlessRecyclerScroll();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // fillStocks Adapter
        adapter.setEvents(calendarEventList.getCalendarDayEvents(calendarEventList.getCurrentEventGroup()));
        // fillStocks Recycler
        initRecycler();
        // Bus Register
        bus.register(this);
        // Loading by Default
        eventListLoadingView.setLoadingState(LoadingView.LoadingState.LOADING);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bus.unregister(this);
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void initRecycler() {
        layoutManager = new LinearLayoutManagerWithSmoothScroller(getContext(), LinearLayoutManager.VERTICAL, false);
        // optimization
        recyclerView.setHasFixedSize(true);
        // set layout
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(10));
    }

    private void initEndlessRecyclerScroll() {
        bothWaysEndlessRecyclerOnScrollListener = new BothWaysEndlessRecyclerOnScrollListener(layoutManager) {

            @Override
            public void onLoadMore(int current_page) {
                if (current_page < 0) {
                    calendarEventList.loadEvents(TimeDirection.PAST);
                } else if (current_page >= 0) {
                    calendarEventList.loadEvents(TimeDirection.FUTURE);
                }
            }

            private void postCalendarDateScolledEvent() {
                int currentFirstVisible = layoutManager.findFirstVisibleItemPosition();
                //final CalendarDayEvent calendarDayEvent = adapter.getItem(currentFirstVisible < adapter.getItemCount() -1 ?currentFirstVisible+1:currentFirstVisible);
                final CalendarDayEvent calendarDayEvent = adapter.getItem(currentFirstVisible);

                if (currentDate == null || !currentDate.equals(calendarDayEvent.getCalendarDay().getDate())) {
                    currentDate = calendarDayEvent.getCalendarDay().getDate();
                    bus.post(new CalendarDateScrolledEvent(calendarDayEvent, this.notifySelectedDate));
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // This line makes the app lagging
                /*if ( dy < 8) {
                    postCalendarDateScolledEvent();
                }*/
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    postCalendarDateScolledEvent();
                }
            }

            @Override
            public void setNotifySelectedDate(boolean notifySelectedDate) {
                this.notifySelectedDate = notifySelectedDate;
            }

        };
    }

    private void refreshItems(EventsListRefresh eventsListRefresh) {
        setEndlessRecyclerOnScrollListenerActivation(false);

        List<CalendarDayEvent> calendarDayEventList = calendarEventList.getCalendarDayEvents(calendarEventList.getCurrentEventGroup());
        adapter.setEvents(calendarDayEventList);
        eventListLoadingView.setLoadingState(LoadingView.LoadingState.LOADED);

        switch (eventsListRefresh.getTimeDirection()) {
            case PAST:
                adapter.notifyItemRangeInserted(0, eventsListRefresh.getCount());
                break;
            case PRESENT:
                adapter.notifyDataSetChanged();
                break;
            case FUTURE:
                adapter.notifyItemRangeInserted(eventsListRefresh.getPreviousCount(), eventsListRefresh.getCount());
                break;
        }

        if (eventsListRefresh.getTimeDirection() == TimeDirection.PRESENT) {
            final CalendarDayEvent firstEventAfterDate = CalendarDayEvent.getFirstCalendarDayEventAfter(adapter.getItems(),
                    CalendarDay.from(Calendar.getInstance()));
            if (firstEventAfterDate != null) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.smoothScrollToPosition(adapter.getItems().indexOf(firstEventAfterDate));
                    }
                }, 100);

            }
            this.bothWaysEndlessRecyclerOnScrollListener.setNotifySelectedDate(false);
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setEndlessRecyclerOnScrollListenerActivation(true);
            }
        }, 1000);
    }

    // -------------------------------------------------------------------------------------------
    //                                       Event(s)
    // -------------------------------------------------------------------------------------------

    @Subscribe
    public void onEventsListRefresh(final EventsListRefresh eventsListRefresh) {
        if (eventsListRefresh.getExceptionType() == null) {


            if (eventsListRefresh.getTimeDirection() == TimeDirection.PRESENT) {
                FadeOutFadInAnimation fadeOutFadInAnimation = new FadeOutFadInAnimation();
                fadeOutFadInAnimation.setListener(new FadeOutFadInAnimation.FadeOutFadInAnimationOnFadeOutEndListener() {
                    @Override
                    public void onFadeOutEndListener() {
                        EventListFragment.this.refreshItems(eventsListRefresh);
                    }
                });
                fadeOutFadInAnimation.startAnimation(recyclerView);
            } else {
                refreshItems(eventsListRefresh);
            }

        } else {
            eventListLoadingView.setLoadingState(LoadingView.LoadingState.FAILED);
        }
    }

    @Subscribe
    public void dateChosen(CalendarDateChosenEvent event) {

        setEndlessRecyclerOnScrollListenerActivation(false);

        CalendarDayEvent firstEventAfterDate = CalendarDayEvent.getFirstCalendarDayEventAfter(adapter.getItems(), event.getDay());


        if (firstEventAfterDate != null) {
            bothWaysEndlessRecyclerOnScrollListener.setNotifiyDisabledUntilEndofScroll();
            recyclerView.smoothScrollToPosition(adapter.getItems().indexOf(firstEventAfterDate));

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setEndlessRecyclerOnScrollListenerActivation(true);
            }
        }, 3000);


        if (event.getDay().getDate().getTime() - calendarEventList.getLoadedRange().getMinDate().getTime() < MONTH_COUNT_THRESHOLD_T0_RELOAD * 30L * 24L * 60L * 60L * 1000L) {
            calendarEventList.loadEvents(TimeDirection.PAST);
        }

    }

    private void setEndlessRecyclerOnScrollListenerActivation(boolean enable) {
        if (enable) {
            recyclerView.addOnScrollListener(bothWaysEndlessRecyclerOnScrollListener);
        } else {
            recyclerView.clearOnScrollListeners();
        }

    }


}
