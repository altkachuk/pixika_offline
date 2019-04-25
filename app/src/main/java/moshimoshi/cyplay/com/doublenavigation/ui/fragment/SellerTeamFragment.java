package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.BuildConfig;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Seller;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetDeviceConfigurationPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.SellerLogInActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.SellerLoginWithTemporaryPasswordChangeActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.ItemAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.LoadingView;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.GridSpacingItemDecoration;
import moshimoshi.cyplay.com.doublenavigation.ui.listener.RecyclerItemClickListener;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.SellerViewHolder;
import moshimoshi.cyplay.com.doublenavigation.view.GetSellersView;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;

/**
 * Created by romainlebouc on 20/04/16.
 */
public class SellerTeamFragment extends BaseFragment implements GetSellersView {

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.sellers_recycler_view)
    RecyclerView sellersRecyclerView;

    @BindView(R.id.sellers_loading_view)
    LoadingView sellersLoadingView;

    @BindView(R.id.version)
    TextView version;

    @Inject
    GetDeviceConfigurationPresenter getDeviceConfigurationPresenter;

    private ItemAdapter<Seller, SellerViewHolder> adapter;

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seller_team, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ItemAdapter<Seller, SellerViewHolder>(this.getContext()) {
            @Override
            public SellerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_seller, parent, false);
                return new SellerViewHolder(v);
            }
        };

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDeviceConfigurationPresenter.setView(this);
        initRecycler();
        loadSellersList();

        if (!BuildConfig.BUILD_TYPE.equals("release")) {
            version.setVisibility(View.VISIBLE);
            version.setText(BuildConfig.CLIENT_NAME + "-" + BuildConfig.BUILD_TYPE + " " + BuildConfig.VERSION_NAME);
        }


    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void initRecycler() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), getResources().getInteger(R.integer.seller_team_columns_count));
        // optimization
        sellersRecyclerView.setHasFixedSize(true);
        // set layout
        sellersRecyclerView.setLayoutManager(layoutManager);
        sellersRecyclerView.setAdapter(adapter);
        // Add 10dp spacing between items
        int spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
        sellersRecyclerView.addItemDecoration(new GridSpacingItemDecoration(getResources().getInteger(R.integer.seller_team_columns_count), spacing, true));
        // On Select an item
        sellersRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new SellerItemClick()));
    }

    public void loadSellersList() {
        this.getDeviceConfigurationPresenter.getDeviceConfiguration();
        sellersLoadingView.setLoadingState(LoadingView.LoadingState.LOADING);
    }

    // -------------------------------------------------------------------------------------------
    //                                      View Impl
    // -------------------------------------------------------------------------------------------

    @Override
    public void onSellersSuccess(List<Seller> sellers) {
        if (sellers == null || sellers.isEmpty())
            sellersLoadingView.setLoadingState(LoadingView.LoadingState.NO_RESULT);
        else
            sellersLoadingView.setLoadingState(LoadingView.LoadingState.LOADED);
        adapter.setItems(sellers);
    }

    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {
        if (this.getActivity() != null && isAdded()) {
            sellersLoadingView.setLoadingState(LoadingView.LoadingState.FAILED);
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, getString(R.string.reload_error_msg), Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    // -------------------------------------------------------------------------------------------
    //                                      Action(s)
    // -------------------------------------------------------------------------------------------

    @OnClick(R.id.reload_button)
    public void onRetry() {
        sellersLoadingView.setLoadingState(LoadingView.LoadingState.LOADING);
        getDeviceConfigurationPresenter.getDeviceConfiguration();
    }

    private class SellerItemClick implements RecyclerItemClickListener.OnItemClickListener {
        @Override
        public void onItemClick(View view, int position) {
            boolean forceChangeTmpPassword = configHelper.getAuthenticationConfig().getForce_change_tmp_password();

            Intent i = new Intent(getContext(),
                    forceChangeTmpPassword ? SellerLoginWithTemporaryPasswordChangeActivity.class : SellerLogInActivity.class);
            i.putExtra(IntentConstants.EXTRA_PASSWORD_RESET, false);
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(SellerTeamFragment.this.getActivity(), view, "sellerBadgeTransition");
            Seller s = adapter.getItems().get(position);
            i.putExtra(IntentConstants.EXTRA_SELLER, Parcels.wrap(s));
            startActivity(i, options.toBundle());
        }
    }

}
