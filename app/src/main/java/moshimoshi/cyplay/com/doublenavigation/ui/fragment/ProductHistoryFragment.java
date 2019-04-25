package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
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

import moshimoshi.cyplay.com.doublenavigation.model.business.ProductItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.history.ProductHistory;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.HistoryContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.ProductItemThumbnailAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.GridSpacingItemDecoration;


/**
 * Created by romainlebouc on 12/05/2017.
 */

public class ProductHistoryFragment extends BaseFragment {

    public final static String PRODUCT_HISTORY_FRAGMENT_TAG = "PRODUCT_HISTORY_FRAGMENT_TAG";

    @Inject
    HistoryContext historyContext;

    @Inject
    SellerContext sellerContext;

    @BindView(R.id.root_view)
    View rootView;

    @BindView(R.id.product_history)
    RecyclerView productHistoryRecyclerView;

    @BindView(R.id.product_history_title)
    TextView productHistoryTitle;

    private ProductItemThumbnailAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_history, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new ProductItemThumbnailAdapter(getActivity(), historyContext.getHistorySize());
        initRecyclerView();
        List<ProductHistory> productHistoryList = new ArrayList<>();
        if (sellerContext != null && sellerContext.getSeller() != null) {
            productHistoryList = historyContext.getProductHistory(sellerContext.getSeller().getId());
        }

        if (productHistoryList != null) {
            Collections.sort(productHistoryList);

            List<ProductItem> productItems = new ArrayList<>();
            for (ProductHistory productHistory : productHistoryList) {
                productItems.add(productHistory.getProduct());
            }
            adapter.setItems(productItems);
        }
        rootView.setVisibility(productHistoryList != null && !productHistoryList.isEmpty() ? View.VISIBLE : View.GONE);

    }

    private void initRecyclerView() {
        boolean includeEdge = true;
        int spacing = (int) getResources().getDimension(R.dimen.contextual_small_margin);
        int column = getContext().getResources().getInteger(R.integer.catalog_columns_count);
        GridLayoutManager girdLayoutManager = new GridLayoutManager(getContext(), column);
        productHistoryRecyclerView.setHasFixedSize(true);
        // set layout
        productHistoryRecyclerView.setLayoutManager(girdLayoutManager);
        productHistoryRecyclerView.setAdapter(adapter);

        productHistoryRecyclerView.addItemDecoration(new GridSpacingItemDecoration(column, spacing, includeEdge));

    }
}
