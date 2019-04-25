package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.model.custom.CalendarDayEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetEventsPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.CustomerTimelineAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.SpaceItemDecoration;
import moshimoshi.cyplay.com.doublenavigation.utils.IntentUtils;
import moshimoshi.cyplay.com.doublenavigation.view.GetEventsView;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;

/**
 * Created by damien on 13/04/16.
 */
public class CustomerTimelineFragment extends BaseFragment implements GetEventsView {

    @BindView(R.id.timeline_recycler_view)
    RecyclerView recyclerView;

    @Inject
    CustomerContext customerContext;

    @Inject
    GetEventsPresenter getEventsPresenter;

    private CustomerTimelineAdapter adapter;
    private Customer preview;

    //Customer id from intent
    String customerId;

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getEventsPresenter.setView(this);
        adapter = new CustomerTimelineAdapter(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        preview = IntentUtils.getExtraFromIntent(this.getActivity().getIntent(), IntentConstants.EXTRA_CUSTOMER_PREVIEW);
        customerId = customerContext.getCustomerId();
        getEventsPresenter.getCustomerEvents(preview != null ? preview.getId() : customerId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_customer_timeline, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecycler();
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void initRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        // optimization
        recyclerView.setHasFixedSize(true);
        // set layout
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        int spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, getResources().getDisplayMetrics());
        recyclerView.addItemDecoration(new SpaceItemDecoration(spacing));
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void onEventsSuccess(List<CalendarDayEvent> calendarDayEvents) {
        adapter.setItems(calendarDayEvents);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {

    }
}
