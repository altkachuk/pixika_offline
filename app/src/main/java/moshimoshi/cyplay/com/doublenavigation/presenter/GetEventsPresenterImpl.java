package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.model.business.Event;
import moshimoshi.cyplay.com.doublenavigation.model.events.EventsListRefresh;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CalendarDayEventList;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerEventList;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractPaginatedResourceRequestCallbackImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.SafeResourceRequestCallbackExecutor;
import moshimoshi.cyplay.com.doublenavigation.view.GetEventsView;
import ninja.cyplay.com.apilibrary.domain.interactor.EventInteractor;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AEvent;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;

/**
 * Created by romainlebouc on 16/05/16.
 */
public class GetEventsPresenterImpl extends BasePresenter implements GetEventsPresenter {

    @Inject
    APIValue apiValue;

    private GetEventsView getEventsView;
    private EventInteractor eventInteractor;

    public GetEventsPresenterImpl(Context context, EventInteractor eventInteractor) {
        super(context);
        this.eventInteractor = eventInteractor;
    }

    @Override
    public void getCustomerEvents(String attendee__id) {
        this.eventInteractor.getCustomerEvents(attendee__id,
                new AbstractPaginatedResourceRequestCallbackImpl<PR_AEvent>() {
                    @Override
                    public void onSuccess(final List<PR_AEvent> resource, Integer count, String previous, String next) {

                        super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                            @Override
                            public void execute() {
                                CalendarDayEventList.CalendarDayEventListLoadedListener calendarDayEventListLoadedListener = new CalendarDayEventList.CalendarDayEventListLoadedListener() {
                                    @Override
                                    public void onCalendarDayEventListLoaded(EventsListRefresh eventsListRefresh) {
                                        getEventsView.onEventsSuccess(eventsListRefresh.getCalendarDayEventByGroup().get("ALL"));
                                    }
                                };
                                CustomerEventList customerEventList = new CustomerEventList(apiValue);
                                customerEventList.setCalendarDayEventListLoadedListener(calendarDayEventListLoadedListener);
                                customerEventList.getCalendarDayEvents((List<Event>) (List<?>) resource);
                            }
                        });

                    }

                    @Override
                    public void onError(BaseException e) {

                    }
                }
        );

    }

    @Override
    public void setView(GetEventsView view) {
        this.getEventsView = view;
    }
}
