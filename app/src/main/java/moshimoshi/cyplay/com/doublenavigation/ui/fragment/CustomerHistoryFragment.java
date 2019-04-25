package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.model.business.history.CustomerHistory;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.HistoryContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.CustomerActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.CustomerPreviewAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.listener.RecyclerItemClickListener;

/**
 * Created by wentongwang on 17/05/2017.
 */

public class CustomerHistoryFragment extends BaseFragment {

    public final static String CUSTOMER_HISTORY_FRAGMENT_TAG = "CUSTOMER_HISTORY_FRAGMENT_TAG";

    @Inject
    HistoryContext historyContext;

    @Inject
    SellerContext sellerContext;


    @Inject
    protected CustomerContext customerContext;

    @BindView(R.id.root_view)
    View rootView;

    @BindView(R.id.product_history)
    RecyclerView recyclerView;

    @BindView(R.id.product_history_title)
    TextView historyTitle;

    @BindView(R.id.sortable_filterable_extra_margin)
    View extraMargin;

    private CustomerPreviewAdapter adapter;
    private List<Customer> customers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_history, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new CustomerPreviewAdapter(getActivity(), R.layout.cell_customer_search);
        initRecyclerView();
        List<CustomerHistory> customerHistoryList = new ArrayList<>();
        if (sellerContext != null && sellerContext.getSeller() != null) {
            customerHistoryList = historyContext.getCustomerHistory(sellerContext.getSeller().getId());
        }
        if (customerHistoryList != null) {
            Collections.sort(customerHistoryList);

            customers = new ArrayList<>();
            for (CustomerHistory customerHistory : customerHistoryList) {
                customers.add(customerHistory.getCustomer());
            }
            adapter.setItems(customers);
        }
        rootView.setVisibility(customerHistoryList != null && !customerHistoryList.isEmpty() ? View.VISIBLE : View.GONE);
        historyTitle.setText(getString(R.string.last_displayed_customer));
        extraMargin.setVisibility(View.GONE);
    }

    private void initRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // optimization
        recyclerView.setHasFixedSize(true);
        // set layout
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // add onclick listener
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new CustomerSearchItemClick()));

    }

    private class CustomerSearchItemClick implements RecyclerItemClickListener.OnItemClickListener {

        @Override
        public void onItemClick(View view, int position) {
            if (customers != null && customers.size() > position) {
                Intent intent = new Intent(getActivity(), CustomerActivity.class);
                customerContext.setCustomerId(customers.get(position).getId());
                startActivity(intent);
            }
        }
    }

}
