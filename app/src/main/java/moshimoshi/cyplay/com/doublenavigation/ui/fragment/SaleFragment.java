package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketItemOffer;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sale;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EBasketItemOfferStatus;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceException;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.SalesPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.BasketActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.SalesHistoryActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.BasketItemAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.BasketSummaryLayout;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.LoadingView;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.WSErrorDialog;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.DividerItemDecoration;
import moshimoshi.cyplay.com.doublenavigation.utils.CompatUtils;
import moshimoshi.cyplay.com.doublenavigation.view.ChangeSaleView;


/**
 * Created by damien on 01/04/16.
 */
public class SaleFragment extends BaseFragment implements ChangeSaleView {

    @Inject
    SellerContext sellerContext;

    @Inject
    CustomerContext customerContext;

    @Inject
    SalesPresenter salesPresenter;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.resource_loading_view)
    LoadingView loadingView;

    @BindView(R.id.basket_items_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.empty_basket_warning)
    TextView emptyBasketWarning;

    @BindView(R.id.basket_summary)
    BasketSummaryLayout basketSummary;

    private BasketItemAdapter adapter;
    private Sale sale;

    private AlertDialog alertDialog;

    private boolean loadingForResult = false;

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sale, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new BasketItemAdapter(this.getContext(), sale.getBasket().getItems());
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPresenter();
        initRecyclerView();

        basketSummary.setItem(sale.getBasket(), false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setView(new ProgressBar(this.getContext()));
    }

    private void initPresenter() {
        salesPresenter.setRemoveView(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.sale, menu);

        // design edit item menu
        MenuItem editItem = menu.findItem(R.id.edit_sale);
        CompatUtils.setDrawableTint(editItem.getIcon(), ContextCompat.getColor(getContext(), R.color.actionBarItem));

        // design remove item menu
        MenuItem removeItem = menu.findItem(R.id.clear_sale);
        CompatUtils.setDrawableTint(removeItem.getIcon(), ContextCompat.getColor(getContext(), R.color.actionBarItem));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ((BasketActivity) getActivity()).finishBasket();
                break;
            case R.id.edit_sale:
                editSale();
                break;
            case R.id.clear_sale:
                removeSale();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    private int getActiveOffersCount(List<BasketItemOffer> iOffers) {
        int count = 0;
        int size = iOffers.size();
        for (int i = 0; i < size; i++) {
            if (EBasketItemOfferStatus.CHOSEN.equals(iOffers.get(i).getStatus()) ||
                    EBasketItemOfferStatus.AUTOMATIC.equals(iOffers.get(i).getStatus())) {
                count++;
            }
        }
        return count;
    }

    protected void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        // optimization
        recyclerView.setHasFixedSize(true);
        // set layout
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // Add Decorator
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
    }


    // -------------------------------------------------------------------------------------------
    //                                       Method(s)
    // -------------------------------------------------------------------------------------------

    public void editSale() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        alertDialog = builder.setTitle(R.string.edit)
                .setMessage(R.string.want_to_change_order)
                .setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        salesPresenter.editSale(sale);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        alertDialog.show();
    }

    public void removeSale() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        alertDialog = builder.setTitle(R.string.delete)
                .setMessage(R.string.want_to_remove_order)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        salesPresenter.removeSale(sale.getId());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        alertDialog.show();
    }

    @Override
    public void showLoading() {
        loadingView.setLoadingState(LoadingView.LoadingState.LOADING);
    }

    @Override
    public void onRemoveSuccess() {
        if (getActivity() != null && !getActivity().isDestroyed()) {
            ((SalesHistoryActivity) getActivity()).displayAndUpdateSales();
        }
    }

    //clear basket error
    @Override
    public void onRemoveError(ResourceException e) {
        //show error dialog
        WSErrorDialog.showWSErrorDialog(getContext(),
                this.getString(R.string.clear_basket_error) + "\n" + e.toString(),
                new WSErrorDialog.DialogButtonClickListener() {
                    @Override
                    public void retry() {
                        removeSale();
                    }

                    @Override
                    public void cancel() {

                    }
                });
    }

    @Override
    public void onAddBasketItemsSuccess() {
        loadingView.setLoadingState(LoadingView.LoadingState.LOADED);

        Intent intent = new Intent(getActivity(), BasketActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.no_anim);
    }

    @Override
    public void onAddBasketItemsError(ResourceException e) {
        loadingView.setLoadingState(LoadingView.LoadingState.FAILED);
    }
}
