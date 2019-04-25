package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.CustomerActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.CustomerPreviewAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.DividerItemDecoration;
import moshimoshi.cyplay.com.doublenavigation.ui.listener.RecyclerItemClickListener;

public class CustomerPreviewListFragment extends BaseFragment {

    @BindView(R.id.customer_preview_recycler_view)
    RecyclerView customerPreviewRecyclerView;

    private List<Customer> customerPreviews;
    private CustomerPreviewAdapter customerPreviewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_customer_preview_list, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAdapter();
        initRecyclerView();
    }


    public void setCustomerPreviews(List<Customer> customerPreviews) {
        this.customerPreviews = customerPreviews;
        customerPreviewAdapter.setItems(this.customerPreviews);
    }

    public void clearCustomers() {
        customerPreviewAdapter.clearItems();
    }

    private void initAdapter() {
        customerPreviewAdapter = new CustomerPreviewAdapter(getContext(), R.layout.cell_customer_preview);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        // optimization
        customerPreviewRecyclerView.setHasFixedSize(false);
        // set layout
        customerPreviewRecyclerView.setLayoutManager(layoutManager);
        customerPreviewRecyclerView.setAdapter(customerPreviewAdapter);
        customerPreviewRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), R.drawable.simple_list_divider));
        // add onclick listener
        customerPreviewRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new CustomerClick()));
    }

    // -------------------------------------------------------------------------------------------
    //                                      Action(s)
    // -------------------------------------------------------------------------------------------

    private class CustomerClick implements RecyclerItemClickListener.OnItemClickListener {
        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent(getContext(), CustomerActivity.class);
            intent.putExtra(IntentConstants.EXTRA_CUSTOMER_PREVIEW, Parcels.wrap(customerPreviews.get(position)));
            startActivity(intent);
        }
    }
}
