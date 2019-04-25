package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcels;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.CustomerActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.CustomerHistoryAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.SpaceItemDecoration;
import moshimoshi.cyplay.com.doublenavigation.ui.listener.RecyclerItemClickListener;

/**
 * Created by damien on 09/05/16.
 */
public class CustomerSearchedHistoryFragment extends BaseFragment {

    @Inject
    SellerContext sellerContext;

    @Inject
    CustomerContext customerContext;

    @BindView(R.id.customer_searched_history_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.customer_history_container)
    View container;

    @BindView(R.id.customer_history_empty_view)
    View emptyView;

    private CustomerHistoryAdapter adapter;

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_customer_searched_history, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new CustomerHistoryAdapter(getContext(), R.layout.cell_customer_preview);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Update design
        updateDesign();
        // recycler
        initRecycler();
    }

    @Override
    public void onResume() {
        super.onResume();
        // set Data each time we come back here
        if (sellerContext != null) {
            if (sellerContext.getCustomer_history() != null && sellerContext.getCustomer_history().size() > 0) {
                emptyView.setVisibility(View.GONE);
                container.setVisibility(View.VISIBLE);
                adapter.setItems(sellerContext.getCustomer_history());
            } else {
                container.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
        }
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void updateDesign() {
    }

    private void initRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // optimization
        recyclerView.setHasFixedSize(true);
        // set layout
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        int spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        recyclerView.addItemDecoration(new SpaceItemDecoration(spacing));
        // add onclick listener
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new CustomerClick()));
    }

    // -------------------------------------------------------------------------------------------
    //                                      Action(s)
    // -------------------------------------------------------------------------------------------

    private class CustomerClick implements RecyclerItemClickListener.OnItemClickListener {
        @Override
        public void onItemClick(View view, int position) {
            if (sellerContext.getCustomer_history() != null && sellerContext.getCustomer_history().size() > position) {
                customerContext.clearContext();
                Intent intent = new Intent(getContext(), CustomerActivity.class);
                intent.putExtra(IntentConstants.EXTRA_CUSTOMER_PREVIEW, Parcels.wrap(sellerContext.getCustomer_history().get(position)));
                startActivity(intent);
            }
        }
    }

}