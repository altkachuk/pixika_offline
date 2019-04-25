package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.custom.CalendarDayEvent;
import moshimoshi.cyplay.com.doublenavigation.presenter.CalendarPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.LoadingView;
import moshimoshi.cyplay.com.doublenavigation.view.CalendarView;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;


/**
 * Created by romainlebouc on 18/04/16.
 */
public class CalendarFragment extends BaseFragment implements CalendarView {

    @Inject
    CalendarPresenter calendarPresenter;

    @BindView(R.id.calendar_loading_view)
    LoadingView calendarLoadingView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        calendarPresenter.setView(this);
        calendarPresenter.getEvents();
        calendarLoadingView.setLoadingState(LoadingView.LoadingState.LOADING);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onEventsSuccess(List<CalendarDayEvent> calendarDayEvents) {
        calendarLoadingView.setLoadingState(LoadingView.LoadingState.LOADED);
    }

    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {
        calendarLoadingView.setLoadingState(LoadingView.LoadingState.FAILED);
    }
}
