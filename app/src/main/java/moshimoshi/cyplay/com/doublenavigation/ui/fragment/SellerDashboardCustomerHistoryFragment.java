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
import android.widget.ImageView;

import org.parceler.Parcels;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.CustomerActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.MenuBaseActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.DashboardCustomerSearchedHistoryAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.SpaceItemDecoration;
import moshimoshi.cyplay.com.doublenavigation.ui.listener.RecyclerItemClickListener;

/**
 * Created by damien on 08/06/16.
 */
public class SellerDashboardCustomerHistoryFragment extends BaseFragment {

    @Inject
    SellerContext sellerContext;

    @Inject
    CustomerContext customerContext;

    @BindView(R.id.customer_search_icon)
    ImageView customerSearchIcon;

    @BindView(R.id.history_recycler_view)
    RecyclerView recyclerView;

    private DashboardCustomerSearchedHistoryAdapter adapter;

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seller_dashboard_customer_history, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new DashboardCustomerSearchedHistoryAdapter(getContext());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //fillStocks Recycler
        initRecyclerView();
    }


    @Override
    public void onResume() {
        super.onResume();
        // set Data each time we come back here
        if (sellerContext != null) {
            if (sellerContext.getCustomer_history() != null && sellerContext.getCustomer_history().size() > 0)
                adapter.setCustomerPreviews(sellerContext.getCustomer_history());
        }
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // optimization
        recyclerView.setHasFixedSize(true);
        // set layout
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        int spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                getResources().getDimension(R.dimen.contextual_small_margin),
                getResources().getDisplayMetrics());
        recyclerView.addItemDecoration(new SpaceItemDecoration(spacing, true, true));
        // add onclick listener
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new CustomerClick()));
    }

    // -------------------------------------------------------------------------------------------
    //                                      Action(s)
    // -------------------------------------------------------------------------------------------

    @OnClick(R.id.customer_search_icon)
    public void onSearchIconClicked() {
        if (getActivity() instanceof MenuBaseActivity) {
            //((MenuBaseActivity)getActivity()).doMenuActionFromTag(EMenuAction.NAVDRAWER_ITEM_SEARCH_CLIENT.getCode());
        }
    }

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
