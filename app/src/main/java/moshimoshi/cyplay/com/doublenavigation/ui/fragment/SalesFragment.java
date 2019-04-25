package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Category;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sale;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.SalesHistoryActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.abs.AbstractCatalogActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.SaleAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.DividerItemDecoration;
import moshimoshi.cyplay.com.doublenavigation.ui.listener.RecyclerItemClickListener;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.CatalogCategoryViewHolder;

/**
 * Created by damien on 28/07/16.
 */
public class SalesFragment extends BaseFragment {

    @BindView(R.id.sale_list_recycler_view)
    RecyclerView recyclerView;

    private List<Sale> sales;

    private SaleAdapter adapter;

    ActionBar actionBar;
    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new SaleAdapter(this.getContext(), sales);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sales, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBar = ((AppCompatActivity) this.getActivity()).getSupportActionBar();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDataSet();
        setupRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void initDataSet() {
        adapter.setSales(sales);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        // optimization
        recyclerView.setHasFixedSize(true);
        // set layout
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // Add Decorator
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        // OnClick action
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new SaleClick()));
    }

    public void setSales(List<Sale> sales) {
        this.sales = sales;

        if (adapter != null) {
            adapter.setSales(sales);
        }
    }

    // -------------------------------------------------------------------------------------------
    //                                      Action(s)
    // -------------------------------------------------------------------------------------------


    private class SaleClick implements RecyclerItemClickListener.OnItemClickListener {
        @Override
        public void onItemClick(View view, int position) {
            Sale saleClicked = sales.get(position);
            if (getActivity() != null && !getActivity().isDestroyed()) {
                ((SalesHistoryActivity) getActivity()).displaySale(sales.get(position));
            }

        }
    }

}
