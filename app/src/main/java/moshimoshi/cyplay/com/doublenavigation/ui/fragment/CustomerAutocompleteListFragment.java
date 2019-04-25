package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.events.CustomerAutocompleteNameClickedEvent;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.CustomerAutocompleteAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.DividerItemDecoration;
import moshimoshi.cyplay.com.doublenavigation.ui.listener.RecyclerItemClickListener;

public class CustomerAutocompleteListFragment extends BaseFragment {

    @Inject
    EventBus bus;

    @BindView(R.id.customer_autocomplete_recycler_view)
    RecyclerView customerAutocompleteRecyclerView;

    private List<String> autocompleteNames;

    // Adapter
    private CustomerAutocompleteAdapter customerAutocompleteAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_customer_autocomplete_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
    }

    public void setAutocompleteNames(List<String> names) {
        autocompleteNames = names;
        if (customerAutocompleteAdapter != null)
            customerAutocompleteAdapter.setCustomers(autocompleteNames);
    }

    public void clearCustomers() {
        if (customerAutocompleteAdapter != null)
            customerAutocompleteAdapter.clearCustomers();
    }

    private void initAdapter() {
        customerAutocompleteAdapter = new CustomerAutocompleteAdapter(getContext());
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        // optimization
        customerAutocompleteRecyclerView.setHasFixedSize(false);
        // set layout
        customerAutocompleteRecyclerView.setLayoutManager(layoutManager);
        customerAutocompleteRecyclerView.setAdapter(customerAutocompleteAdapter);
        customerAutocompleteRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), R.drawable.simple_list_divider));
        // add onclick listener
        customerAutocompleteRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new AutocompleteNameClick()));
    }

    // -------------------------------------------------------------------------------------------
    //                                      Action(s)
    // -------------------------------------------------------------------------------------------

    private class AutocompleteNameClick implements RecyclerItemClickListener.OnItemClickListener {
        @Override
        public void onItemClick(View view, int position) {
            if (autocompleteNames != null && autocompleteNames.size() > position) {
                bus.post(new CustomerAutocompleteNameClickedEvent(autocompleteNames.get(position)));
            }
        }
    }

}